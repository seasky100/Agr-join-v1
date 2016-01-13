package cn.fundview.app.action.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 我的infor保存
 **/
public class SaveProfileInforAction extends ABaseAction {

    /**
     * 参数
     **/
    private String infor;
    private String callback;
    private int uid;

    /**
     * 处理结果
     ***/
    private boolean result;

    public SaveProfileInforAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String infor, String callback, int uid) {

        this.infor = infor;
        this.callback = callback;
        this.uid = uid;
        handle(true);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doAsynchHandle() {

        int type = PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY);
        if (null != infor && !"".equals(infor)) {

            // 上传到网络
            Map<String, String> param = new HashMap<>();
            param.put("accountId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");

            switch (type) {

                case UserInfor.EXPERT_TYPE:
                    param.put("attName", UserInfor.SERVER_SPECIALTY);
                    param.put("attValue", infor);
                    String jsonReturn = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.UPDATE_USERINFOR_ATTR_URL);

                    try {
                        ResultBean resultBean = JSONTools.parseResult(jsonReturn);

                        if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                            UserInfor userInfor = new UserInfor();
                            userInfor.setSpecialty(infor);
                            userInfor.setId(uid);
                            result = DaoFactory.getInstance(context).getUserInforDao().saveOrUpdate(userInfor);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UserInfor.PERSON_TYPE:
                    param.put("attName", UserInfor.SERVER_WORK);
                    param.put("attValue", infor);
                    String jsonReturn1 = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.UPDATE_USERINFOR_ATTR_URL);

                    try {
                        ResultBean resultBean = JSONTools.parseResult(jsonReturn1);

                        if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                            UserInfor userInfor = new UserInfor();
                            userInfor.setWork(infor);
                            userInfor.setId(uid);
                            result = DaoFactory.getInstance(context).getUserInforDao().saveOrUpdate(userInfor);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UserInfor.COMPANY_TYPE:
                    param.put("attName", UserInfor.SERVER_SERVICE);
                    param.put("attValue", infor);
                    String jsonReturn2 = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.UPDATE_USERINFOR_ATTR_URL);

                    try {
                        ResultBean resultBean = JSONTools.parseResult(jsonReturn2);

                        if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                            UserInfor userInfor = new UserInfor();
                            userInfor.setService(infor);
                            userInfor.setId(uid);
                            result = DaoFactory.getInstance(context).getUserInforDao().saveOrUpdate(userInfor);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        String js = JsMethod.createJs(
                "javascript:" + callback + "(${result});", result);
        webView.loadUrl(js);
    }
}
