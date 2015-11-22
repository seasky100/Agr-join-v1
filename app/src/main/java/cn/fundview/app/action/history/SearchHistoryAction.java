package cn.fundview.app.action.history;

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
    private int type = 0;// 搜索的类型 0专家 1成果  2 企业 3 需求

    /**
     * 执行结果
     **/
    private List<SearchHistory> list = null;

    public SearchHistoryAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String key, int type) {
        this.type = type;
        this.key = key;
        if (key.equals("")) {

            // 加载专家的所有搜索历史
            handle(false);
        } else {
            // 根据关键字加载历史 -- 异步执行
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

        System.out.println("异步至查询搜索历史...");
        list = DaoFactory.getInstance(context).getSearchHistoryDao()
                .getSearchHistorys(key, type);
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {


        webView.loadUrl("javascript:Page.cleanData();");

        if (null != list && list.size() > 0) {

            // 循环加载项目
            for (SearchHistory history : list) {

                String js = JsMethod.createJs(
                        "javascript:Page.addItem(${key},${type});", history.getWords(), history.getType());
                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.showNoHistory();");
        }

    }


}
