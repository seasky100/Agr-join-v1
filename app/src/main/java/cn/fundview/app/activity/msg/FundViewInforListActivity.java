package cn.fundview.app.activity.msg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.fundview.R;
import cn.fundview.app.action.msg.InitFundviewInforListAction;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.DateTimeUtil;
import cn.fundview.app.tool.adapter.RecyclerViewAdapter;
import cn.fundview.app.view.AsyncTaskCompleteListener;
import cn.fundview.app.view.msg.FundViewInforListView;
import cn.fundview.app.view.msg.RefreshFundViewInforListView;

public class FundViewInforListActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

    private FundViewInforListView fundViewInforListView;
    private RefreshFundViewInforListView mPullWebView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ImageButton mBackImageButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_findview_infor_list);

        initViews();
        attachEvents();
        initData();

//        mPullWebView = (RefreshFundViewInforListView) findViewById(R.id.pull_webview);//new PullToRefreshWebView(this);
//        mPullWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<FundViewInforListView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<FundViewInforListView> refreshView) {
//
//                fundViewInforListView.loadHistoryData();
//                mPullWebView.onPullDownRefreshComplete();
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<FundViewInforListView> refreshView) {
//            }
//        });
////
//        fundViewInforListView = mPullWebView.getRefreshableView();
//
//        setLastUpdateTime();
    }

    @Override
    public void complete(int requestCode, int responseCode, Object msg) {

        this.mRecyclerView.setAdapter(new RecyclerViewAdapter<FundviewInfor>((List<FundviewInfor>)msg,this,R.layout.fundview_info_item_layout) {
            @Override
            public void bindViewHolder(MyViewHolder viewHolder, FundviewInfor item) {

                viewHolder.setText(R.id.receive_time, DateTimeUtil.getRelativeTimeSinceNow(item.getUpdateDate()));
                viewHolder.setText(R.id.title, item.getTitle());
                viewHolder.setText(R.id.publish_time, DateTimeUtil.formatTime(item.getUpdateDate()));
                viewHolder.setImageByUrl(R.id.logo, item.getLogo(),R.mipmap.zx_moren,R.mipmap.zx_moren);
                viewHolder.setText(R.id.summary, item.getIntroduction());
            }

        });
    }

    /**
     * 初始化view
     */
    private void initViews() {

        this.mToolbar = (Toolbar) this.findViewById(R.id.toolBar);
        this.mBackImageButton = (ImageButton) (this.mToolbar.findViewById(R.id.backImageBtn));
        this.mRecyclerView = (RecyclerView) this.findViewById(R.id.recycleView);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }

    private void initData() {

        new InitFundviewInforListAction(this,this);
    }


    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullWebView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }
}
