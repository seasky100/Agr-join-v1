package cn.fundview.app.activity.my;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 忘记密码activity
 */
public class ForgetPasswordActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        this.setCommonTitleBar("找回密码", R.id.webView, null, false);
    }
}
