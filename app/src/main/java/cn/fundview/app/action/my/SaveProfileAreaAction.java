package cn.fundview.app.action.my;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

public class SaveProfileAreaAction extends ABaseAction {

    /**
     * 参数
     **/
    private String areaIds;     //地区 id
    private String areaNames;   //地区 name
    private int uid;

    /**
     * 处理结果
     ***/
    private boolean result;

    public SaveProfileAreaAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String areaIds, String areaNames, int uid) {

        this.areaIds = areaIds;
        this.areaNames = areaNames;

        this.uid = uid;
        handle(true);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doAsynchHandle() {

        if (null != areaNames && !"".equals(areaNames) && null != areaIds && !"".equals(areaIds) ) {

            if(areaNames.endsWith(",")) {

                areaNames = areaNames.substring(0,areaNames.length()-1);
            }


            if(areaIds.endsWith(",")) {

                areaIds = areaIds.substring(0,areaIds.length()-1);
            }
            // 上传到网络
            Map<String, String> param = new HashMap<>();
            param.put("accountId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");
            param.put("attName", UserInfor.SERVER_AREA_IDS);
            param.put("attValue", areaIds);
            String jsonReturn = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.UPDATE_USERINFOR_ATTR_URL);

            try {
                ResultBean resultBean = JSONTools.parseResult(jsonReturn);

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    UserInfor userInfor = new UserInfor();
                    userInfor.setAreaIds(areaIds);
                    userInfor.setId(uid);
                    result = DaoFactory.getInstance(context).getUserInforDao().saveOrUpdate(userInfor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 上传到网络
            Map<String, String> param1 = new HashMap<>();
            param1.put("accountId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");
            param1.put("attName", UserInfor.SERVER_AREA_NAMES);
            param1.put("attValue", areaNames);
            String jsonReturn1 = RService.doPostSync(param1, cn.fundview.app.domain.webservice.util.Constants.UPDATE_USERINFOR_ATTR_URL);

            try {

                ResultBean resultBean = JSONTools.parseResult(jsonReturn1);
                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    UserInfor userInfor = new UserInfor();
                    userInfor.setArea(areaNames);
                    userInfor.setId(uid);
                    result = DaoFactory.getInstance(context).getUserInforDao().saveOrUpdate(userInfor);
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

        if (result)
            ((Activity) context).finish();
        else
            webView.loadUrl("javascript:Page.hintFail();");
    }

}
