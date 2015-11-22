package cn.fundview.app.activity.my;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 用户注册 activity
 */
public class RegistActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        this.setCommonTitleBar("用户注册", R.id.webview, null, false );
    }
}
