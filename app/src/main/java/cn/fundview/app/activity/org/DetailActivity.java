package cn.fundview.app.activity.org;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 参展机构 详细 activity
 */
public class DetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_org_detail);

        String title = getIntent().getStringExtra("orgName");

        this.setCommonTitleBar(title, R.id.webView, null, false);
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

