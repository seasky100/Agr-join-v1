package cn.fundview.app.view.company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.company.DetailAction;
import cn.fundview.app.action.company.ProductDetailAction;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.action.my.AttentUserAction;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 企业产品 详细view
 */
public class ProductDetailView extends ABaseWebView {

    private Integer productId;// 企业产品id

    public ProductDetailView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/company/product-detail.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        productId = intent.getIntExtra("productId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间

        ProductDetailAction action = new ProductDetailAction(context, this);
        action.execute(productId, lastModify);

    }

}
