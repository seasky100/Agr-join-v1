package cn.fundview.app.action.home;

import android.content.Context;
import android.support.v7.internal.widget.ActivityChooserView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.RequDao;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 首页action
 */
public class IndexAction extends ABaseAction {

    // result
    private List<Achv> achvList;// 优质成果
    private List<Requ> requList;// 技术需求

    //param
    private boolean firstIncome;


    public IndexAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    public void execute(boolean firstIncome) {

        this.firstIncome = firstIncome;
        // 执行异步操作
        handle(true);
    }

    /**
     * 异步处理
     * 成果的推荐处理  可以从server下载数据的时候,使用server 端的数据,不再从本地查询,只是更新/保存本地数据
     * 无法连接server的时候,从本地查询推荐的成果
     **/
    protected void doAsynchHandle() {

        // 查询网络成功,将数据添加或更新到本地,显示
        // 失败 查询本地.成功,显示 失败,提示查询失败

        Map<String, String> param = new HashMap<>();
        param.put("pageSize", "4");
        ResultBean resultBean = null;
        try {
            if (NetWorkConfig.checkNetwork(context)) {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_HOME_ACHV_LIST_URL));
            }
            if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                JSONObject jsonObject = JSON.parseObject(resultBean.getResult());
                if (jsonObject != null) {

                    String jsonResult = jsonObject.getString("resultList");
                    if (!StringUtils.isBlank(jsonResult)) {

                        achvList = JSON.parseArray(jsonResult, Achv.class);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        AchvDao achvDao = DaoFactory.getInstance(context).getAchvDao();

        if (achvList != null && achvList.size() > 0) {

            //保存/更新成果
            for (Achv item : achvList) {

                Achv localAchv = achvDao.getById(item.getId());
                if (localAchv != null) {

                    //更新本地
                    localAchv.setUpdataDate(item.getUpdataDate());
                    if (localAchv.getLogo() != item.getLogo()) {

                        //本地的logo 和服务端的logo 不一致的时候,更新logo
                        item.setOldLocalPath(localAchv.getLogo());
                        localAchv.setOldLocalPath(localAchv.getLogo());
                        localAchv.setLogo(item.getLogo());
                    }
                    localAchv.setName(item.getName());
                    localAchv.setPrice(item.getPrice());
                    localAchv.setOwnerName(item.getOwnerName());
                    localAchv.setTradeName(item.getTradeName());
                    localAchv.setRecommend(1);//设置为推荐成果
                    achvDao.update(localAchv);//更新本地
                } else {

                    item.setRecommend(1);//设置为推荐成果
                    achvDao.save(item);//保存成果
                }
            }
        } else {

            achvList = achvDao.getRecommendList(4);
        }


        //需求
        Map<String, String> paramRequ = new HashMap<>();
        paramRequ.put("pageSize", "4");
        ResultBean resultBeanRequ = null;
        try {
            if (NetWorkConfig.checkNetwork(context)) {
                resultBeanRequ = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_HOME_REQU_LIST_URL));
            }
            if (resultBeanRequ != null && resultBeanRequ.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                JSONObject jsonObject = JSON.parseObject(resultBeanRequ.getResult());
                if (jsonObject != null) {

                    String jsonResult = jsonObject.getString("resultList");
                    if (!StringUtils.isBlank(jsonResult)) {

                        requList = JSON.parseArray(jsonResult, Requ.class);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        RequDao requDao = DaoFactory.getInstance(context).getRequDao();

        if (requList != null && requList.size() > 0) {

            //保存/更新成果
            for (Requ item : requList) {

                Requ localRequ = requDao.getById(item.getId());
                if (localRequ != null) {

                    //更新本地
                    localRequ.setUpdateTime(item.getUpdateTime());
                    if (localRequ.getLogo() != item.getLogo()) {

                        //本地的logo 和服务端的logo 不一致的时候,更新logo
                        item.setLogoLocalPath(localRequ.getLogo());
                        localRequ.setLogoLocalPath(localRequ.getLogo());
                        localRequ.setLogo(item.getLogo());
                    }
                    localRequ.setName(item.getName());
                    localRequ.setFinPlan(item.getFinPlan());
                    localRequ.setOwnerName(item.getOwnerName());
                    localRequ.setTradeName(item.getTradeName());
                    localRequ.setRecommend(1);//设置为推荐成果
                    requDao.update(localRequ);//更新本地
                } else {

                    item.setRecommend(1);//设置为推荐成果
                    requDao.save(item);//保存成果
                }
            }
        } else {

            requList = requDao.getRecommendList(4);
        }

    }

    /**
     * 执行处理结果
     **/

    protected void doHandleResult() {

        String[] imgs = new String[10];
        imgs[0] = "http://static.fundview.cn/thumb/ad/banner1.jpg";
        imgs[1] = "http://static.fundview.cn/thumb/ad/banner2.jpg";
        imgs[2] = "http://static.fundview.cn/thumb/ad/banner3.jpg";

        String js = "";
        if (NetWorkConfig.checkNetwork(context)) {

            webView.loadUrl("javascript:Page.clearImg();");
            for (String path : imgs) {

                if (path != null && path.trim() != "") {
                    js = JsMethod.createJs("javascript:Page.addImg(${path});", path);
                    System.out.println(js);
                    webView.loadUrl(js);
                }
            }
//
            if (firstIncome) {

                js = JsMethod.createJs("javascript:Page.initSwiper();");
                webView.loadUrl(js);
            }
        }else {

            webView.loadUrl("javascript:Page.hideSlideImg();");
        }



//        清空数据
        webView.loadUrl("javascript:Page.clearData();");

        if (null != achvList && achvList.size() > 0) {

            // 循环加载项目
            for (Achv item : achvList) {

                String oldLogoName = item.getOldLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                js = JsMethod.createJs(
                        "javascript:Page.addAchv(${id}, ${logo}, ${name}, ${trade}, ${price}, ${ownerName}, ${oldLogo}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getPrice(), item.getOwnerName(), oldLogoName, item.getUpdataDate());
                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadAchvFailed();");
        }

        //加载需求
        if (null != requList && requList.size() > 0) {
            // 循环加载项目
            for (Requ item : requList) {

                String oldLogoName = item.getLogoLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //requId, logo, name, hj, price, oldLogo, ownerName,lastModify
                js = JsMethod.createJs("javascript:Page.addRequ(${id}, ${logo}, ${name}, ${hj},${price},${oldLogo}, ${ownerName}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getHj(), item.getFinPlan(), oldLogoName, item.getOwnerName(), item.getUpdateTime());

                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadRequFailed();");
        }

        this.closeWaitDialog();
    }

}
