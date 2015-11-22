package cn.fundview.app.action.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.ExpertDao;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.Area;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.AreaInforParser;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 获取地区列表
 */
public class GetAreaListAction extends ABaseAction {

    /**
     * 参数
     **/
    private int level;          //地区级别
    private int parentId;       //父级id
    private int selectedId;      // 已选择的id
    /**
     * 处理的结果
     **/
    private List<Area> results;

    public GetAreaListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(int parentId, int selectId, int level) {

        this.parentId = parentId;
        this.level = level;
        this.selectedId = selectId;
        this.showWaitDialog();
        handle(true);
    }

    /**
     * 异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultBean resultBean = null;
            Map<String, String> param = new HashMap<>();

            param.put("parentId", this.parentId + "");


            try {

                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_AREA_LIST));
                if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                    if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {

                        results = JSON.parseArray(resultBean.getResult(), Area.class);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        this.closeWaitDialog();
        webView.loadUrl("javascript:Page.clearData();");
        if (results != null && results.size() > 0) {

            for (Area item : results) {

                String js = JsMethod.createJs("javascript:Page.loadItem(${id}, ${name}, ${level}, ${selectedId});", item.getId(), item.getName(), item.getLevels(), selectedId);
                webView.loadUrl(js);
            }
        } else {

            //提示加载失败
            webView.loadUrl("javascript:Page.hintFail();");
        }

    }
}
