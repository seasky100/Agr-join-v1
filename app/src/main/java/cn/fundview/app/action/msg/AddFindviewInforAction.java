package cn.fundview.app.action.msg;

import java.util.Date;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

public class AddFindviewInforAction extends ABaseAction {

    /**
     * 参数
     */
    private FundviewInfor infor = null;

    public AddFindviewInforAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(FundviewInfor infor) {

        this.infor = infor;
        handle(false);
    }

    /**
     * 处理结果
     */
    protected void doHandleResult() {

        if (null != infor) {

//			String js = JsMethod
//					.createJs(
//							"javascript:Page.addNewMsgInfor(${id}, ${title}, ${logo}, ${introduction}, ${DeliveryTime}, ${publishTime},${parentId});",
//					infor.getId(), infor.getTitle(), infor.getLogo(), infor.getIntroduction(), infor.getDeliveryTime(),new Date().getTime() ,new Date().getTime());
//
//			webView.loadUrl(js);
        }
    }

}
