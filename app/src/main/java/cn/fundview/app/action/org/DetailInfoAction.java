package cn.fundview.app.action.org;

import android.content.Context;

import java.io.File;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 机构详细页面的查看详细action
 */
public class DetailInfoAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer orgId;
    private String lastModify;
    /**
     * 处理结果
     */
    private Map<String, String> detail = null;

    public DetailInfoAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer orgId, String lastModify) {

        this.orgId = orgId;
        this.lastModify = (lastModify == null || lastModify == "") ? "0" : lastModify;
        handle(false);

    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doHandle() {

        // 机构详细json的存放路径
        String orgSavePath = DeviceConfig.getSysPath(context) + cn.fundview.app.tool.Constants.ORG_JSON_DIR + orgId + "/";//
        String fileName = lastModify + ".json";//

        detail = JSONTools.parseJsonFile(new File(orgSavePath + fileName));
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        String js = JsMethod.createJs("javascript:Page.init(${attrValue});", detail.get("intro"));
        webView.loadUrl(js);
    }


}
