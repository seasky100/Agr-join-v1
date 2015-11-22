package cn.fundview.app.action.company;

import java.util.List;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

/**
 * 搜索项目
 */
public class SearchCompAction extends ABaseAction {

    /**
     * 参数
     **/
    private String key;
    //private int type = 2;

    /**
     * 执行结果
     **/
    private List<Company> list = null;

    public SearchCompAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String key) {

        this.key = key;

        handle(true);
        this.showWaitDialog();
    }

    @Override
    protected void doAsynchHandle() {

        // 获取数据
//		List<CompItem> list = RService.getInstance().searchProj(key);
//
//		if (null != list && list.size() > 0) {
//			// 保存服务器
//			DaoFactory.getInstance(context).getCompDao().saveCompList(list);
//		} else {
//			// TODO 判断网络是否连接 连接了 到数据库中获取 否则返回null
//			list = DaoFactory.getInstance(context).getCompDao().getCompListByWords(key);
//		}

    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (null != list && list.size() > 0) {

            webView.loadUrl("javascript:Page.cleanData();");
            // 循环加载项目
            for (Company item : list) {

                String js = JsMethod
                        .createJs(
                                "javascript:Page.addItem(${id}, ${logo}, ${name}, ${trade}, ${attent}, ${area}, ${time});",
                                item.getId(), item.getLogo(), item.getName(),
                                item.getTradeName(), item.getAttention(),
                                item.getAreaName(), item.getUpdateDate());
                webView.loadUrl(js);
            }
        } else {
            webView.loadUrl("javascript:Page.hintError(" + true + ");");
        }
        this.closeWaitDialog();
    }
}
