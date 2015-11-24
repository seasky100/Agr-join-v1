package cn.fundview.app.activity.FundProject;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 融资项目列表
 */
public class FundProjectListActivity extends ABaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_project_list);
        this.setCommonTitleBar("项目列表", R.id.webView, null, false);

    }
}
