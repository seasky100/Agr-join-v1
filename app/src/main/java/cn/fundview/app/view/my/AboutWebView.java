package cn.fundview.app.view.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

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
     * 打开新页面
     **/
    @JavascriptInterface
    public void openPage(String page) {

        Intent intent = null;

        intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(page));

        context.startActivity(intent);
    }


    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }
}
