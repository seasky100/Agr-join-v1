package cn.fundview.app.view.my;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.LoadMyIconAction;
import cn.fundview.app.activity.my.ProfileIconActivity;
import cn.fundview.app.activity.my.TakeHeadIconActivity;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.view.ABaseWebView;

/**
 * 个人头像
 */
public class ProfileIconView extends ABaseWebView {

    public ProfileIconView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/my/profile-icon.html");
    }

    @Override
    public void init() {

        setMyIcon();
    }

    /**
     * 打开照相机
     **/
    @JavascriptInterface
    public void takePhoto() {

        openTakePicActivity(1);
    }

    /**
     * 打开本地相册
     **/
    @JavascriptInterface
    public void takeLocalPic() {

        openTakePicActivity(2);
    }

    /**
     * 关闭
     **/
    @JavascriptInterface
    public void closePage() {

        ((Activity) context).finish();
    }

    /**
     * 拍完照片回来
     **/
    public void onRestart(String msg) {

        setMyIcon();
        if (null != msg && "saveSuccess".equals(msg))
            this.loadUrl("javascript:Page.changePageStatus(1)");
    }

    /**
     * 设置我的头像
     **/
    private void setMyIcon() {

        // 获取数据库中我的头像的信息
        LoadMyIconAction action = new LoadMyIconAction(context, this);
        action.execute("Page.setHeadIcon");
    }

    /**
     * @param source 来源 1 相机  2相册
     */
    private void openTakePicActivity(int source) {

        int uid = ((Activity) context).getIntent().getIntExtra("uid", 0);
        String drivePath = DeviceConfig.getSysPath(context);
        Bundle bundle = new Bundle();
        bundle.putInt("source", source);
        bundle.putString("fileSavePath", drivePath + Constants.MY_ICON_SAVE_DIR + uid + "/");
        bundle.putString("fileName", new Date().getTime() + ".jpg");
        Intent intent = new Intent(context, TakeHeadIconActivity.class);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ProfileIconActivity.OPEN_TAKE_PIC);
    }


    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }
}
