package cn.fundview.app.activity.org;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

public class OrgListActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_list);
        setCommonTitleBar("参展院所", R.id.webView, null, true);
    }
}
