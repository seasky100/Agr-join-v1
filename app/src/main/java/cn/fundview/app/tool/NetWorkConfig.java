package cn.fundview.app.tool;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 判断网络是否正常连接
 *
 * @author 欧达
 */
public class NetWorkConfig {

    public static boolean checkNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Network[] networks = manager.getAllNetworks();
            if (networks != null && networks.length > 0) {

                for (Network item : networks) {

                    NetworkInfo networkInfo = manager.getNetworkInfo(item);
                    if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {

                        return true;
                    }
                }
            }
            return false;
        } else {

            NetworkInfo mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean flag = false;
            if ((mWifi != null) && ((mWifi.isAvailable()) || (mMobile.isAvailable()))) {
                if ((mWifi.isConnected()) || (mMobile.isConnected())) {
                    flag = true;
                }
            }
            return flag;
        }
    }

}
