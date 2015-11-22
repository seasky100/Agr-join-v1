package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import cn.fundview.app.action.my.InitMyPageAction;
import cn.fundview.app.action.my.LoginAction;
import cn.fundview.app.activity.MainActivity;
import cn.fundview.app.activity.my.ForgetPasswordActivity;
import cn.fundview.app.activity.my.LoginActivity;
import cn.fundview.app.activity.my.RegistActivity;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.AsyncTaskCompleteListener;

public class LoginView extends ABaseWebView implements AsyncTaskCompleteListener {

    private String backPage;
    private static final int REQUESTCODE = 1;

    public LoginView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadUrl("file:///android_asset/page/my/login.html");
    }

    @Override
    public void init() {

    }

    @Override
    public void onClickLeftBtn() {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("flag", 3);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    @Override
    public void onClickRight() {
        super.onClickRight();

        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    @Override
    public void active() {

        // super.active();
        System.out.println("active");
    }

    @JavascriptInterface
    public void login(String username, String password) {

        LoginAction action = new LoginAction(context, this);
        action.regListener(this);
        action.execute(username, password, REQUESTCODE);
    }

    @JavascriptInterface
    public void forgetPassword() {
        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, ForgetPasswordActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

    }

    @JavascriptInterface
    public void failHint(final String hintValue) {
        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(context, hintValue, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void complete(int requestCode, int responseCode, Object msg) {
        // TODO Auto-generated method stub
        if (REQUESTCODE == requestCode) {

            if (responseCode == Constants.REQUEST_SUCCESS) {

                ((Activity) context).finish();
            } else if (responseCode == 0) {

                ToastUtils.show(context, msg.toString());
//				Intent intent = new Intent(context, LoginActivity.class);
//				context.startActivity(intent);
            }
        }
    }

}
