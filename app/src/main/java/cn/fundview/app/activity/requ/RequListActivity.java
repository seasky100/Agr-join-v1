package cn.fundview.app.activity.requ;

import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;

/**
 * 需求列表
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：AchvListActivity 类描述： 创建人：lict 创建时间：2015年6月12日
 * 下午1:53:34 修改人：lict 修改时间：2015年6月12日 下午1:53:34 修改备注：
 */
public class RequListActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_requ_list);

        //设置需求列表页的titleBar
        this.setCommonTitleBar("需求列表", R.id.webView, null, true);


    }

}
