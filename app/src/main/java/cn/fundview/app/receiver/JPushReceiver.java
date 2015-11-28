package cn.fundview.app.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.fundview.app.action.msg.LoadFundviewInforAction;
import cn.fundview.app.activity.expert.ExpertListActivity;
import cn.fundview.app.activity.msg.FundViewInforListActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.FundviewInforDao;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.msg.NotificationController;
import cn.fundview.app.msg.observer.NewFundviewInforObserver;
import cn.fundview.app.msg.observer.NewFundviewInforObserverMrg;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPushFundview";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            Intent i = new Intent(context, ExpertListActivity.class);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {

        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        JSONObject extraJson = null;

        if (extras != null && extras.trim() != "") {

            extraJson = JSON.parseObject(extras);

            //构造通知
            NotificationController notificationController = NotificationController.getInstance();
            Intent intent = new Intent(context, FundViewInforListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.FUNDVIEW_INFOR_KEY, extraJson.getIntValue(Constants.FUNDVIEW_INFOR_KEY));
            intent.putExtra("size", 2);//标示在资讯列表页中展示的条数
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationController.setPendingIntent(pendingIntent);

            //存储咨询
            FundviewInforDao fundviewInforDao = DaoFactory.getInstance(context).getFundviewInforDao();
            if (fundviewInforDao.getById(Integer.parseInt(extraJson.getString(Constants.FUNDVIEW_INFOR_KEY))) == null) {

                FundviewInfor fundviewInfor = new FundviewInfor();
                fundviewInfor.setId(extraJson.getIntValue(Constants.FUNDVIEW_INFOR_KEY));//咨询的id
                fundviewInfor.setRead(0);
                fundviewInfor.setTitle(message);//使用推送过来的消息作为标题
                fundviewInfor.setPublishDate(new Date().getTime());
                fundviewInforDao.save(fundviewInfor);

                //新资讯的发送通知
                notificationController.sendNotification(context, message);

                //只用新的通知才被监听
                List<NewFundviewInforObserver> observers = NewFundviewInforObserverMrg.getInstance().getObservers();
                if (observers != null && observers.size() > 0) {

                    for (NewFundviewInforObserver observer : observers) {

                        observer.onReceive(fundviewInforDao.getById(extraJson.getIntValue(Constants.FUNDVIEW_INFOR_KEY)));
                    }
                }

            } else {

                //更新操作  更新的资讯是不发送通知,也不通知观察者的
                FundviewInfor fundviewInfor = fundviewInforDao.getById(extraJson.getIntValue(Constants.FUNDVIEW_INFOR_KEY));
                fundviewInfor.setTitle(message);
                fundviewInforDao.update(fundviewInfor);
            }

            //收到资讯后,不管是新资讯还是旧得资讯,均要从服务器拉取数据
            LoadFundviewInforAction action = new LoadFundviewInforAction(context);
            action.execute(extraJson.getIntValue(Constants.FUNDVIEW_INFOR_KEY));
        }
    }
}
