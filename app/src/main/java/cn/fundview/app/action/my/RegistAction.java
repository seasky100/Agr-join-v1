package cn.fundview.app.action.my;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.my.RegistView;

/**
 * 注册action
 */
public class RegistAction extends ABaseAction {

    /**
     * 参数
     */
    private String password;

    /**
     * 处理结果
     **/
    private String result;

    public RegistAction(Context context, ABaseWebView webView) {
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
        param.put("phone", PreferencesUtils.getString(context, "phone"));
        param.put("password", this.password);
        param.put("type", PreferencesUtils.getString(context, "accountType"));
        result = RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.REGIST_URL);
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

                        //注册成功,使用用户名和密码进行登录
                        String username = PreferencesUtils.getString(context, "phone");

                        LoginAction action = new LoginAction(context, webView);
                        action.regListener((RegistView)webView);
                        action.execute(username, password, 1);
                    } else {

                        ToastUtils.show(context, resultBean.getMessage());
                    }
                } else {

                    ToastUtils.show(context, "注册失败");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ToastUtils.show(context, "注册失败");
            }


        } else {

            Toast.makeText(context, "服务器端异常", Toast.LENGTH_LONG).show(); //
        }
    }


}
