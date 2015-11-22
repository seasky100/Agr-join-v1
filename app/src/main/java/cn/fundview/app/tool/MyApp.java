package cn.fundview.app.tool;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lict on 2015/10/19.
 */
public class MyApp extends Application {

    private static final String TAG = "JPush";

    @Override
    public void onCreate() {
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();
//
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        JPushInterface.setLatestNotificationNumber(this, 1);
    }
}
