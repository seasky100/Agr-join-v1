package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.SaveProfileInforAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

public class ProfileInforView extends ABaseWebView {

    public ProfileInforView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/my/profile-infor.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

        UserInfor profile = DaoFactory.getInstance(context).getUserInforDao().getById(PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));

        int type = PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY);
        String infor = type == UserInfor.EXPERT_TYPE ? profile.getSpecialty() : type == UserInfor.COMPANY_TYPE ? profile.getService() : profile.getWork();
        String js = JsMethod.createJs("javascript:Page.initPage("
                        + "${result},${type},${editable});", infor, type,
                ((Activity) context).getIntent().getBooleanExtra("editable", true));
        loadUrl(js);
    }

    @JavascriptInterface
    public void save(String name) {

        SaveProfileInforAction action = new SaveProfileInforAction(context, this);
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

        this.loadUrl("javascript:Page.saveInfor();");
    }
}
