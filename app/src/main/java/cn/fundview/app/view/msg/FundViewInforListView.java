package cn.fundview.app.view.msg;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.msg.FundviewInforHistoryAction;
import cn.fundview.app.activity.msg.FundViewInforDetailActivity;
import cn.fundview.app.view.ABaseWebView;

public class FundViewInforListView extends ABaseWebView {

    private int id;//当前页面中存储的最小id,用于查看历史

    public FundViewInforListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/msg/fundview-infor-list.html");
    }

    @Override
    public void init() {

//        int id = ((Activity) context).getIntent().getIntExtra("id", 0);//设置未读的最大的id
//        int size = ((Activity) context).getIntent().getIntExtra("size", 0);//标示在资讯列表页中显示的资讯条数
//        InitFundviewInforListAction action = new InitFundviewInforListAction(context, this);
//        action.execute(id, size);
    }

    /**
     * 加载历史数据
     */
    public void loadHistoryData() {

        FundviewInforHistoryAction action = new FundviewInforHistoryAction(context, this);
        action.execute(id, 2);
    }

    /**
     * 打開咨訊詳細
     *
     * @param id
     * @param name
     */
    @JavascriptInterface
    public void openDetail(int id, String name, String updateDate) {

        Intent intent = new Intent(context, FundViewInforDetailActivity.class);
        intent.putExtra("title", name);
        intent.putExtra("id", id);
        intent.putExtra("updateDate", updateDate);
        context.startActivity(intent);
    }


    /**
     * 设置页面中显示的资讯的最小id
     *
     * @param id
     */
    @JavascriptInterface
    public void setSmallestId(int id) {

        this.id = id;
    }
}
