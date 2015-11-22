package cn.fundview.app.action.msg;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.view.ABaseWebView;

public class MsgListAction extends ABaseAction {

    private long deliverTime;
    private String title;

    public MsgListAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    public void execute() {

        handle(true);
    }

    /**
     * 异步处理
     **/
    @Override
    protected void doAsynchHandle() {

//		this.showWaitDialog();

        //政府提交数据信息
//		List<ReportMsgInfo> list = DaoFactory.getInstance(context).getReportDataDao().getLastData(1);
//		if (null != list && list.size() > 0) {
//			deliverTime = list.get(0).getDeliverTime();
//			title = list.get(0).getTitle();
//		}
    }

    /**
     * 执行处理结果
     **/
    @Override
    protected void doHandleResult() {

//		// webView.loadUrl("javascript:Page.clearData();");
//		//得到提交数据的消息总数
//		int fvCount = NewMessageCountMgr.getInstance().getMessageCount(MsgTypeEnum.REPORT_DATA_MSG.getValue());
//		String fv = JsMethod.createJs("javascript:Page.setReportInfor(${reqTime}, ${title}, ${count});", deliverTime, title, fvCount);
//		webView.loadUrl(fv);
//		System.out.println(fv);
//		this.closeWaitDialog();
    }


}
