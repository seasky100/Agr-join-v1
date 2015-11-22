package cn.fundview.app.view.expert;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.expert.ExpertListAction;
import cn.fundview.app.activity.expert.DetailActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * 专家列表
 */
public class ExpertListView extends ABaseWebView {

    private int page = 1;
    private boolean isHavingData = false;

    public ExpertListView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/expert/list.html");
    }

    @Override
    public void init() {

        loadExperts();
    }

    @Override
    public void active() {

        super.active();
    }

    @Override
    public void show() {

        super.show();
        System.out.println("有网络了,正在加载数据...");
        ExpertListAction action = new ExpertListAction(context, this);
        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.EXPERT_ITEM_HEIGHT + 1;
        action.execute(page, pageSize, null);
    }

    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void nextPage() {

        loadExperts();
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
     * 打开专家详细页面
     **/
    @JavascriptInterface
    public void openExpertDetail(int expertId, String expertName, String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("expertName", expertName);
        intent.putExtra("expertId", expertId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    private void loadExperts() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.EXPERT_ITEM_HEIGHT + 1;
        ExpertListAction action = new ExpertListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }

    @Override
    public void onClickRight() {
        super.onClickRight();

        Intent intent = new Intent(context, SearchHistoryActivity.class);
        intent.putExtra("type", SearchHistory.EXPERT_HISTORY);
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
