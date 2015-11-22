package cn.fundview.app.activity.qrcode;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 二维码扫描后的科研机构详细
 */
public class KyorgDetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_org_detail);

        this.setCommonTitleBar("", R.id.webView, null, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (CallAction.CallRequestCode == requestCode) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                String phoneNum = getIntent().getStringExtra("tel");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.replaceAll("-", "")));

//				startActivity(intent);
            }
        }
    }
}

