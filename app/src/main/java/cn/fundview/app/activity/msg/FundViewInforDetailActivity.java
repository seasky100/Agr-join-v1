package cn.fundview.app.activity.msg;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

public class FundViewInforDetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findview_infor_detail);
        String title = getIntent().getStringExtra("title");
        this.setCommonTitleBar(title, R.id.webview, null, false);
    }

}
