package cn.fundview.app.action.fundProject;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.FundProject;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.achv.AchvListView;
import cn.fundview.app.view.fundProject.ListView;

/**
 * 融资项目列表
 */
public class FundProjectListAction extends ABaseAction {

    private int pageSize;
    private int page;
    private Map<String, String> condition;
    private boolean hasNext;
    private int total;


    /**
     * 处理结果
     */
    private List<FundProject> results = null;

    public FundProjectListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(int page, int pageSize, Map<String, String> condition) {

        this.page = page;
        this.pageSize = pageSize;
        this.condition = condition;
        this.showWaitDialog();

        handle(true);
    }

    /**
     * 执行异步处理
     */
    @Override
    protected void doAsynchHandle() {

        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<FundProject> resultBean = null;
            Map<String, String> param = new HashMap<>();
            param.put("currentPage", page + "");
            param.put("pageSize", pageSize + "");

            try {
                resultBean = JSONTools.parseList(RService.doPostSync(param,
                        cn.fundview.app.domain.webservice.util.Constants.GET_FUND_PROJ_LIST_URL), FundProject.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    total = resultBean.getTotalSize();
                    results =resultBean.getList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {

        if (total > 0) {
            if (ListView.class.isInstance(webView)) {
                ((ListView) webView).setTitle("项目列表(" + total + ")");
            }
        }
        if (null != results && results.size() > 0) {

            // 循环加载项目
            for (FundProject item : results) {

                //(id, logo, name, jd, compName, invest)
                String js = JsMethod.createJs(
                        "javascript:Page.addItem(${id}, ${logo}, ${name}, ${jd}, ${compName}, ${invest});",
                        item.getId(), item.getLogo(), item.getProjName(), item.getJdName(), item.getName(), item.getInvest());
                webView.loadUrl(js);
            }

            if (hasNext) {

                webView.loadUrl("javascript:Page.moreBtn('true');");
            } else {

                webView.loadUrl("javascript:Page.moreBtn('false');");
            }


        } else {

            //加载失败
            webView.loadUrl("javascript:Page.moreBtn('false');");
            if (this.page == 1) {

                //第一页没有加载到数据的话提示加载失败
                webView.loadUrl("javascript:Page.loadFailed();");
            }
        }

        this.closeWaitDialog();

    }
}