package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.LoadProfileAction;
import cn.fundview.app.action.my.SaveProfileProfessionalTitleAction;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

public class ProfileProfessionalTitleWebView extends ABaseWebView {

    public ProfileProfessionalTitleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/my/profile-professoional-title.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

        LoadProfileAction action = new LoadProfileAction(context, this);
        action.execute(UserInfor.PROFESSIONAL_TITLE, "Page.initPage",
                PreferencesUtils.getInt(context, Constants.ACCOUNT_ID), ((Activity) context).getIntent().getBooleanExtra("editable", true));
    }

    @JavascriptInterface
    public void save(String name) {

        SaveProfileProfessionalTitleAction action = new SaveProfileProfessionalTitleAction(context, this);
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
        this.loadUrl("javascript:Page.saveProfessionalTitle();");
    }
}
