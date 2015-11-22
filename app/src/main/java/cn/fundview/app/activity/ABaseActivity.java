package cn.fundview.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;

import java.lang.reflect.Field;

import cn.fundview.R;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.OptionMenuListener;
import cn.fundview.app.view.SearchTitleBar;
import cn.jpush.android.api.JPushInterface;

public abstract class ABaseActivity extends Activity {

    protected ABaseWebView webView;
    protected boolean firstIncome = true;

    protected OptionMenuListener optionMenuListener;

    /**
     * 设置通用titleBar 的标题  viewId,和右边按钮的文字(为空的时候是搜索)
     *
     * @param title
     * @param webviewId
     * @param rightBtnText
     */
    protected void setCommonTitleBar(String title, int webviewId, String rightBtnText, boolean isSearch) {

        this.webView = (ABaseWebView) this.findViewById(webviewId);

        SearchTitleBar searchTitleBar = (SearchTitleBar) this.findViewById(R.id.titleBarView);

        searchTitleBar.setCommonTitlebarRightBtn(title, rightBtnText, isSearch);
        webView.setTitleBar(searchTitleBar);//绑定监听器
    }

    /**
     * 设置首页标题栏
     *
     * @param webviewId
     * @param title     为空的时候 表示没有标题栏
     */
    protected void setHomeTitleBar(int webviewId, String title) {

        this.webView = (ABaseWebView) this.findViewById(webviewId);
        SearchTitleBar searchTitleBar = (SearchTitleBar) this.findViewById(R.id.titleBarView);
        searchTitleBar.setHomeTitle(title);
        webView.setTitleBar(searchTitleBar);//绑定监听器
    }

    /**
     * 设置搜索页的标题栏
     *
     * @param webviewId
     */
    protected void setSearchPageTitleBar(int webviewId) {

        this.webView = (ABaseWebView) this.findViewById(webviewId);
        SearchTitleBar titleBar = (SearchTitleBar) this.findViewById(R.id.titleBarView);
        webView.setTitleBar(titleBar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 界面切换动画效果
        if (firstIncome) {
            firstIncome = false;

            animation_1();
        } else {

            animation_2();
        }

        // 页面处于活动状态
        if (null != webView && !firstIncome)
            webView.active();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 页面处于非活动状态
        if (null != webView)
            webView.inActive();
        JPushInterface.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (null != webView)
                webView.onKeyBack();
//                webView.removeAllViews();
//                webView.destroy();

            return true;
        } else {

            return super.onKeyDown(keyCode, event);
        }
    }

    protected void animation_1() {

        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    protected void animation_2() {

        this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {

            Field field = WebView.class.getDeclaredField("mWebViewCore");

            field = field.getType().getDeclaredField("mBrowserFrame");

            field = field.getType().getDeclaredField("sConfigCallback");

            field.setAccessible(true);

            Object configCallback = field.get(null);


            if (null == configCallback) {

                return;

            }


            field = field.getType().getDeclaredField("mWindowManager");

            field.setAccessible(true);

            field.set(configCallback, windowManager);

        } catch (Exception e) {

        }

    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

    }


    protected void onDestroy() {

//        setConfigCallback(null);
        super.onDestroy();
//        webView.removeAllViews();
//        webView.destroy();

    }

}
