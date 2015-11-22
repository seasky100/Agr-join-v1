package cn.fundview.app.activity.qrcode;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.company.DetailView;

/**
 * 扫描二维码 后的企业详细
 */
public class CompanyDetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_detail);

        this.setCommonTitleBar("", R.id.webView, null, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            //登录完成后,刷新页面,需要从新加载json文件
            ((DetailView) this.webView).reloadPage();
        }
    }
}

