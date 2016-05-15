package cn.fundview.app.activity.my;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 关于我们activity
 **/
public class AboutActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.setCommonTitleBar("关于丰景", R.id.aboutWebview, null, false);
    }

}
