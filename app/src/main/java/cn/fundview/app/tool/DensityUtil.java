package cn.fundview.app.tool;

import android.content.Context;

/**
 * 手机密度适配工具
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：DensityUtil
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月4日 下午5:44:31
 * 修改人：lict
 * 修改时间：2015年6月4日 下午5:44:31
 * 修改备注：
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}  