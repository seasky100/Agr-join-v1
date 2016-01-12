package cn.fundview.app.view.company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.achv.OwnerAction;
import cn.fundview.app.action.company.ProductDetailAction;
import cn.fundview.app.view.ABaseWebView;

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

    /**
     * 查找成果对应的专家
     *
     * @param ownerId   拥有者id
     * @param ownerType 拥有者类型 1企业  2 专家
     */
    @JavascriptInterface
    public void findOwner(int ownerId,  String ownerName) {

        OwnerAction action = new OwnerAction(context, this);
        action.execute(ownerId, 1, ownerName);
    }

}
