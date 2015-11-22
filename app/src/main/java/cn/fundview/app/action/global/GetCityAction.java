package cn.fundview.app.action.global;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.model.Area;
import cn.fundview.app.tool.AreaInforParser;
import cn.fundview.app.view.ABaseWebView;

public class GetCityAction extends ABaseAction {

    /**
     * 参数
     **/
    private int parentId;
    private String callback;

    /**
     * 处理的结果
     **/
    private String result = null;

    public GetCityAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(int parentId, String callback) {

        this.callback = callback;
        this.parentId = parentId;
        this.showWaitDialog();
        handle(true);
    }

    /**
     * 异步处理
     **/
    @Override
    protected void doAsynchHandle() {


        try {
            result = "";//AreaInforParser.getAreas(context, "city-" + parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        this.closeWaitDialog();
        webView.loadUrl("javascript:" + callback + "('" + result + "');");
    }

}
