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

public class SaveProfileAddrAction extends ABaseAction {

    /**
     * 参数
     **/
    private String addr;
    private String callback;
    private int uid;

    /**
     * 处理结果
     ***/
    private boolean result;

    public SaveProfileAddrAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String addr, String callback, int uid) {

        this.addr = addr;
        this.callback = callback;
        this.uid = uid;
        handle(true);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doAsynchHandle() {

        if (null != addr && !"".equals(addr)) {
            // 上传到网络
            Map<String, String> param = new HashMap<>();
            param.put("accountId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");
            if (PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY) == UserInfor.PERSON_TYPE) {


                param.put("attName", UserInfor.SERVER_ADDRESS);
                //个人角色特殊处理
            } else {

                param.put("attName", UserInfor.SERVER_ADDR);
            }
            param.put("attValue", addr);
            String jsonReturn = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.UPDATE_USERINFOR_ATTR_URL);

            try {
                ResultBean resultBean = JSONTools.parseResult(jsonReturn);

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    UserInfor userInfor = new UserInfor();
                    userInfor.setAddr(addr);
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

        String js = JsMethod.createJs("javascript:" + callback + "(${result});", result);
        webView.loadUrl(js);
    }

}
