package cn.fundview.app.activity.history;

import android.os.Bundle;
import android.widget.EditText;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.SearchTitleBar;
import cn.fundview.app.view.history.SearchHistoryView;

/**
 * 搜索历史activity
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：SearchHistoryActivity
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月25日 上午10:40:35
 * 修改人：lict
 * 修改时间：2015年6月25日 上午10:40:35
 * 修改备注：
 */
public class SearchHistoryActivity extends ABaseActivity {

    private SearchTitleBar searchTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_history);

        SearchHistoryView webView = (SearchHistoryView) this.findViewById(R.id.webView);

        searchTitleBar = (SearchTitleBar) this.findViewById(R.id.titleBarView);

        this.setSearchPageTitleBar(R.id.webView);
        int type = this.getIntent().getIntExtra("type", 0);

        EditText et1 = searchTitleBar.getSearchEditText();
        switch (type) {

            case 0:
                et1.setHint("专家搜索");
                break;
            case 1:
                et1.setHint("成果搜索");
                break;
            case 2:
                et1.setHint("企业搜索");
                break;
            case 3:
                et1.setHint("需求搜索");
                break;
            case 4:
                et1.setHint("院所搜索");
                break;
            case 5:
                et1.setHint("产品搜索");
                break;
        }

        searchTitleBar.setType(type);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        SearchHistoryView webView = (SearchHistoryView) this.findViewById(R.id.webView);
        webView.init();

    }
}
