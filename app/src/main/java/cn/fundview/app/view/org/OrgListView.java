package cn.fundview.app.view.org;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.org.OrgListAction;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.activity.org.DetailActivity;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * Created by Administrator on 2015/10/19 0019.
 * 参展机构列表view
 */
public class OrgListView extends ABaseWebView {

    private int page = 1;

    public OrgListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadUrl("file:///android_asset/page/organ/list.html");
    }

    @Override
    public void init() {

        loadOrgs();
    }

    @Override
    public void active() {

        super.active();
    }

    @Override
    public void show() {

        super.show();
        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ORG_ITEM_HEIGHT + 1;
        OrgListAction action = new OrgListAction(context, this);
        action.execute(page, pageSize, null);
    }

    /**
     * 加载更多参展机构
     **/
    @JavascriptInterface
    public void nextPage() {

        loadOrgs();
    }

    /**
     * 打开专家搜索页面
     **/
    @JavascriptInterface
    public void openSearchPage() {

//		Intent intent = new Intent(context, ExpertSearchActivity.class);
//		context.startActivity(intent);
        // ((Activity)context).finish();

    }

    /**
     * 打开机构详细页面
     **/
    @JavascriptInterface
    public void openOrgDetail(int orgId, String orgName, String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("orgName", orgName);
        intent.putExtra("orgId", orgId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    private void loadOrgs() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ORG_ITEM_HEIGHT + 1;
        OrgListAction action = new OrgListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }

    @Override
    public void onClickRight() {
        super.onClickRight();

        Intent intent = new Intent(context, SearchHistoryActivity.class);
        intent.putExtra("type", SearchHistory.ORG_HISTORY);
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
