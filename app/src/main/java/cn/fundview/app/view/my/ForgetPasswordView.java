package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.ForgetPasswordPhoneCodeAction;
import cn.fundview.app.action.my.ForgetPasswordResetPasswordAction;
import cn.fundview.app.activity.my.LoginActivity;
import cn.fundview.app.tool.Installation;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 忘记密码 view
 */
public class ForgetPasswordView extends ABaseWebView implements AsyncTaskCompleteListener{

    public ForgetPasswordView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadUrl("file:///android_asset/page/my/forget-password.html");
    }

    @Override
    public void init() {

        //默认读取手机号
        this.loadUrl("javascript:Page.loadData('phone', '" + Installation.getPhone(context) + "')");
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

        ForgetPasswordPhoneCodeAction action = new ForgetPasswordPhoneCodeAction(context, this);
        action.execute(phone);
    }


    /**
     * 重设密码
     * @param password
     */
    @JavascriptInterface
    public void updatePassword(String password) {

        ForgetPasswordResetPasswordAction action = new ForgetPasswordResetPasswordAction(context, this);
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

    /**
     * 找回密码成功后进行登录
     */
    public void toLogin() {

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
