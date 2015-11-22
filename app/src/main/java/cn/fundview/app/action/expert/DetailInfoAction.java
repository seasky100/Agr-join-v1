package cn.fundview.app.action.expert;

import android.content.Context;

import java.io.File;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 专家详细页面的查看详细action
 */
public class DetailInfoAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer expertId;
    private String lastModify;
    private String attr;
    /**
     * 处理结果
     */
    private Map<String, String> detail = null;

    public DetailInfoAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer expertId, String lastModify, String attr) {

        this.expertId = expertId;
        this.lastModify = (lastModify == null || lastModify == "") ? "0" : lastModify;
        this.attr = attr;
        handle(false);

    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doHandle() {

        // 企业详细json的存放路径
        String compSavePath = DeviceConfig.getSysPath(context) + cn.fundview.app.tool.Constants.EXPERT_JSON_DIR + expertId + "/";//
        String fileName = lastModify + ".json";//

        if (PreferencesUtils.getInt(context, cn.fundview.app.tool.Constants.LOGIN_STATUS_KEY) == cn.fundview.app.tool.Constants.LOGIN_STATUS) {

            //登录用户需要传递当前登录id
            compSavePath += "login/";
        } else {

            compSavePath += "anonymous/";
        }
        detail = JSONTools.parseJsonFile(new File(compSavePath + fileName));
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        String js = JsMethod.createJs("javascript:Page.init(${attrValue});", detail.get(attr));
        webView.loadUrl(js);
    }


}
