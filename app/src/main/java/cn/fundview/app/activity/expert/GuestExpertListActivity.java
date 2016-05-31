package cn.fundview.app.activity.expert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.fundview.R;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshRecyclerView;

public class GuestExpertListActivity extends AppCompatActivity {

    @ViewInject(R.id.pullRecyclerView)
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private RecyclerView recyclerView;
    @ViewInject(R.id.toolBar)
    private Toolbar mToolbar;

    private ImageButton mBackImageButton;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_expert_list);

        ViewUtils.inject(this);

        initView();
        attachEvents();
        initData();
    }

    /**
     * 加载数据
     */
    private void initData() {
    }

    /**
     * 事件绑定
     */
    private void attachEvents() {
    }

    /**
     * 初始化view
     */
    private void initView() {
        this.mBackImageButton = (ImageButton) (this.mToolbar.findViewById(R.id.backImageBtn));
        this.mRecyclerView = this.mPullToRefreshRecyclerView.getRecyclerView();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(null);
        mRecyclerView.setHasFixedSize(true);
    }

}
