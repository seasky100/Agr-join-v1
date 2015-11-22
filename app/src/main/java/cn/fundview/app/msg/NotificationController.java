package cn.fundview.app.msg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.fundview.R;
import cn.fundview.app.activity.MainActivity;

/**
 * notification 控制器 单例
 **/
public class NotificationController {

    private NotificationController() {
    }

    private static final NotificationController instance = new NotificationController();
    private NotificationManager notificationManager = null;
    private Notification notification = null;
    private RemoteViews contentView;
    private PendingIntent pendingIntent;
    private final static SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    public static NotificationController getInstance() {

        return instance;
    }

    //发送通知
    public void sendNotification(Context context, String msg) {

        if (notificationManager == null) {

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (notification == null) {

            notification = new Notification();
        }

        notification.icon = R.mipmap.logo_notification;
        notification.tickerText = msg;
        notification.when = System.currentTimeMillis();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_NO_CLEAR;// 点击后自动删除

        if (contentView == null) {

            contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        }

        //下拉后显示的内容
        contentView.setTextViewText(R.id.notification_msg, msg);
        contentView.setTextViewText(R.id.notification_title, "丰景资讯");
        Date date = new Date();
        contentView.setTextViewText(R.id.notification_time, format.format(date));
        notification.contentView = contentView;

        // 点击后跳转到的页面
        if (pendingIntent == null) {
            Intent intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        }
        notification.contentIntent = pendingIntent;
        //发送通知
        notificationManager.cancelAll();
        notificationManager.notify(465465, notification);
    }

    //取消通知
    public void closeNotification() {

        if (notificationManager != null) {

            notificationManager.cancelAll();
            notificationManager = null;
        }
    }

    public void setPendingIntent(PendingIntent pendingIntent) {

        this.pendingIntent = pendingIntent;
    }
}