package cn.fundview.app.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.view.OptionMenuListener;
import cn.fundview.app.view.my.ProfileIconView;

/**
 * 从本地和相机中选择照片作为我的头像
 **/
public class ProfileIconActivity extends ABaseActivity implements OptionMenuListener {

    /**
     * 设置我的头像的请求吗
     **/
    public static final int OPEN_TAKE_PIC = 1234565;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_icon);

        this.setCommonTitleBar("我的头像", R.id.webView, null, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == OPEN_TAKE_PIC) {

            Bundle bundle = data.getExtras();
            String message = bundle.getString("takePic");
            Log.d(Constants.TAG, message);
            ProfileIconView view = (ProfileIconView) findViewById(R.id.webView);
            view.onRestart(message);
        }
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
