package cn.fundview.app.activity.company;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * CompanyProductDetailActivity
 * 企业产品详细 activity
 *
 * @author Lict
 * @version 1.0
 * @date 2015/10/23 0023
 */
public class CompanyProductDetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_product_detail);

        String title = getIntent().getStringExtra("productName");

        this.setCommonTitleBar(title, R.id.webView, null, false);
    }
}
