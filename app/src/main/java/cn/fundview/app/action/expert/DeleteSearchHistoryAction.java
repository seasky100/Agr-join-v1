package cn.fundview.app.action.expert;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.view.ABaseWebView;

/**
 * 删除 专家搜索历史
 *
 * @version 1.0
 */
public class DeleteSearchHistoryAction extends ABaseAction {

    public DeleteSearchHistoryAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public boolean execute(String key, int type) {


        return DaoFactory.getInstance(context).getSearchHistoryDao().deleteByTypeAndWords(key, type);
    }

}
