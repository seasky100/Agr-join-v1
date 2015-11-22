package cn.fundview.app.activity.my;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 登录 activity
 **/
public class LoginActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setCommonTitleBar("用户登录",R.id.Webview, "注册",false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // LoginView view = (LoginView) this.findViewById(R.id.webView);
        // view.onRestart();
    }
}
