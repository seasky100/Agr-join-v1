package cn.fundview.app.action.company;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.ProductDao;
import cn.fundview.app.domain.dao.RequDao;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Product;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.MenuItem;

/**
 * @author dell 企业详细信息action
 */
public class DetailAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer compId;
    private String lastModify;

    /**
     * 处理结果
     */
    private Map<String, String> detail = null;
    private List<Requ> requs;
    private List<Product> products;
    private int isAtt = 0;//当前登录用户是否已经关注了自己\
    private int attentNum = 0;//关注数

    public DetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer compId, String lastModify) {

        this.compId = compId;
        this.lastModify = (lastModify == null || lastModify == "") ? "0" : lastModify;
        handle(true);

        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        // 根据有无内存卡判断
        String drivePath = "";
        if (DeviceConfig.isExistExtendStorage()) {
            drivePath = DeviceConfig.getExtendStoragePath(context);
        } else {
            drivePath = DeviceConfig.getLocalStoragePath(context);
        }

        // 企业详细json的存放路径
        String compSavePath = drivePath + cn.fundview.app.tool.Constants.COMPANY_JSON_DIR + compId + "/";//
        String fileName = lastModify + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<>();
        param.put("accountId", String.valueOf(compId));
        if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) == cn.fundview.app.tool.Constants.LOGIN_STATUS) {

            //登录用户需要传递当前登录id
            param.put("currentId", PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.ACCOUNT_ID) + "");
            compSavePath += "login/";
        } else {

            compSavePath += "anonymous/";
        }
        Map<String, String> attentParam = new HashMap<>();
        if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) == cn.fundview.app.tool.Constants.LOGIN_STATUS) {

            //登录用户需要传递当前登录id, 登录后可以查询关注状态
            attentParam.put("attentId", PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.ACCOUNT_ID) + "");
            attentParam.put("beAttentId", compId + "");

            try {
                ResultBean attentionResultBean = JSONTools.parseResult(RService.doPostSync(attentParam, Constants.IS_ATTENTION_URL));

                if (attentionResultBean != null && attentionResultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                    //请求成功
                    isAtt = Integer.parseInt(attentionResultBean.getResult());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Map<String, String> attentNumParam = new HashMap<>();
        attentNumParam.put("beAttentId", compId + "");
        try {
            ResultBean attentionResultBean = JSONTools.parseResult(RService.doPostSync(attentNumParam, Constants.FIND_ATTENTION_NUM));

            if (attentionResultBean != null && attentionResultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                //请求成功
                attentNum = Integer.parseInt(attentionResultBean.getResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!FileTools.isFileExist(compSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(compSavePath);
            // 需要更新下载
            ResultBean resultBean = null;
            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_COMP_DETAIL));

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(resultBean.getResult().getBytes());
                    FileTools.saveDownFile(compSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        parseJson(compSavePath + fileName);

        if (lastModify.equals("0")) {

            //该企业不再企业列表中,修改存储的json 文件名
            File file = new File(compSavePath + fileName);   //指定文件名及路径
            String name = compSavePath + detail.get("updateDate") + ".json";
            file.renameTo(new File(name));   //改名
        }

        //查询企业需求
        RequDao requDao = DaoFactory.getInstance(context).getRequDao();
        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultBean resultBean;
            Map<String, String> requParam = new HashMap<>();
            requParam.put("accountId", compId + "");

            try {

                resultBean = JSONTools.parseResult(RService.doPostSync(requParam, cn.fundview.app.domain.webservice.util.Constants.GET_REQU_BY_ACCOUNTID_LIST_URL));
                if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {
                    if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {

                        requs = JSON.parseArray(resultBean.getResult(), Requ.class);
                        if (requs != null && requs.size() > 0) {
                            for (Requ item : requs) {

                                if (item != null) {

                                    Requ localItem = requDao.getById(item.getId());
                                    if (localItem == null) {

                                        //添加新成果
                                        requDao.save(item);
                                    } else if (localItem.getUpdateTime() != item.getUpdateTime()) {

                                        //更新成果信息
                                        localItem.setName(item.getName());
                                        localItem.setFinPlan(item.getFinPlan());
                                        localItem.setOwnerName(item.getOwnerName());
                                        localItem.setTradeName(item.getTradeName());
                                        if (!localItem.getLogo().equals(item.getLogo())) {

                                            localItem.setLogoLocalPath(localItem.getLogo());//删除老图片的时候用
                                            localItem.setLogo(item.getLogo());
                                        }
                                        localItem.setHj(item.getHj());
                                        localItem.setOtherHj(item.getOtherHj());
                                        localItem.setUpdateTime(item.getUpdateTime());
                                        requDao.update(localItem);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ProductDao productDao = DaoFactory.getInstance(context).getProductDao();
        //查询企业产品
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultBean resultBean;
            Map<String, String> productParam = new HashMap<>();
            productParam.put("accountId", compId + "");


            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(productParam, Constants.GET_COMP_PRODUCT_LIST_URL));
                if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                    if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {

                        products = JSON.parseArray(resultBean.getResult(), Product.class);
                        if (products != null && products.size() > 0) {
                            for (Product item : products) {

                                if (item != null) {

                                    Product localItem = productDao.getById(item.getId());
                                    if (localItem == null) {

                                        //添加新产品
                                        item.setCompId(compId);
                                        productDao.save(item);
                                    } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                        //更新产品信息
                                        localItem.setName(item.getName());
                                        if (!localItem.getLogo().equals(item.getLogo())) {

                                            localItem.setLocaLogo(localItem.getLogo());//删除老图片的时候用
                                            localItem.setLogo(item.getLogo());
                                        }
                                        localItem.setUpdateDate(item.getUpdateDate());
                                        productDao.update(localItem);
                                        item.setLocaLogo(localItem.getLocaLogo());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        //设置用户对当前的企业是否关注
        //首先判断是否是登录用户,是登录用户,查询服务器的关注状态,查询不到的时候查询本地
        if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) != cn.fundview.app.tool.Constants.LOGIN_STATUS) {

            detail.put("attMe", "-1");
        } else {
            detail.put("attMe", isAtt + "");
        }

        detail.put("attentNum", attentNum + "");
        if (detail != null) {

            webView.loadUrl("javascript:Page.setTitlebar('"+detail.get("name")+"')");


            //id, logo, name, auth, attentNum, isAttented, expoNo, trade, tel, area, regTime, regCapital, regType, addr
            String js = JsMethod.createJsWithJsonItems("javascript:Page.init(${accountId}, ${logo}, ${name}, ${auth}, ${attentNum}, ${attMe}, ${expoNo},${tradeNames}, " +
                    "${tel}, ${areaNames}, ${regTime}," +
                    "${regCapital}, ${regType}, ${addr});", detail);
            System.out.println(js);
            webView.loadUrl(js);

            // 显示企业经营范围
            String service = detail.get("service");
            boolean serviceIsMore = false;
            if (service != null) {

                serviceIsMore = service.length() > 60;
                if (serviceIsMore)
                    service = service.substring(0, 60);
            }
            js = JsMethod.createJs("javascript:Page.addService(${service}, ${isMore}, ${id}, ${name}, ${updateDate})", service, serviceIsMore, compId, detail.get("name"), detail.get("updateDate"));
            webView.loadUrl(js);


            // 显示企业介绍
            String info = detail.get("info");
            boolean infoIsMore = false;
            if (info != null) {

                infoIsMore = info.length() > 60;
                if (infoIsMore)
                    info = info.substring(0, 60);
            }
            js = JsMethod.createJs("javascript:Page.addInfo(${info},${isMore}, ${id}, ${name}, ${updateDate})", info, infoIsMore, compId, detail.get("name"), detail.get("updateDate"));
            webView.loadUrl(js);

            if (requs != null && requs.size() > 0) {

                // 显示企业需求
                for (Requ item : requs) {

                    String hj = item.getHj() == null ? "" : item.getHj();
                    String otherHj = item.getOtherHj() == null ? "" : item.getOtherHj();
                    //添加需求requId, name, hj, price, ownerName,lastModify
                    js = JsMethod.createJs("javascript:Page.addRequ(${id}, ${title}, ${hj}, ${price}, ${ownerName}, ${lastModify})",
                            item.getId(), item.getName(), hj + otherHj, item.getFinPlan(), item.getOwnerName(), item.getUpdateTime());
                    webView.loadUrl(js);
                }
            } else {

                webView.loadUrl("javascript:Page.noRequ();");
            }

            if (products != null && products.size() > 0) {

                for (Product item : products) {

                    String oldLogoName = item.getLocaLogo();
                    if (oldLogoName != null && oldLogoName.trim() != "") {

                        oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                    }
                    js = JsMethod.createJs("javascript:Page.addImg(${id}, ${name}, ${lastModify}, ${path}, ${localImg});",
                            item.getId(), item.getName(), item.getUpdateDate(), item.getLogo(), oldLogoName);
                    webView.loadUrl(js);
                }

            } else {

                webView.loadUrl("javascript:Page.noProduct();");
            }

        } else {

            this.closeWaitDialog();
            // 失败提示
            webView.loadUrl("javascript:Page.loadFailed()");

        }
        this.closeWaitDialog();
        String js = JsMethod.createJs("javascript:Page.initSwiper();");
        webView.loadUrl(js);
    }

    // 解析Json 文件

    private void parseJson(String filePath) {

        try {

            detail = JSONTools.parseJsonFile(new File(filePath));
            System.out.println(detail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
