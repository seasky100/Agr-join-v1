package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.LoadProfileAction;
import cn.fundview.app.action.my.SaveProfileNameAction;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

public class ProfileNameWebView extends ABaseWebView {

    private int uid;

    public ProfileNameWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        uid = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);
        this.loadUrl("file:///android_asset/page/my/profile-name.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

        //加载我的名字
        LoadProfileAction action = new LoadProfileAction(context, this);

        action.execute("name", "Page.initPage", uid, ((Activity) context).getIntent().getBooleanExtra("editable", true));
    }

    @JavascriptInterface
    public void save(String name) {

        SaveProfileNameAction action = new SaveProfileNameAction(context, this);
        action.execute(name, "Page.showHintDialog", uid);
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
        this.loadUrl("javascript:Page.saveName();");
    }
}
