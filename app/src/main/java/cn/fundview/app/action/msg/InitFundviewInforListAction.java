package cn.fundview.app.action.msg;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

/**
 * 丰景资讯列表页 初始化,加载本地的所有未读的数据
 */
public class InitFundviewInforListAction extends ABaseAction {

    /**
     * param
     */
    private int id;
    private int size;
    /**
     * 处理结果
     */
    private List<FundviewInfor> results = null;

    public InitFundviewInforListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(int id, int size) {

        this.id = id;
        this.size = size;
        handle(true);
    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        results = DaoFactory.getInstance(context).getFundviewInforDao().getAllUnRead(size);
        if (null == results || results.size() == 0) {

            //本地没有未读的消息,查询本地的最新的2条资讯, 从列表中进入的
            results = DaoFactory.getInstance(context).getFundviewInforDao().getLastest(2);
            if (null == results || results.size() == 0) {

                //本地没有任何的资讯信息,显示一个默认的欢迎资讯
            }
        }

        //设置所有的消息已读
        DaoFactory.getInstance(context).getFundviewInforDao().setRead(id);
    }

    /**
     * 处理结果
     */
    protected void doHandleResult() {

        String js = "";
        if (null != results && results.size() > 0) {

            for (int i = 0; i < results.size(); i++) {

                FundviewInfor infor = results.get(i);
                if (i == results.size() - 1) {

                    //当前查询的第一条
                    webView.loadUrl("javascript:Page.setId(" + infor.getId() + ");");//将当前查询的最小id 传递到页面,再由页面传递到view,解决view 取不到页面中展示的最小的id
                }
                js = JsMethod.createJs(
                        "javascript:Page.addInfor" + "(${id}, ${title}, ${logo}, ${introduction}, ${deliverTime}, ${publishTime}, ${updateDate});", infor.getId(),
                        infor.getTitle(), infor.getLogo(), infor.getIntroduction(), infor.getPublishDate(), infor.getPublishDate(), infor.getUpdateDate());

                webView.loadUrl(js);
            }

            webView.loadUrl("javascript:Page.init();");
        }

    }
}
