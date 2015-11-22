package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.LoadProfileAction;
import cn.fundview.app.action.my.SaveProfileAddrAction;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

public class ProfileAddrWebView extends ABaseWebView {

    public ProfileAddrWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/my/profile-addr.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

        //加载我的详细地址
        Intent intent = ((Activity) context).getIntent();
        boolean editable = intent.getBooleanExtra("editable", true);
        LoadProfileAction action = new LoadProfileAction(context, this);
        action.execute(UserInfor.ADDR, "Page.initPage", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID), editable);
    }

    @JavascriptInterface
    public void save(String name) {

        SaveProfileAddrAction action = new SaveProfileAddrAction(context, this);
        action.execute(name, "Page.showHintDialog", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));
    }

    @JavascriptInterface
    public void closePage() {

        ((Activity) context).finish();
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

        this.loadUrl("javascript:Page.saveAddr();");
    }
}
