package cn.fundview.app.activity.my;

import android.app.Activity;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 用户二维码预览
 */
public class ProfileQrcodeActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_qrcode);
        this.setCommonTitleBar("我的二维码", R.id.webView, null, false);
    }
}
