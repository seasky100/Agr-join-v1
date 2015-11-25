package cn.fundview.app.action.fundProject;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.FundProject;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 融资项目详细
 */
public class FundProjectDetailAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer id;
    /**
     * 处理结果
     */
    /**
     * 处理结果
     */
    private FundProject fundProject;
    private List<FundProject> fundProjects;

    public FundProjectDetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer id) {

        this.id = id;
        handle(true);

        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        //设置参数
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));

        try {
            ResultBean resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_FUND_PROJ_DETAIL_URL));
            if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                fundProject = JSON.parseObject(resultBean.getResult(), FundProject.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (fundProject != null) {


            webView.loadUrl("javascript:Page.setTitlebar('"+fundProject.getProjField()+"')");

            //name, jd, logo, compname, compId, price, projField, summary
            String js = JsMethod.createJs(
                    "javascript:Page.initBasic(${name}, ${jd}, ${logo}, ${compname}, ${compId}, ${price},${projField}, " +
                            "${summary});", fundProject.getProjName(),fundProject.getJdName(), fundProject.getLogo(), fundProject.getName(),fundProject.getId(), fundProject.getInvest(), fundProject.getProjField(), fundProject.getSummary());

            webView.loadUrl(js);
        } else {

            // 失败提示
            webView.loadUrl("javascript:Page.loadFailed()");
        }

        this.closeWaitDialog();
    }

}