package cn.fundview.app.action.msg;

import android.content.Context;

import java.util.Date;
import java.util.List;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

/**
 * 初始化显示消息列表中的资讯信息
 */
public class InitMsgListAction extends ABaseAction {

    private long deliverTime;
    private String title;
    private int unreadTotal;
    private int id = 0;

    public InitMsgListAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    /**
     * 执行action流程
     */
    public void execute() {

        handle(false);
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doHandle() {

        //查询本地的最新的一条资讯记录，可能只有id和标题
        //查询未读的资讯体条数
        unreadTotal = DaoFactory.getInstance(context).getFundviewInforDao().countUnReadFundviewInfor();
        List<FundviewInfor> fundviewInfors = DaoFactory.getInstance(context).getFundviewInforDao().getLastest(1);
        FundviewInfor fundviewInfor = null;
        if (fundviewInfors != null && fundviewInfors.size() > 0)
            fundviewInfor = fundviewInfors.get(0);

        title = "关注农业发展，与丰景一起为实现中国农业现代化而努力！";
        deliverTime = new Date().getTime();
        if (fundviewInfor != null && fundviewInfor.getTitle() != null && !fundviewInfor.getTitle().trim().equals("")) {

            title = fundviewInfor.getTitle();
            deliverTime = fundviewInfor.getPublishDate();
            id = fundviewInfor.getId();
        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        //得到提交数据的消息总数
        String fv = JsMethod.createJs("javascript:Page.setFindviewInfor(${reqTime}, ${title}, ${count}, ${id});", deliverTime, title, unreadTotal, id);
        webView.loadUrl(fv);
    }


}
