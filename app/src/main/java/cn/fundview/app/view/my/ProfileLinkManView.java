package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.LoadProfileAction;
import cn.fundview.app.action.my.SaveProfileLinkManAction;
import cn.fundview.app.action.my.SaveProfileNameAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

/**
 * 我的二维码view
 */
public class ProfileLinkManView extends ABaseWebView {

    public ProfileLinkManView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/my/profile-linkman.html");
    }

    @Override
    public void init() {

        LoadProfileAction action = new LoadProfileAction(context, this);
        action.execute(UserInfor.LINKMAN, "Page.initPage", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID), ((Activity) context).getIntent().getBooleanExtra("editable", true));
    }

    @JavascriptInterface
    public void save(String name) {

        SaveProfileLinkManAction action = new SaveProfileLinkManAction(context, this);
        action.execute(name, "Page.showHintDialog", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));
    }

    @JavascriptInterface
    public void closePage() {

        ((Activity) context).finish();
    }

    @Override
    public void onClickRight() {
        super.onClickRight();
        this.loadUrl("javascript:Page.saveLinkMan();");
    }
}
