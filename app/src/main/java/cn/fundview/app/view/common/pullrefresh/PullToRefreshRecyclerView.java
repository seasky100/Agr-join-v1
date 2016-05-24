package cn.fundview.app.view.common.pullrefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;


/**
 * Created by lict on 2016/5/15.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;

    /**
     * 滑动到顶部刷新的header
     */
    private LoadingLayout mRefreshHeaderLayout;
    /**
     * 滚动的监听器
     */
    private RecyclerView.OnScrollListener mScrollListener;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPullLoadEnabled(false);
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRefreshableView = recyclerView;
        mRefreshableView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isScrollLoadEnabled() && hasMoreData()) {
                    if (isReadyForPullUp()) {
                        startLoading();
                    }
                }

                if (null != mScrollListener) {
                    mScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }
        });

        return mRefreshableView;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }


    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            if (null == mLoadMoreFooterLayout.getParent()) {
                ((ViewGroup) mRefreshableView.getParent().getParent()).addView(mLoadMoreFooterLayout, -1);
            }
            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new RotateLoadingLayout(context);
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        return !((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == ILoadingLayout.State.NO_MORE_DATA));

    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }

        int mostTop = (mRefreshableView.getChildCount() > 0) ? mRefreshableView.getChildAt(0).getTop() : 0;
        return mostTop >= 0;

    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }

        final int lastItemPosition = adapter.getItemCount() - 1;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRefreshableView.getLayoutManager();
        final int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

        return lastVisiblePosition == lastItemPosition;
    }

    public RecyclerView getRecyclerView() {
        return mRefreshableView;
    }

    @Override
    public void onPullDownRefreshComplete() {
        super.onPullDownRefreshComplete();
        System.out.println("onPullDownRefreshComplete");
        if (null != getHeaderLoadingLayout()) {
            getHeaderLoadingLayout().setState(ILoadingLayout.State.RESET);
        }
    }

    @Override
    protected void startRefreshing() {
        super.startRefreshing();
        System.out.println("startRefreshing");

        if (null != getHeaderLoadingLayout()) {
            getHeaderLoadingLayout().setState(ILoadingLayout.State.REFRESHING);
        }

        try {
            if (null != getHeaderLoadingLayout()) {
                getHeaderLoadingLayout().setState(ILoadingLayout.State.RELEASE_TO_REFRESH);
            }
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
