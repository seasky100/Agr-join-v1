package cn.fundview.app.view.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import java.util.Date;

import cn.fundview.app.action.expert.DeleteSearchHistoryAction;
import cn.fundview.app.action.history.SearchHistoryAction;
import cn.fundview.app.activity.history.SearchResultActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.SearchTitleBar;

public class SearchHistoryView extends ABaseWebView {

    private SearchTitleBar searchTitleBar;

    public SearchHistoryView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/history/search.html");
    }

    @Override
    public void init() {

        searchTitleBar = this.getTitleBarView();
        // 查询历史记录
        SearchHistoryAction action = new SearchHistoryAction(context, this);

        int type = ((Activity) context).getIntent().getIntExtra("type", 0);
        action.execute("", type);
    }

    /**
     * 删除某个历史记录
     **/
    @JavascriptInterface
    public boolean deleteHistory(String key, int type) {

        // 企业的搜索历史 类型是2
        DeleteSearchHistoryAction action = new DeleteSearchHistoryAction(
                context, this);
        return action.execute(key, type);
    }

    /**
     * 点击了历史记录
     **/
    @JavascriptInterface
    public void clickHistory(String key, int type) {

        openSearchPage(key, type);
    }

    /**
     * 当搜索框的文字改变
     **/
    @Override
    public void onClickMiddle() {

        String key = searchTitleBar.getKey();
        int type = searchTitleBar.getType();
        SearchHistoryAction action = new SearchHistoryAction(context, this);
        action.execute(key, type);
    }

    /**
     * 执行搜索
     **/
    @Override
    public void onClickRight() {

        String key = searchTitleBar.getKey();
        int type = searchTitleBar.getType();
        // 保存 到本地
        SearchHistory item = new SearchHistory();
        item.setType(type);
        item.setWords(key);
        item.setSearchTime(new Date().getTime() + "");
        SearchHistory oldItem = DaoFactory.getInstance(context).getSearchHistoryDao().getSearchHistoryByNameAndType(key, type);
        if (oldItem == null) {
            DaoFactory.getInstance(context).getSearchHistoryDao()
                    .save(item);
        }
        openSearchPage(key, type);
    }

    private void openSearchPage(String key, int type) {

        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putInt("type", type);
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
