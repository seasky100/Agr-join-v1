package cn.fundview.app.action.my;

import java.util.List;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

public class MyAttentCompListAction extends ABaseAction {

    /** 参数 **/

    /**
     * 处理结果
     **/
//	private List<ProjectAttent> result = null;
    public MyAttentCompListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute() {

        handle(false);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doHandle() {

        //result = DaoFactory.getInstance(context).getProjectAttentDao().getProjectAttentList();
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

//		if (null != result && result.size() > 0)
//
//			for (ProjectAttent each : result) {
//
//				String js = JsMethod.createJs("javascript:Page.addItem(${projId}, ${logo}, ${title}, ${comps}, ${attent}, ${lastModify});",
//						each.getProjId(), each.getLogo(), each.getTitle(), each.getComps(), each.getAttent(), each.getLastModify());
//				webView.loadUrl(js);
//			}
//		else
//			webView.loadUrl("javascript:Page.hintFindProj();");
    }
}
