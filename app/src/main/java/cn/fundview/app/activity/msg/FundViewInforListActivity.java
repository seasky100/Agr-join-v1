package cn.fundview.app.activity.msg;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshBase;
import cn.fundview.app.view.msg.FundViewInforListView;
import cn.fundview.app.view.msg.RefreshFundViewInforListView;

public class FundViewInforListActivity extends ABaseActivity {

    private FundViewInforListView fundViewInforListView;
    private RefreshFundViewInforListView mPullWebView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_findview_infor_list);
        this.setCommonTitleBar("丰景 资讯", R.id.webviewId, null, false);
        mPullWebView = (RefreshFundViewInforListView) findViewById(R.id.pull_webview);//new PullToRefreshWebView(this);
        mPullWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<FundViewInforListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<FundViewInforListView> refreshView) {

                fundViewInforListView.loadHistoryData();
                mPullWebView.onPullDownRefreshComplete();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<FundViewInforListView> refreshView) {
            }
        });
//
        fundViewInforListView = mPullWebView.getRefreshableView();

        setLastUpdateTime();
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
