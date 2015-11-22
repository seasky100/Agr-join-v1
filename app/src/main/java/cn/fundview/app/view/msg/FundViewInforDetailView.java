package cn.fundview.app.view.msg;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import cn.fundview.app.action.msg.FundviewInforDetailAction;
import cn.fundview.app.view.ABaseWebView;

public class FundViewInforDetailView extends ABaseWebView {

    public FundViewInforDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/msg/msg-infor-detail.html");
    }

    @Override
    public void init() {

        FundviewInforDetailAction action = new FundviewInforDetailAction(context, this);
        int id = ((Activity) context).getIntent().getIntExtra("id", 0);
        String updateDate = ((Activity) context).getIntent().getStringExtra("updateDate");
        action.execute(id, updateDate);
    }

}
