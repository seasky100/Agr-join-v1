package cn.fundview.app.activity.my;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.OptionMenuListener;

public class ProfileAreaActivity extends ABaseActivity implements OptionMenuListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_area);

        this.setCommonTitleBar("我的地区", R.id.webView, null, false);
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
