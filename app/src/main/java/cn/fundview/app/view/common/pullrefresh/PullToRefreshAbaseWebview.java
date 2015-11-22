package cn.fundview.app.view.common.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import cn.fundview.app.view.ABaseWebView;

/**
 * 支持下拉刷新的webview
 *
 * @author Li Hong
 * @since 2013-8-22
 */
public abstract class PullToRefreshAbaseWebview<T extends ABaseWebView> extends PullToRefreshBase<T> {
    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshAbaseWebview(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshAbaseWebview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshAbaseWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
