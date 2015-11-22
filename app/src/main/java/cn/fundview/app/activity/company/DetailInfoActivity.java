package cn.fundview.app.activity.company;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

public class DetailInfoActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        String compName = getIntent().getStringExtra("compName");
        this.setCommonTitleBar(compName, R.id.webview, null, false);

    }
}
