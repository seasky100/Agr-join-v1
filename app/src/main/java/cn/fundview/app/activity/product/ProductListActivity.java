package cn.fundview.app.activity.product;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 产品列表activity
 */
public class ProductListActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        //设置需求列表页的titleBar
        this.setCommonTitleBar("产品列表", R.id.webView, null, true);


    }

}
