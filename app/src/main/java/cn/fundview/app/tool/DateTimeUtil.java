package cn.fundview.app.tool;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    /**
     * 格式化数据为 多久之前
     *
     * @param date
     * @return
     */
    public static String format(Date date) {

        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    public static String getRelativeTimeSinceNow(long time) {

        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int isAm = calendar.get(Calendar.AM_PM);
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        Calendar nowCalendar = Calendar.getInstance();
        int dayDelta = nowCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
        int yearDelta = nowCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (yearDelta == 0 && dayDelta == 0) {

            //同一天
            result = isAm == Calendar.AM ? "上午 " : "下午 " + (hour > 9 ? hour : ("0" + hour)) + ":" + (minutes > 9 ? minutes : ("0" + minutes));
        } else if (yearDelta == 0 && dayDelta == 1) {

            result = "昨天 " + (isAm == Calendar.AM ? "上午 " : "下午 ") + (hour > 9 ? hour : ("0" + hour)) + ":" + (minutes > 9 ? minutes : ("0" + minutes));
        } else {

            String week = "";
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {

                case Calendar.SUNDAY:
                    week = "周日";
                    break;
                case Calendar.MONDAY:
                    week = "周一";
                    break;
                case Calendar.TUESDAY:
                    week = "周二";
                    break;
                case Calendar.WEDNESDAY:
                    week = "周三";
                    break;
                case Calendar.THURSDAY:
                    week = "周四";
                    break;
                case Calendar.FRIDAY:
                    week = "周五";
                    break;
                case Calendar.SATURDAY:
                    week = "周六";
                    break;
            }
            result = week + (isAm == Calendar.AM ? " 上午 " : " 下午 ") + (hour > 9 ? hour : ("0" + hour)) + ":" + (minutes > 9 ? minutes : ("0" + minutes));
        }

        return result;
    }

    /**
     * 毫秒转秒
     *
     * @param date
     * @return
     */
    private static long toSeconds(long date) {
        return date / 1000L;
    }

    /**
     * 毫秒 转 分钟
     *
     * @param date
     * @return
     */
    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    /**
     * 毫秒 转 小时
     *
     * @param date
     * @return
     */
    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    /**
     * 毫秒 转天
     *
     * @param date
     * @return
     */
    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    /**
     * 毫秒 转 月
     *
     * @param date
     * @return
     */
    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    /**
     * 毫秒 转 年
     *
     * @param date
     * @return
     */
    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static String formatTime(long time) {
        String result = "";
        try {
            Date d = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
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
