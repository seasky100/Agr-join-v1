package cn.fundview.app.action.msg;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.view.ABaseWebView;

public class NewMsgAction extends ABaseAction {

    //	private Infor infor;
//	private List<ReportMsgInfo> results;
    private int count;

    public NewMsgAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

//	public void execute(Infor infor, int count) {
//
//		this.infor = infor;
//		this.count = count;
//		handle(false);
//	}
//
//	//执行同步操作
//	@Override
//	protected void doHandle() {
//
//		this.showWaitDialog();
//		switch(infor.getChatMessageType()) {
//			case 5:
//
//				//提交数据信息
////				results = DaoFactory.getInstance(context).getReportDataDao().getLastData(count);
//				break;
//		}
//	}
//
//
//	@Override
//	/**执行处理结果**/
//	protected void doHandleResult() {
//
////		if(results != null && results.size() > 0) {
////
////			for(ReportMsgInfo item : results) {
////
////				System.out.println(this.getClass().getName() + " action 调用js");
////
////				String fv = JsMethod.createJs("javascript:Page.addNewMsgInfor(${id}, ${title},${logo}, ${introduction}, ${deliveryTime}, ${publishTime}, ${parentId});", item.getId(), item.getTitle(), "", item.getContent(), item.getDeliverTime(), item.getDeliverTime(), new Date().getTime());
////				webView.loadUrl(fv);
////				System.out.println(fv);
////			}
////		}
//		this.closeWaitDialog();
//	}

}
