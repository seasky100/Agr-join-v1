package cn.fundview.app.view.msg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import java.util.Date;

import cn.fundview.app.action.msg.InitMsgListAction;
import cn.fundview.app.action.msg.MsgListAction;
import cn.fundview.app.activity.msg.FundViewInforListActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.FundviewInforDao;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.msg.NotificationController;
import cn.fundview.app.msg.observer.NewFundviewInforObserver;
import cn.fundview.app.msg.observer.NewFundviewInforObserverMrg;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

public class MsgListView extends ABaseWebView implements NewFundviewInforObserver {

    public MsgListView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadUrl("file:///android_asset/page/msg/msg-list.html");
    }

    @Override
    public void init() {

        initMsgList();
    }

    @Override
    public void active() {
        super.active();

        if (pageLoaded) {

            initMsgList();
        }
    }

    @Override
    public void show() {
        super.show();

        if (this.getUrl() == null)
            this.loadUrl("file:///android_asset/page/msg/msg-list.html");

        if (pageLoaded)
            initMsgList();

    }

    /**
     * 丰景资讯列表页   打开咨询列表页
     **/
    @JavascriptInterface
    public void openMsgInforList(final int id) {

        // 清空消息提醒
        NotificationController.getInstance().closeNotification();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(context, FundViewInforListActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("size", 0);//标示查看所有的未读的历史
                context.startActivity(intent);
            }
        });
    }


    private void loadMsgList() {

        MsgListAction msgListAction = new MsgListAction(context, this);
        msgListAction.execute();
    }

    private void initMsgList() {

        InitMsgListAction msgListAction = new InitMsgListAction(context, this);
        msgListAction.execute();
    }

    @Override
    public void onReceive(FundviewInfor item) {

        initMsgList();
    }
}
