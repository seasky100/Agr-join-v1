package cn.fundview.app.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 */
public class ToastUtils {

    private ToastUtils() {
        throw new AssertionError();
    }

    /**
     * 显示指定的资源,使用默认的持续时间
     *
     * @param context
     * @param resId   void
     */
    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    /**
     * 显示指定的资源,并指定显示时间
     *
     * @param context
     * @param resId
     * @param duration void
     */
    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    /**
     * 显示指定的text
     *
     * @param context
     * @param text    void
     */
    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示指定的text 并指定显示时间
     *
     * @param context
     * @param text
     * @param duration void
     */
    public static void show(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    /**
     * 显示指定的args ,使用指定的资源进行格式化
     *
     * @param context
     * @param resId
     * @param args    void
     */
    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    /**
     * 显示args, 使用format 进行格式化
     *
     * @param context
     * @param format
     * @param args    void
     */
    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    /**
     * 显示格式化的args ,同时指定显示时间
     *
     * @param context
     * @param resId
     * @param duration
     * @param args     void
     */
    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    /**
     * 显示格式化的args ,同时指定显示时间
     *
     * @param context
     * @param resId
     * @param duration
     * @param args     void
     */
    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
