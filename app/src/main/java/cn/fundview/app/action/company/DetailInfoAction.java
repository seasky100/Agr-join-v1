package cn.fundview.app.action.company;

import android.content.Context;

import com.alibaba.fastjson.annotation.JSONCreator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 企业详细页面的查看详细action
 */
public class DetailInfoAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer compId;
    private String lastModify;
    private String attr;
    /**
     * 处理结果
     */
    private Map<String, String> detail = null;

    public DetailInfoAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer compId, String lastModify, String attr) {

        this.compId = compId;
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
        String compSavePath = DeviceConfig.getSysPath(context) + cn.fundview.app.tool.Constants.COMPANY_JSON_DIR + compId + "/";//
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
