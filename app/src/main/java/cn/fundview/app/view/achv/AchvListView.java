package cn.fundview.app.view.achv;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.achv.AchvListAction;
import cn.fundview.app.activity.achv.DetailActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * 成果列表
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
public class AchvListView extends ABaseWebView {

    private int page = 1;

    public AchvListView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/achv/list.html");
    }

    @Override
    public void init() {

        loadAchvs();
    }

    @Override
    public void active() {

        super.active();
    }

    @Override
    public void show() {

        super.show();
        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1;
        AchvListAction action = new AchvListAction(context, this);
        action.execute(page, pageSize, null);
    }

    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void nextPage() {

        loadAchvs();
    }


    /**
     * 打开专家详细页面
     **/
    @JavascriptInterface
    public void openAchvDetail(int achvId, String achvName,
                               String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("achvName", achvName);
        intent.putExtra("achvId", achvId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    private void loadAchvs() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1;
        AchvListAction action = new AchvListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }

    @Override
    public void onClickRight() {
        super.onClickRight();

        Intent intent = new Intent(context, SearchHistoryActivity.class);
        intent.putExtra("type", SearchHistory.ACHV_HISTORY);
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
