package cn.fundview.app.activity.msg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import cn.fundview.R;
import cn.fundview.app.action.msg.FundviewInforHistoryAction;
import cn.fundview.app.action.msg.InitFundviewInforListAction;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.DateTimeUtil;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.tool.adapter.RecyclerViewAdapter;
import cn.fundview.app.view.AsyncTaskCompleteListener;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshBase;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshRecyclerView;

public class FundViewInforListActivity extends AppCompatActivity implements AsyncTaskCompleteListener,RecyclerViewAdapter.MyRecyclerViewItemOnClickListener {

    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ImageButton mBackImageButton;

    private int id;//当前页面中存储的最小id,用于查看历史
    private int size;//每次查询的条数
    private List<FundviewInfor> dataSource;//recyclerview 数据源

    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_findview_infor_list);

        int id = getIntent().getIntExtra("id", 0);//设置未读的最大的id
        size = 1;//标示在资讯列表页中显示的资讯条数
        initViews();
        attachEvents();
        initData(id,size);
    }

    /**
     * 读取数据库 或 请求网络 完成处理
     * @param requestCode  请求码
     * @param responseCode 响应码 0 第一次请求 1后续的加载历史数据
     * @param msg          返回的消息,可以携带异步任务额执行结果
     */
    @Override
    public void complete(int requestCode, int responseCode, Object msg) {

        if(null != msg) {

            List<FundviewInfor> fundviewInforList = (List<FundviewInfor>)msg;
            if(null != fundviewInforList && fundviewInforList.size() > 0) {

                id = fundviewInforList.get(0).getId() - 1;
            }

            if(responseCode == 0) {

                dataSource = fundviewInforList;

                RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter<FundviewInfor>(dataSource, this, R.layout.fundview_info_item_layout) {
                    @Override
                    public void bindViewHolder(MyViewHolder viewHolder, FundviewInfor item) {

                        viewHolder.setText(R.id.receive_time, DateTimeUtil.getRelativeTimeSinceNow(item.getUpdateDate()));
                        viewHolder.setText(R.id.title, item.getTitle());
                        viewHolder.setText(R.id.publish_time, DateTimeUtil.formatTime(item.getUpdateDate()));
                        viewHolder.setImageByUrl(R.id.logo, item.getLogo(), R.mipmap.zx_moren, R.mipmap.zx_moren);
                        viewHolder.setText(R.id.summary, item.getIntroduction());
                    }

                };
                viewAdapter.setClickListener(this);
                this.mRecyclerView.setAdapter(viewAdapter);
            }else if(responseCode == 1) {

                if(fundviewInforList != null && fundviewInforList.size() > 0) {

                    id = fundviewInforList.get(0).getId()-1;
                    ((RecyclerViewAdapter)this.mRecyclerView.getAdapter()).insertData(fundviewInforList);
                    List<FundviewInfor> tempDatas = new ArrayList<>();

                    tempDatas.addAll(fundviewInforList);
                    tempDatas.addAll(dataSource);
                    dataSource = tempDatas;

                }else {

                    ToastUtils.show(this,"已是最后一条数据");
                }

                mPullToRefreshRecyclerView.onPullDownRefreshComplete();

            }
        }else {

            ToastUtils.show(this,"已是最后一条数据");
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

    }

    /**
     * 初始化view
     */
    private void initViews() {

        this.mToolbar = (Toolbar) this.findViewById(R.id.toolBar);
        this.mBackImageButton = (ImageButton) (this.mToolbar.findViewById(R.id.backImageBtn));
        this.mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.pullRecyclerView);//new PullToRefreshWebView(this);
        this.mRecyclerView = this.mPullToRefreshRecyclerView.getRecyclerView();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 绑定事件
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

                loadHistoryData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
    }

    /**
     * 进入该 activity 后 第一次加载数据
     * @param id 未读的最小的id
     * @param size 查询的条数
     */
    private void initData(int id,int size) {

        new InitFundviewInforListAction(this,id,size,this);
    }

    private void loadHistoryData() {

        new FundviewInforHistoryAction(this,id,size, this);
    }

    @Override
    public void onClick(View view, int position) {

        Intent intent = new Intent(this, FundViewInforDetailActivity.class);
        intent.putExtra("title",dataSource.get(position).getTitle());
        intent.putExtra("id",dataSource.get(position).getId());
        intent.putExtra("updateDate",dataSource.get(position).getUpdateDate()+"");
        startActivity(intent);
    }

}
