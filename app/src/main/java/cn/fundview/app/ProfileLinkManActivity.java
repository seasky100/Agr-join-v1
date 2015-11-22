package cn.fundview.app;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 用户中心 企业联系人activity
 */
public class ProfileLinkManActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_link_man);
        if (getIntent().getBooleanExtra("editable", true)) {

            setCommonTitleBar("企业联系人", R.id.webView, "保存", false);
        } else {

            setCommonTitleBar("企业联系人", R.id.webView, null, false);
        }
    }
}
