package cn.fundview.app.action.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

public class MsgInforSimpleListAction extends ABaseAction {

    /**
     * 参数
     */
    private int pageSize = 8;

    /**
     * 处理结果
     */
    private List<FundviewInfor> results = null;

    public MsgInforSimpleListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute() {

        this.showWaitDialog();

        handle(true);
    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        // 模拟数据
//		results = DaoFactory.getInstance(context).getFindviewInforDao().getInforList(null, pageSize);

    }

    /**
     * 处理结果
     */
    protected void doHandleResult() {
        boolean switchVisble = false;

        if (null != results) {
            // 循环加载项目
            for (FundviewInfor infor : results) {

                String js = JsMethod
                        .createJs(
                                "javascript:Page.addItem(${id}, ${title}, ${publishTime});",
                                infor.getId(), infor.getTitle(),
                                new Date().getTime());

                webView.loadUrl(js);

            }

            if (results.size() >= pageSize)
                switchVisble = true;

        } else {

            //加载失败
            //webView.loadUrl("javascript:Page.loadFailed();");
        }

        // 判断是否显示更多以及关闭loading转圈
        webView.loadUrl("javascript:Page.switchVisble(" + switchVisble + ")");

        this.closeWaitDialog();
    }

    // 测试数据
    private List<FundviewInfor> getData() {

        List<FundviewInfor> list = new ArrayList<FundviewInfor>();
//		FundviewInfor infor1 = new FundviewInfor(
//				94,
//				"严字当头，怎样解读农村金融机构补贴新规？",
//				"http://www.findview.cn/static/thumb/consultation_logo/20140410/2014041016475505096_426_240.jpg",
//				"财政部3月28日印发《农村金融机构定向费用补贴管理办法》。符合补贴条件的三类金融机构，即银监会批准设立的村镇银行、贷款公司、农村资金互助社。明确了补贴资金将改由中央和地方财政按比例分担，充分照顾中西部。",
//				"1397108561000", "1397108561000", "1397108561000");
//
//		FundviewInfor infor2 = new FundviewInfor(
//				93,
//				"创新“三农”金融服务助推现代农业发展",
//				"http://www.findview.cn/static/thumb/consultation_logo/20140409/2014040912432782750_426_240.png",
//				"农业部副部长张桃林在山东、江苏专题调研金融支农惠农工作。张桃林强调，要总结推广各地在金融支农惠农方面的成功经验和模式，统筹考虑不同经营主体在不同发展阶段的金融需求，发挥好公共财政资金引导和市场配置资源两方面的作用，不断丰富和完善面向“三农”的金融产品和服务，促进现代农业加快发展和农民增收。",
//				"1397018190000", "1397018190000", "1397018190000");
//
//		FundviewInfor infor3 = new FundviewInfor(
//				92,
//				"天津市武清区投8000万调整农业结构 打造精品节点34个",
//				"http://www.findview.cn/static/thumb/consultation_logo/20140408/201404080930030833_426_240.png",
//				"作为天津市的传统农业大区，武清区从去年下半年开始在全区26个农业镇街实施种植业结构调整，预计投入财政资金8000万元，到2014年底完成调整面积6万亩，新发展设施农业1.2万亩，打造特色精品节点34个。",
//				"1396920431000", "1396920431000", "1396920431000");
//
//		FundviewInfor infor4 = new FundviewInfor(
//				91,
//				"“十二五”农业农村经济发展成效斐然",
//				"http://www.findview.cn/static/thumb/consultation_logo/20140404/2014040415015362515_426_240.png",
//				"近期，农业部对“十二五”农业农村经济发展系列规划进行了中期评估，系统总结分析了规划主要指标、重点任务和重大工程的完成情况。规划实施以来，农业农村经济发展各主要指标实现程度良好，各项重大工程任务积极推进，中期目标顺利实现。",
//				"1396594230000", "1396594230000", "1396594230000");
//
//		FundviewInfor infor5 = new FundviewInfor(
//				90,
//				"潍坊被列为现代农业综合改革试点城市",
//				"http://www.findview.cn/static/thumb/consultation_logo/20140403/2014040316511530333_426_240.png",
//				"近日，国家发改委批复将潍坊、苏州等市列为综合改革试点。",
//				"1396514946000", "1396514946000", "1396514946000");
//
//		list.add(infor2);
//		list.add(infor3);
//		list.add(infor4);
//		list.add(infor5);
        return list;
    }
}
