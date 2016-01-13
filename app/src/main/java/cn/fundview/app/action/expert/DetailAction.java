package cn.fundview.app.action.expert;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 专家详细 json 文件存放在 /fundview/data/expert/json/专家id/专家信息更新时间.json 文件中
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：DetailAction 类描述： 创建人：lict 创建时间：2015年6月10日
 * 上午10:13:45 修改人：lict 修改时间：2015年6月10日 上午10:13:45 修改备注：
 */
public class DetailAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer expertId;
    private String lastModify;
    /**
     * 处理结果
     */
    private Map<String, String> detail = null;
    private List<Achv> achvs;
    private int isAtt = 0;//当前登录用户是否已经关注了自己\
    private int attentNum = 0;//关注数

    public DetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer expertId, String lastModify) {

        this.expertId = expertId;
        this.lastModify = (lastModify == null || lastModify.trim().equals("")) ? "0" : lastModify;
        handle(true);

        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        // 根据有无内存卡判断
        String drivePath;
        if (DeviceConfig.isExistExtendStorage()) {
            drivePath = DeviceConfig.getExtendStoragePath(context);
        } else {
            drivePath = DeviceConfig.getLocalStoragePath(context);
        }

        // 专家详细json的存放路径
        String expertSavePath = drivePath + cn.fundview.app.tool.Constants.EXPERT_JSON_DIR + expertId + "/";//
        String fileName = lastModify + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<>();
        param.put("accountId", String.valueOf(expertId));
        if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) == cn.fundview.app.tool.Constants.LOGIN_STATUS) {

            //登录用户需要传递当前登录id
            param.put("currentId", PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.ACCOUNT_ID) + "");
            expertSavePath += "login/";
        } else {

            expertSavePath += "anonymous/";
        }

        Map<String, String> attentParam = new HashMap<>();
        if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) == cn.fundview.app.tool.Constants.LOGIN_STATUS) {

            //登录用户需要传递当前登录id, 登录后可以查询关注状态
            attentParam.put("attentId", PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.ACCOUNT_ID) + "");
            attentParam.put("beAttentId", expertId + "");

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
        attentNumParam.put("beAttentId", expertId + "");
        try {
            ResultBean attentionResultBean = JSONTools.parseResult(RService.doPostSync(attentNumParam, Constants.FIND_ATTENTION_NUM));

            if (attentionResultBean != null && attentionResultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                //请求成功
                attentNum = Integer.parseInt(attentionResultBean.getResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!FileTools.isFileExist(expertSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(expertSavePath);
            // 需要更新下载
            ResultBean expertResultBean;
            try {
                expertResultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_EXPERT_DETAIL));

                if (expertResultBean != null && expertResultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(expertResultBean.getResult().getBytes());
                    FileTools.saveDownFile(expertSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        parseJson(expertSavePath + fileName);

        if (lastModify.equals("0")) {

            //该企业不再企业列表中,修改存储的json 文件名
            File file = new File(expertSavePath + fileName);   //指定文件名及路径
            String name = expertSavePath + detail.get("updateDate") + ".json";
            file.renameTo(new File(name));   //改名
        }

        //查询专家所属成果
        AchvDao achvDao = DaoFactory.getInstance(context).getAchvDao();
        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultBean resultBean;
            Map<String, String> achvParam = new HashMap<>();
            achvParam.put("accountId", expertId + "");

            try {

                resultBean = JSONTools.parseResult(RService.doPostSync(achvParam, cn.fundview.app.domain.webservice.util.Constants.GET_ACHV_LIST_BY_ACCOUNTID_URL));
                if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {


                    if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {

                        achvs = JSON.parseArray(resultBean.getResult(), Achv.class);
                        if (achvs != null && achvs.size() > 0) {
                            for (Achv item : achvs) {

                                if (item != null) {

                                    Achv localItem = achvDao.getById(item.getId());
                                    if (localItem == null) {

                                        //添加新成果
                                        achvDao.save(item);
                                    } else if (localItem.getUpdataDate() != item.getUpdataDate()) {

                                        //更新成果信息
                                        localItem.setName(item.getName());
                                        localItem.setPrice(item.getPrice());
                                        localItem.setOwnerName(item.getOwnerName());
                                        localItem.setTradeName(item.getTradeName());
                                        localItem.setLogo(item.getLogo());

//                                        if (!localItem.getLogo().equals(item.getLogo())) {
//
//                                            localItem.setOldLocalPath(localItem.getLogo());//删除老图片的时候用
//                                            localItem.setLogo(item.getLogo());
//                                        }
                                        localItem.setUpdataDate(item.getUpdataDate());
                                        achvDao.update(localItem);
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

        if (detail != null) {

            webView.loadUrl("javascript:Page.setTitlebar('"+detail.get("name")+"')");
            //设置用户对当前的专家是否关注
            //首先判断是否是登录用户,是登录用户,查询服务器的关注状态,查询不到的时候查询本地
            if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) != cn.fundview.app.tool.Constants.LOGIN_STATUS) {

                detail.put("attMe", "-1");
            } else {
                detail.put("attMe", isAtt + "");
            }

            detail.put("attentNum", attentNum + "");
            String js = JsMethod.createJsWithJsonItems(
                    "javascript:Page.initBasic(${accountId}, ${name}, ${auth}, ${logo}, ${theUnit}, ${department},${professionalTitle}, " +
                            "${tradeNames}, ${tel}, ${addr}, ${areaNames}, ${attentNum},${attMe});", detail);

            webView.loadUrl(js);

            //专家研究领域
            String specialty = detail.get("specialty");
            boolean specialtyIsMore = false;
            if (specialty != null) {

                specialtyIsMore = specialty.length() > 60;
                if (specialtyIsMore)
                    specialty = specialty.substring(0, 60);
            }
            js = JsMethod.createJs("javascript:Page.addSpecialty(${info},${isMore}, ${id}, ${name}, ${updateDate})",
                    specialty, specialtyIsMore, expertId, detail.get("name"), detail.get("updateDate"));
            webView.loadUrl(js);

            // 显示专家介绍
            String intro = detail.get("intro");
            boolean introIsMore = false;
            if (intro != null) {

                introIsMore = intro.length() > 60;
                if (introIsMore)
                    intro = intro.substring(0, 60);
            }
            js = JsMethod.createJs("javascript:Page.addIntro(${info},${isMore}, ${id}, ${name}, ${updateDate})",
                    intro, introIsMore, expertId, detail.get("name"), detail.get("updateDate"));
            webView.loadUrl(js);


            if (achvs != null && achvs.size() > 0) {

                // 显示专家成果
                for (Achv item : achvs) {

                    js = JsMethod.createJs("javascript:Page.addImg(${id}, ${name}, ${lastModify}, ${path});",
                            item.getId(), item.getName(), item.getUpdataDate(), item.getLogo());
                    System.out.println(js);
                    webView.loadUrl(js);
                }
                String jss = JsMethod.createJs("javascript:Page.initSwiper();");
                webView.loadUrl(jss);
            } else {

                webView.loadUrl("javascript:Page.noAchv();");
            }

        } else {

            // 失败提示
            webView.loadUrl("javascript:Page.loadFailed()");
        }

        this.closeWaitDialog();
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
