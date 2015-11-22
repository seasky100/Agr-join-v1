package cn.fundview.app.view.requ;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.requ.RequListAction;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.activity.requ.DetailActivity;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * 需求列表
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：AchvListActivity
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月12日 下午1:46:00
 * 修改人：lict
 * 修改时间：2015年6月12日 下午1:46:00
 * 修改备注：
 */
public class RequListView extends ABaseWebView {

    private int page = 1;

    public RequListView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/requ/list.html");
    }

    @Override
    public void init() {

        loadRequs();
    }

    @Override
    public void active() {

        super.active();
    }

    @Override
    public void show() {

        super.show();
        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1;
        RequListAction action = new RequListAction(context, this);
        action.execute(page, pageSize, null);
    }

    /**
     * 加载更多需求
     **/
    @JavascriptInterface
    public void nextPage() {

        loadRequs();
    }


    /**
     * 打开专家详细页面
     **/
    @JavascriptInterface
    public void openRequDetail(int requId, String requName, String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("requName", requName);
        intent.putExtra("requId", requId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    private void loadRequs() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1;
        RequListAction action = new RequListAction(context, this);
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
        intent.putExtra("type", SearchHistory.REQU_HISTORY);
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
