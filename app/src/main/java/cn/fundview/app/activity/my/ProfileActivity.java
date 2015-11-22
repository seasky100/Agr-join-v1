package cn.fundview.app.activity.my;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.OptionMenuListener;
import cn.fundview.app.view.my.ProfileWebView;

public class ProfileActivity extends ABaseActivity implements OptionMenuListener {

    //调转到LoginActivity 的请求码
    public static final int TO_LOGIN_REQUEST_CODE = 1211;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.setCommonTitleBar("我的资料", R.id.webView, null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        ProfileWebView view = (ProfileWebView) this.findViewById(R.id.webView);
        view.onRestart();
    }

    @Override
    public void onClick(int menuId) {
        // TODO Auto-generated method stub
        if (menuId == R.id.quit) {

            //在我的页面
            startActivityForResult(new Intent(this, LoginActivity.class), TO_LOGIN_REQUEST_CODE);
            this.finish();
        }
    }


}
