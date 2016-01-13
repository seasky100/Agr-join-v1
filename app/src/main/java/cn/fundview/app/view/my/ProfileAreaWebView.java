package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.global.GetAreaListAction;
import cn.fundview.app.action.my.SaveProfileAreaAction;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

public class ProfileAreaWebView extends ABaseWebView {

    private int uid;
    private int provinceId;
    private int cityId;
    private int countyId;

    public ProfileAreaWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        uid = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);
        this.loadUrl("file:///android_asset/page/my/profile-area.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

        Intent intent = ((Activity) context).getIntent();
        provinceId = intent.getIntExtra("provinceId", 0);
        cityId = intent.getIntExtra("cityId", 0);
        countyId = intent.getIntExtra("countyId", 0);
        //String province = intent.getStringExtra("province");

        //查询province  返回json
        new GetAreaListAction(context, this).execute(0, provinceId, 0);

    }

    @JavascriptInterface
    public void save(String ids, String names) {

        SaveProfileAreaAction action = new SaveProfileAreaAction(context, this);
        action.execute(ids, names, uid);
    }

    @JavascriptInterface
    public void openCity(int parentId, int level) {

        GetAreaListAction action = new GetAreaListAction(context, this);

        int selectedId = 0;
        if(level == 1) {

            selectedId = cityId;
        }else if(level == 2){

            selectedId = countyId;
        }
        action.execute(parentId, selectedId, level);
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

    }
}
