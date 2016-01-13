package cn.fundview.app.action.my;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.my.ForgetPasswordView;

/**
 * 忘记密码中的 更新密码
 */
public class ForgetPasswordResetPasswordAction extends ABaseAction {

    /**
     * 参数
     */
    private String password;

    /**
     * 处理结果
     **/
    private String result;

    public ForgetPasswordResetPasswordAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(String password) {

        this.password = password;
        handle(true);
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        Map<String, String> param = new HashMap<>();
        param.put("username", PreferencesUtils.getString(context, "forget_password_username"));
        param.put("password", this.password);
        result = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.FORGET_PASSWORD_UPDATE_PASSWORD_URL);
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

                if (resultBean != null) {
                    if (resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                        //修改成功,提示用户去登录
                        ((ForgetPasswordView)webView).toLogin();
                    } else {

                        ToastUtils.show(context, resultBean.getMessage());
                    }
                } else {

                    ToastUtils.show(context, "修改密码失败");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ToastUtils.show(context, "修改密码失败");
            }


        } else {

            Toast.makeText(context, "服务器端异常", Toast.LENGTH_LONG).show(); //
        }
    }


}
