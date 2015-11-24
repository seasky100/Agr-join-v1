package cn.fundview.app.view.fundProject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.expert.ExpertListAction;
import cn.fundview.app.action.fundProject.FundProjectListAction;
import cn.fundview.app.activity.FundProject.FundProjectDetailActivity;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * 项目名称：Agr-join-v1
 * 类描述： 融资项目列表view
 * 创建人：lict
 * 创建时间：2015/11/24 0024 上午 10:19
 * 修改人：lict
 * 修改时间：2015/11/24 0024 上午 10:19
 * 修改备注：
 */
public class ListView extends ABaseWebView {

    private int page = 1;
    public ListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/fundProject/list.html");
    }

    @Override
    protected void init() {

        loadData();
    }


    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void nextPage() {

        loadData();
    }

    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void openDetail(int id, String name) {

        Intent intent = new Intent(context, FundProjectDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    public void setTitle(String title) {

        searchTitleBarView.setCommonTitlebarRightBtn(title, null, false);
    }

    private void loadData() {

        int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.PROJECT_ITEM_HEIGHT + 1;
        FundProjectListAction action = new FundProjectListAction(context, this);
        action.execute(page, pageSize, null);
        this.page = page + 1;
    }
}
