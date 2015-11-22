package cn.fundview.app.activity.my;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.OptionMenuListener;

public class ProfileAddrActivity extends ABaseActivity implements OptionMenuListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_addr);
        if (getIntent().getBooleanExtra("editable", true)) {

            this.setCommonTitleBar("我的详细地址", R.id.webView, "保存", false);
        } else {

            this.setCommonTitleBar("我的详细地址", R.id.webView, null, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(int menuId) {
        // TODO Auto-generated method stub
        if (menuId == R.id.quit) {

            //在我的页面
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("backPage", "my");// 表示在登录页面返回的话直接返回到我的页面
            startActivity(intent);
            this.finish();
        }
    }
}
