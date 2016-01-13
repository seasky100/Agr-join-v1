package cn.fundview.app.activity.my;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.OptionMenuListener;

/**
 * 专家研究领域  企业经营范围 个人的从事行业activity
 **/
public class ProfileInforActivity extends ABaseActivity implements OptionMenuListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_infor);

        int type = PreferencesUtils.getInt(this, Constants.ACCOUNT_TYPE_KEY);
        boolean editable = getIntent().getBooleanExtra("editable", true);
        if (type == UserInfor.COMPANY_TYPE) {

            if (editable) {

                this.setCommonTitleBar("经营范围", R.id.webView, "保存", false);
            } else {

                this.setCommonTitleBar("经营范围", R.id.webView, null, false);
            }
        } else if (type == UserInfor.EXPERT_TYPE) {

            if (editable) {

                this.setCommonTitleBar("研究领域", R.id.webView, "保存", false);
            } else {

                this.setCommonTitleBar("研究领域", R.id.webView, null, false);
            }
        } else if (type == UserInfor.PERSON_TYPE) {

            if (editable) {

                this.setCommonTitleBar("从事行业", R.id.webView, "保存", false);
            } else {

                this.setCommonTitleBar("从事行业", R.id.webView, null, false);
            }
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
