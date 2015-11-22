package cn.fundview.app.view.company;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.company.CompanyListAction;
import cn.fundview.app.activity.company.DetailActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

public class CompanyListView extends ABaseWebView {

    private int page = 1;


    public CompanyListView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/company/list.html");
    }

    @Override
    public void init() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.COMPANY_ITEM_HEIGHT + 1;
        CompanyListAction action = new CompanyListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }

    /**
     * 加载下一页
     **/
    @JavascriptInterface
    public void nextPage() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.COMPANY_ITEM_HEIGHT + 1;
        CompanyListAction action = new CompanyListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }

    /**
     * 打开企业详细页面
     **/
    @JavascriptInterface
    public void openCompDetail(int compId, String compName, String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("compName", compName);
        intent.putExtra("compId", compId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    @Override
    public void onClickRight() {

        Intent intent = new Intent(context, SearchHistoryActivity.class);
        intent.putExtra("type",  SearchHistory.COMPANY_HISTORY);
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
