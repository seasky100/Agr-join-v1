package cn.fundview.app.action.company;

import java.util.List;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

/**
 * 搜索历史
 *
 * @version 1.0
 */
public class SearchHistoryAction extends ABaseAction {

    /**
     * 参数
     **/
    private String key;
    private int type = 2;

    /**
     * 执行结果
     **/
    private List<SearchHistory> list = null;

    public SearchHistoryAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String key) {

        if (key.equals("")) {

            // 加载企业的所有搜索历史
            handle(false);
        } else {
            // 根据关键字加载历史 -- 异步执行
            this.key = key;
            handle(true);
        }
    }

    @Override
    protected void doHandle() {

        list = DaoFactory.getInstance(context).getSearchHistoryDao()
                .getSearchHistorys(type);
    }

    @Override
    protected void doAsynchHandle() {

        list = DaoFactory.getInstance(context).getSearchHistoryDao()
                .getSearchHistorys(key, type);
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if ("".equals(key))
            webView.loadUrl("javascript:Page.showNoHistory();");

        webView.loadUrl("javascript:Page.cleanData();");

        if (null != list && list.size() > 0) {

            // 循环加载项目
            for (SearchHistory history : list) {

                String js = JsMethod.createJs(
                        "javascript:Page.addItem(${key});", history.getWords());
                webView.loadUrl(js);
            }
        }
    }

}
