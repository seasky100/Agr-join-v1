package cn.fundview.app.view.common.pullrefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * Created by lict on 2016/5/15.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

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
        return mRefreshableView;
    }


    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected boolean isReadyForPullUp() {
        return false;
    }


    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        RotateLoadingLayout rotateLoadingLayout = new RotateLoadingLayout(context);

        rotateLoadingLayout.setHintTextNormal("下拉可加载历史");
        rotateLoadingLayout.setHintTextRelease("松开可加载历史");
        return rotateLoadingLayout;
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


    public RecyclerView getRecyclerView() {
        return mRefreshableView;
    }

}
