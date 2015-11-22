package cn.fundview.app.activity.requ;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 技术需求详细activity
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：ExpertDetailActivity
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月10日 上午10:33:11
 * 修改人：lict
 * 修改时间：2015年6月10日 上午10:33:11
 * 修改备注：
 */
public class DetailActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_requ_detail);

        String title = getIntent().getStringExtra("requName");

        this.setCommonTitleBar(title, R.id.webView, null, false);
    }


}

