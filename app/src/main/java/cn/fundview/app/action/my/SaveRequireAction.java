package cn.fundview.app.action.my;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

public class SaveRequireAction extends ABaseAction {

    /**
     * 参数
     **/
    private String addr;
    private String callback;
    private String uid;

    /**
     * 处理结果
     ***/
    private boolean result;

    public SaveRequireAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String addr, String callback, String uid) {

        this.addr = addr;
        this.callback = callback;
        this.uid = uid;
        handle(true);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doAsynchHandle() {

//		if (null != addr && !"".equals(addr)) {
//			result = DaoFactory.getInstance(context).getProfileDao().updateAddrById(addr, uid);
//			if (result) {
//				
//				// 上传到网络
//				RService.getInstance().updateProfile(uid, "addr", addr);
//			}
//		}
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        String js = JsMethod.createJs("javascript:" + callback + "(${result});", result);
        webView.loadUrl(js);
    }

}
