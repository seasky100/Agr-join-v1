package cn.fundview.app.view.product;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.product.ProductListAction;
import cn.fundview.app.action.requ.RequListAction;
import cn.fundview.app.activity.company.CompanyProductDetailActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.activity.requ.DetailActivity;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * 产品列表
 * <p/>
 * 类名称：ProductListActivity
 * 类描述：
 * 创建人：lict
 * 修改人：lict
 * 修改备注：
 */
public class ProductListView extends ABaseWebView {

    private int page = 1;

    public ProductListView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/product/list.html");
    }

    @Override
    public void init() {

        loadProduct();
    }

    @Override
    public void active() {

        super.active();
    }

    @Override
    public void show() {

        super.show();
        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.PRODUCT_ITEM_HEIGHT + 1;
        ProductListAction action = new ProductListAction(context, this);
        action.execute(page, pageSize, null);
    }

    /**
     * 加载更多需求
     **/
    @JavascriptInterface
    public void nextPage() {

        loadProduct();
    }


    /**
     * 打开产品详细
     * @param productId     产品id
     * @param productName   产品名称
     * @param lastModify    更新时间
     */
    @JavascriptInterface
    public void openProductDetail(int productId, String productName, String lastModify) {

        Intent intent = new Intent(context, CompanyProductDetailActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("productId", productId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }


    private void loadProduct() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.PRODUCT_ITEM_HEIGHT + 1;
        ProductListAction action = new ProductListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }

    @Override
    public void onClickMiddle() {
        //需求列表页中的titleBar 中间是title
    }

    @Override
    public void onClickRight() {

        //需求列表页中的titleBar 右边是搜索按钮,跳转到需求搜索历史也
        Intent intent = new Intent(context, SearchHistoryActivity.class);
        intent.putExtra("type", SearchHistory.PRODUCT_HISTORY);
        context.startActivity(intent);
    }

    /**
     * 重新设置列表页标题
     * @param title
     */
    public void setTitle(String title) {

        searchTitleBarView.setCommonTitlebarRightBtn(title, null, true);
    }
}
