package cn.fundview.app.activity.my;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 客服热线
 */
public class HotActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        this.setCommonTitleBar("客服热线", R.id.webView, null, false);
    }

    ;
}
