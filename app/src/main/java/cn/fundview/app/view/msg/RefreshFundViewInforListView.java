package cn.fundview.app.view.msg;

import android.content.Context;
import android.util.AttributeSet;

import cn.fundview.R;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshAbaseWebview;


public class RefreshFundViewInforListView extends PullToRefreshAbaseWebview<FundViewInforListView> {

    /**
     * 构造方法
     *
     * @param context context
     */
    public RefreshFundViewInforListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public RefreshFundViewInforListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public RefreshFundViewInforListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected FundViewInforListView createRefreshableView(Context context, AttributeSet attrs) {
        FundViewInforListView webView = new FundViewInforListView(context, attrs);
        webView.setId(R.id.webviewId);
        return webView;
    }


    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }


    @Override
    protected boolean isReadyForPullUp() {
        double exactContentHeight = Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }
}
