package cn.fundview.app.action.my;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.InstallationId;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 登录action
 */
public class LoginAction extends ABaseAction {

    /**
     * 参数
     */
    private String username;
    private String password;
    private int requestCode;// 请求码,用于标识请求的源
    private AsyncTaskCompleteListener listener;

    /**
     * 处理结果
     **/
    private String result;

    public LoginAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(String username, String password, int requestCode) {

        this.username = username;
        this.password = password;
        this.requestCode = requestCode;
        handle(true);
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        Map<String, String> param = new HashMap<>();
        param.put("username", this.username);
        param.put("password", this.password);
        result = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.ACCOUNT_LOGIN_URL);
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (!StringUtils.isBlank(result)) {
            try {

                ResultBean resultBean;

                resultBean = JSONTools.parseResult(result);

                if (resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    // 登录成功---跳转到我的信息页面
                    // 更新preferences
                    String jsonData = resultBean.getResult();
                    JSONObject json = new JSONObject(jsonData);
                    PreferencesUtils.putInt(context, Constants.LOGIN_STATUS_KEY, Constants.LOGIN_STATUS);//用户登录状态
                    PreferencesUtils.putString(context, Constants.ACCOUNT_KEY, json.getString("username"));//用户账号
                    PreferencesUtils.putString(context, Constants.PASSWORD_KEY, password);//用户密码
                    PreferencesUtils.putInt(context, Constants.ACCOUNT_TYPE_KEY, json.getInt("type"));//用户类型
                    PreferencesUtils.putInt(context, Constants.ACCOUNT_ID, json.getInt("id"));//用户id

                    UserInfor userInfor = new UserInfor();
                    userInfor.setType(json.getInt("type"));
                    userInfor.setAccount(json.getString("username"));
                    userInfor.setAuth(json.getBoolean("isAuth"));
                    userInfor.setId(json.getInt("id"));
                    userInfor.setDeviceId(InstallationId.getDriverId(context));
                    DaoFactory.getInstance(context).getUserInforDao().saveOrUpdate(userInfor);
                    if (listener != null) {

                        listener.complete(requestCode, cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS, null);//参数是将要跳转到的action 响应码标识 登录成功还是失败
                    }
                } else {

                    // 登录失败  跳转到登录页面
                    if (listener != null) {

                        listener.complete(requestCode, 0, resultBean.getMessage());
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {

            Toast.makeText(context, "服务器端异常", Toast.LENGTH_LONG).show(); //
        }
    }

    public void regListener(AsyncTaskCompleteListener listener) {
        this.listener = listener;
    }

    public void unRegListener() {

        this.listener = null;
    }


}
