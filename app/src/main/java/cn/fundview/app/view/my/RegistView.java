package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import cn.fundview.app.action.my.LoginAction;
import cn.fundview.app.action.my.PhoneCodeAction;
import cn.fundview.app.action.my.RegistAction;
import cn.fundview.app.activity.MainActivity;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.InstallationId;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 用户注册的view
 */
public class RegistView extends ABaseWebView implements AsyncTaskCompleteListener{

    public RegistView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadUrl("file:///android_asset/page/my/regist.html");
    }

    @Override
    public void init() {

        //默认读取手机号
        this.loadUrl("javascript:Page.loadData('phone', '" + InstallationId.getPhone(context) + "')");
    }


    @Override
    public void active() {

        // super.active();
        System.out.println("active");
    }


    /****
     * js 事件处理
     */

    @JavascriptInterface
    public void setPreference(String key, String value) {

        PreferencesUtils.putString(context, key, value);
    }

    @JavascriptInterface
    public String getPreference(String key) {

        return PreferencesUtils.getString(context, key);
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    @JavascriptInterface
    public void getCode(String phone) {

        PhoneCodeAction action = new PhoneCodeAction(context, this);
        action.execute(phone);
    }


    /**
     * 用户注册
     * @param password
     */
    @JavascriptInterface
    public void regist(String password) {

        RegistAction action = new RegistAction(context, this);
        action.execute(password);
    }

    @Override
    public void complete(int requestCode, int responseCode, Object msg) {

        if(requestCode == 1 && responseCode == 2) {

            ToastUtils.show(context, "登录成功");
        }else {

            ToastUtils.show(context, msg.toString());
        }


        ((Activity)context).finish();
    }
}
