package cn.fundview.app.view.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.global.UpdateAppAction;
import cn.fundview.app.view.ABaseWebView;

public class AboutWebView extends ABaseWebView {

    public AboutWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadUrl("file:///android_asset/page/my/about.html");
    }

    @Override
    public void init() {


    }
    /**
     * 检查更新
     */
    @JavascriptInterface
    public void update() {

        UpdateAppAction action = new UpdateAppAction(context);
        action.execute();
    }

}
