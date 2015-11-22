package cn.fundview.app.tool;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {

    public static String formatTime(String time) {
        String result = "";
        try {
            Date d = new Date(Long.valueOf(time));
            SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
            result = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String formatTime(long time, String format) {
        String result = "";
        try {
            Date d = new Date(Long.valueOf(time));
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("deprecation")
    public static String formatTime2(String time) {
        String result = "";
        try {
            Date d = new Date(Long.valueOf(time));
            String tm = "";
            if (d.getHours() > 12) {
                tm = "下午";
            } else {
                tm = "上午";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            result = sdf.format(d) + " " + tm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
