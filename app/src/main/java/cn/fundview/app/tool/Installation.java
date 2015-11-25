package cn.fundview.app.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 根据手机设备的各种硬件信息得到一个唯一码,作为用户的id
 */
public class Installation {

    public static final String DRIVER_ID_LABEL = "driverId";

    public static String getDriverId(Context context) {

        String szImei = "";
        try {
            // 电话IMEI
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            szImei = TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String szWLANMAC = "";
        try {
            // wifiMAC
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            szWLANMAC = wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String szAndroidID = "";
        try {
            // ANDROID_ID
            szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String m_szLongID = szImei + szAndroidID + szWLANMAC;
        // 计算 md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase(Locale.getDefault());

        return m_szUniqueID;
    }

    public static String getPhone(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceid = tm.getDeviceId();//获取智能设备唯一编号
//        String imei = tm.getSimSerialNumber();//获得SIM卡的序号
//        String imsi = tm.getSubscriberId();//得到用户Id
        String phone = tm.getLine1Number();//获取本机号码

        if(phone == null || phone.equals("phone")) {

            phone = "";
        }
        if(phone != null && phone.startsWith("+86")) {

            phone = phone.substring(3);
        }
        return phone;
    }

    // 获得版本文件code
    public static Integer getVersionCode(Context context) {

        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    "cn.fundview", 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

}
