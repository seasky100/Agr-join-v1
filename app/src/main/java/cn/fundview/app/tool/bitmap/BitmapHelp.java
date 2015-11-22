package cn.fundview.app.tool.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import cn.fundview.R;

import com.lidroid.xutils.BitmapUtils;

/**
 * Author: wyouflf
 * Date: 13-11-12
 * Time: 上午10:24
 */
public class BitmapHelp {
    private BitmapHelp() {
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.ic_launcher);
            bitmapUtils.configDefaultLoadFailedImage(R.mipmap.logo);
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

            bitmapUtils.configMemoryCacheEnabled(false);
            bitmapUtils.configDiskCacheEnabled(false);

            bitmapUtils.configDefaultAutoRotation(true);

//            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            scaleAnimation.setDuration(800);

            // AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
            // 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
//            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
//            alphaAnimation.setDuration(1000);
//            bitmapUtils.configDefaultImageLoadAnimation(animation);
        }
        return bitmapUtils;
    }
}
