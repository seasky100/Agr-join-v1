package cn.fundview.app.activity.FundProject;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 融资项目详细
 */
public class FundProjectDetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_project_detail_action);

        String title = getIntent().getStringExtra("name");

        this.setCommonTitleBar(title, R.id.webView, null, false);
    }
}
