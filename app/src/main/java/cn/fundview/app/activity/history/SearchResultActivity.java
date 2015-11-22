package cn.fundview.app.activity.history;

import android.os.Bundle;
import android.view.View;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.SearchTitleBar;
import cn.fundview.app.view.history.SearchResultView;

/**
 * 搜索历史 result activity 展示 4种不同的列表专家 企业 成果 需求
 *
 * @version 1.0
 */
public class SearchResultActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result);
        String key = this.getIntent().getStringExtra("key");
        int type = this.getIntent().getIntExtra("type", 0);

        SearchTitleBar searchTitleBar = (SearchTitleBar) this.findViewById(R.id.titleBarView);
        searchTitleBar.setType(type);
        String title = key;


        // 修改标题栏的提示信息
        switch (type) {

            case 0:
                title += "--专家搜索";
                break;
            case 1:
                title += "--成果搜索";
                break;
            case 2:
                title += "--企业搜索";
                break;
            case 3:
                title += "--需求搜索";
                break;
            case 4:
                title += "--院所搜索";
                break;
            case 5:
                title += "--产品搜索";
                break;
        }
        this.setCommonTitleBar(title, R.id.webView, null, true);
        searchTitleBar.registerListener(webView);
    }

}
