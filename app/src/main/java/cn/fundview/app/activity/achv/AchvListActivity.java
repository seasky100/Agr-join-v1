package cn.fundview.app.activity.achv;

import android.os.Bundle;
import android.view.View;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 成果列表
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：AchvListActivity 类描述： 创建人：lict 创建时间：2015年6月12日
 * 下午1:53:34 修改人：lict 修改时间：2015年6月12日 下午1:53:34 修改备注：
 */
public class AchvListActivity extends ABaseActivity {

    private View oldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_achv_list);

        this.setCommonTitleBar("成果列表", R.id.webView, null, true);

    }
}
