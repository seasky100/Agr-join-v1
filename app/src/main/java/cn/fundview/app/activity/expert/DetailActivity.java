package cn.fundview.app.activity.expert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.expert.DetailView;

public class DetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expert_detail);

        String title = getIntent().getStringExtra("expertName");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            //登录完成后,刷新页面,需要从新加载json文件
            ((DetailView) this.webView).reloadPage();
        }
    }
}

