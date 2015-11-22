package cn.fundview.app.view.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.view.ABaseWebView;

/**
 * 客服电话webView
 **/
public class HotLineWebView extends ABaseWebView {


    public HotLineWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/my/hot-line.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

    }

    //拨打电话
    @JavascriptInterface
    public void dail(String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        context.startActivity(intent);
    }

    //发送邮件
    @JavascriptInterface
    public void mailTo(String email) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        context.startActivity(Intent.createChooser(intent, "选择发送邮件的客户端"));
    }

    //打开网页
    @JavascriptInterface
    public void openPage(String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }
}
