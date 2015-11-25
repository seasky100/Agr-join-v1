package cn.fundview.app.view;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.fundview.app.action.global.FileExistAction;
import cn.fundview.app.action.global.FileLoadAction;
import cn.fundview.app.action.global.GetSysPathAction;
import cn.fundview.app.activity.my.LoginActivity;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.view.common.pullrefresh.IPullToRefresh;

/**
 * 浏览器组件
 *
 * @author ouda
 *         IPullToRefresh<T> 添加滑动加载事件
 */
@SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
public abstract class ABaseWebView extends WebView implements TitleBarListener {

    protected Context context;
    protected SearchTitleBar searchTitleBarView;
    protected boolean pageLoaded = false;// 用于判断页面是否已经加载过

    public ABaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        // 禁用缓存
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置基本属性，启用JS和禁用滚动条
        getSettings().setJavaScriptEnabled(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        getSettings().setDomStorageEnabled(true);

        try {

            // can read the json file on local
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = this.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);

                if (method != null)
                    method.invoke(this.getSettings(), true);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        // 禁止长按事件，防止长按页面上的文字，打开文字编辑菜单
        setOnLongClickListener(longClickListener);

        // 禁止多点触摸，防止同时点击多个按钮
        setOnTouchListener(touchListener);

        // 响应alert事件
        setWebChromeClient(webChromeClient);

        setWebViewClient(client);

        // 注册 js 调用的sdk对象
        addJavascriptInterface(this, "sdk");
        addJavascriptInterface(this, "file");
    }

    public void setTitleBar(SearchTitleBar titleBarView) {

        this.searchTitleBarView = titleBarView;
        titleBarView.registerListener(this);
    }

    public SearchTitleBar getTitleBarView() {

        return this.searchTitleBarView;
    }

    public void onKeyBack() {

//        this.removeAllViews();
//        this.destroy();
        clearCache(true);
        ((Activity) context).finish();
    }

    /************************
     * search title bar listener implemention start
     ***************************/
    @Override
    public void onClickLeftBtn() {
        // TODO Auto-generated method stub
//        this.removeAllViews();
//        this.destroy();
        ((Activity) context).finish();
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }

    /************************
     * search title bar listener implemention end
     ***************************/

    protected abstract void init();

    public void show() {

        this.setVisibility(View.VISIBLE);
    }

    public void hide() {

        this.setVisibility(View.GONE);
    }

    public void active() {

        if (this.pageLoaded) {
            this.loadUrl("javascript:Global.onPageActive();");
            //this.invalidate();
        }
    }

    public void inActive() {

        if (this.pageLoaded)
            this.loadUrl("javascript:Global.onPageInActive();");// 通知页面隐藏
    }

    private OnLongClickListener longClickListener = new OnLongClickListener() {

        // 禁止长按事件
        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    };

    private OnTouchListener touchListener = new OnTouchListener() {

        // 禁止多点触摸
        @SuppressLint("ClickableViewAccessibility")
        @SuppressWarnings("deprecation")
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            int action = arg1.getAction();

            switch (action) {

                case MotionEvent.ACTION_POINTER_2_DOWN:

                    return true;
                case MotionEvent.ACTION_POINTER_3_DOWN:

                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);

        new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    ABaseWebView.this.postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取系统路径
     **/
    @JavascriptInterface
    public String getSysPath() {

        GetSysPathAction sysPathAction = new GetSysPathAction(context, this);
        return sysPathAction.execute();
    }

    /**
     * 判断文件是否在本地存在
     **/
    @JavascriptInterface
    public boolean isFileExist(String localPath) {

        FileExistAction fileExistAction = new FileExistAction(context, this);
        return fileExistAction.execute(localPath);
    }

    /**
     * 文件下载
     **/
    @JavascriptInterface
    public void fileLoad(String downPath, String saveLocalPath, String fileName, String oldFileName) {

        FileLoadAction fileLoadAction = new FileLoadAction(context, this);
        fileLoadAction.execute(downPath, saveLocalPath, fileName, oldFileName);
    }

    /**
     * 客户端的错误提示
     **/
    @JavascriptInterface
    public void clientHint(String hint) {

        Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
    }

    /**
     * 用户退出
     **/
    @JavascriptInterface
    public void logout() {

        PreferencesUtils.putInt(context, Constants.ACCOUNT_ID, 0);//当前登录账户id
        PreferencesUtils.putInt(context, Constants.ACCOUNT_TYPE_KEY, 0);// 当前登录账户类型
        PreferencesUtils.putInt(context, Constants.LOGIN_STATUS_KEY, Constants.LOGIN_OUT_STATUS);// 当前登录账户登录状态
        PreferencesUtils.putString(context, Constants.ACCOUNT_KEY, null);// 当前登录账户账号
        PreferencesUtils.putString(context, Constants.PASSWORD_KEY, null);// 当前登录账户密码

        Intent intent = new Intent(context, LoginActivity.class);

        context.startActivity(intent);

        ((Activity) context).finish();

    }


    /**
     * 客户端的错误提示
     **/
    @JavascriptInterface
    public void closePage() {

        ((Activity) context).finish();
    }


    /**
     * 打开手机默认浏览器
     **/
    @JavascriptInterface
    public void openUrl(String url) {

        Intent intent = null;

        intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        context.startActivity(intent);
    }

    /**
     * 跳转到登录页面
     */
    @JavascriptInterface
    public void openLoginPage(int requestCode) {

        Intent intent = new Intent(context, LoginActivity.class);

        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {

        // 让页面响应alert事件
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            // TODO Auto-generated method stub
            ConsoleMessage.MessageLevel level = consoleMessage.messageLevel();
            switch (level) {

                case WARNING:
                    Log.w(Constants.TAG, "第" + consoleMessage.lineNumber() + "行出现警告," + consoleMessage.message());
                    break;
                case ERROR:
                    Log.e(Constants.TAG, "第" + consoleMessage.lineNumber() + "行出现错误," + consoleMessage.message());
                    break;
                default:
                    break;
            }
            return super.onConsoleMessage(consoleMessage);
        }

    };

    private WebViewClient client = new WebViewClient() {

        // 页面加载完成执行初始化操作
        @Override
        public void onPageFinished(WebView view, String url) {

            pageLoaded = true;
            init();
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            System.out.println("shouldOverrideUrlLoading");
            return super.shouldOverrideUrlLoading(view, url);
        }

    };
}
