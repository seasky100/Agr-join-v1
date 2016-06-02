package cn.fundview.app.activity.expert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.fundview.R;
import cn.fundview.app.action.expert.GuestExpertListAction;
import cn.fundview.app.action.msg.InitFundviewInforListAction;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.DateTimeUtil;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.tool.adapter.RecyclerViewAdapter;
import cn.fundview.app.view.AsyncTaskCompleteListener;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshBase;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshRecyclerView;

public class GuestExpertListActivity extends AppCompatActivity implements AsyncTaskCompleteListener, RecyclerViewAdapter.MyRecyclerViewItemOnClickListener {

    @ViewInject(R.id.pullRecyclerView)
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    @ViewInject(R.id.toolBar)
    private Toolbar mToolbar;

    private ImageButton mBackImageButton;
    private RecyclerView mRecyclerView;

    private List<Expert> dataSource;
    private int currentPage = 1;
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

        new GuestExpertListAction(this, currentPage++, 10, null, this);
    }

    /**
     * 事件绑定
     */
    private void attachEvents() {

        this.mBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.mPullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

                new GuestExpertListAction(GuestExpertListActivity.this, currentPage++, 10, null, GuestExpertListActivity.this);
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        this.mBackImageButton = (ImageButton) (this.mToolbar.findViewById(R.id.backImageBtn));
        this.mRecyclerView = this.mPullToRefreshRecyclerView.getRecyclerView();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        this.mRecyclerView.addItemDecoration(null);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 数据加载完成
     *
     * @param requestCode  请求码
     * @param responseCode 响应码 currentPage  根据当前页数 判断是首次加载数据还是 通知数据源更新recyclerView
     * @param msg          返回的消息,可以携带异步任务额执行结果
     */
    @Override
    public void complete(int requestCode, int responseCode, Object msg) {

        List<Expert> result = null;
        if (msg != null) {

            result = (List<Expert>) msg;
        } else {

            ToastUtils.show(this, "没有更多数据");
        }
        if (result != null && result.size() > 0) {

            if (dataSource == null) {

                dataSource = new ArrayList<>();
            }
            dataSource.addAll(result);

            if (responseCode == 1) {

                RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter<Expert>(dataSource, this, R.layout.guest_expert_item) {
                    @Override
                    public void bindViewHolder(MyViewHolder viewHolder, Expert item) {

//                        viewHolder.setText(R.id.receive_time, DateTimeUtil.getRelativeTimeSinceNow(item.getUpdateDate()));
//                        viewHolder.setText(R.id.title, item.getTitle());
//                        viewHolder.setText(R.id.publish_time, DateTimeUtil.formatTime(item.getUpdateDate()));
//                        viewHolder.setImageByUrl(R.id.logo, item.getLogo(), R.mipmap.zx_moren, R.mipmap.zx_moren);
//                        viewHolder.setText(R.id.summary, item.getIntroduction());
                    }

                };
                viewAdapter.setClickListener(this);
                this.mRecyclerView.setAdapter(viewAdapter);
            } else {

                ((RecyclerViewAdapter) this.mRecyclerView.getAdapter()).insertData(result);
                List<FundviewInfor> tempDatas = new ArrayList<>();
                dataSource.addAll(result);
            }

            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        } else {

            ToastUtils.show(this, "没有更多数据");
        }
    }

    @Override
    public void onClick(View view, int position) {

    }
}
