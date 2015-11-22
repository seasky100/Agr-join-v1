package cn.fundview.app.action.my;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.view.ABaseWebView;

public class InitUserInforPageAction extends ABaseAction {

//	/** 参数 **/
//	private String userId;
//	
//	/** 处理结果 **/
//	private UserInfor infor;

    public InitUserInforPageAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

//	public void execute(String userId) {
//
//		this.userId = userId;
//		this.showWaitDialog();
//		handle(true);
//	}
//
//	/** 执行处理 **/
//	protected void doAsynchHandle() {
//
//		// 先判断用户是否存在
////		UserInfor userInfor = DaoFactory.getInstance(context).getUserInforDao().getUserInfor(userId);
////		if (null == userInfor) {
////			// 去下载和保存
////			userInfor = RService.getInstance().getUserInfor(userId);
////			if (null != userInfor)
////				DaoFactory.getInstance(context).getUserInforDao().saveUserInfor(userInfor);
////		}
////
////		infor = userInfor;
//	}
//
//	/** 处理结果 **/
//	protected void doHandleResult() {
//
//		if (null != infor) {
//			
////			if(!infor.getUserId().equals(Util.getUserId(context))){
////				
////				webView.loadUrl("javascript:Page.showFunc();");
////			}
////			
////			String js = JsMethod.createJs(
////					"javascript:Page.initPage(${uid}, ${account}, ${name}, ${area}, ${require}, ${provide}, ${logo}, ${status});",
////					infor.getUserId(), infor.getAccount(), infor.getName(), infor.getArea(), infor.getRequire(), infor.getProvide(),
////					infor.getHeadIcon(), infor.getAttent());
////			webView.loadUrl(js);
//			
//		}else{
//			
//			webView.loadUrl("javascript:Page.hideFunc();");
//		}
//		this.closeWaitDialog();
//	}
}
