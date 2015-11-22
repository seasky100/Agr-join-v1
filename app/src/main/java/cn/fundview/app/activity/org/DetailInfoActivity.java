package cn.fundview.app.activity.org;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

public class DetailInfoActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_info);

        String compName = getIntent().getStringExtra("orgName");
        this.setCommonTitleBar(compName, R.id.webview, null, false);

    }
}
