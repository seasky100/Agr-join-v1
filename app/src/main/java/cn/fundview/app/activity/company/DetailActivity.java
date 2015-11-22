package cn.fundview.app.activity.company;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.company.DetailView;

/**
 * 企业详细
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：ExpertDetailActivity
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月26日 下午4:55:27
 * 修改人：lict
 * 修改时间：2015年6月26日 下午4:55:27
 * 修改备注：
 */
public class DetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_detail);

        String title = getIntent().getStringExtra("compName");

        this.setCommonTitleBar(title, R.id.webView, null, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            //登录完成后,刷新页面,需要从新加载json文件
            ((DetailView) this.webView).reloadPage();
        }
    }
}

