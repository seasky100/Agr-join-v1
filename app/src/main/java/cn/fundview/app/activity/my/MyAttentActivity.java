package cn.fundview.app.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.my.AttentListAction;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.OptionMenuListener;
import cn.fundview.app.view.my.MyAttentWebView;

/**
 * 我的关注
 */
public class MyAttentActivity extends ABaseActivity implements OptionMenuListener {

    public static final int TO_LOIN_ATTENT_EXPERT_FLAG = 123;//关注的专家 未登录标识
    public static final int TO_LOIN_ATTENT_COMPANY_FLAG = 124;//关注的企业 未登录标识
    public static final int TO_LOIN_HOME_FLAG = 125;//未登录标识  登录后跳转到首页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_attent);
        int type = getIntent().getIntExtra("attent-type", 1);//1我关注的专家  2我关注的企业
        if (type == 2) {

            this.setCommonTitleBar("我关注的专家", R.id.webView, null, false);
        } else if (type == 1) {

            this.setCommonTitleBar("我关注的企业", R.id.webView, null, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 1) {

                //重新加载我关注的企业页面
                int pageSize = getIntent().getIntExtra("pageSize", 10);//每页显示的条数
                int page = getIntent().getIntExtra("page", 0);//每页显示的条数

                Integer uid = PreferencesUtils.getInt(this, Constants.ACCOUNT_ID);//关注者id
                AttentListAction attentAction = new AttentListAction(this, (MyAttentWebView) this.findViewById(R.id.webView));
                attentAction.execute(1, uid, page, pageSize, true);
            } else if (requestCode == 2) {

                //重新加载我关注的专家页面
                int pageSize = getIntent().getIntExtra("pageSize", 10);//每页显示的条数
                int page = getIntent().getIntExtra("page", 0);//每页显示的条数
                Integer uid = PreferencesUtils.getInt(this, Constants.ACCOUNT_ID);//关注者id
                AttentListAction attentAction = new AttentListAction(this, (MyAttentWebView) this.findViewById(R.id.webView));
                attentAction.execute(2, uid, page, pageSize, true);
            }
        }
    }

    @Override
    public void onClick(int menuId) {
        // TODO Auto-generated method stub
        if (menuId == R.id.quit) {

            //在我的页面
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }
    }
}
