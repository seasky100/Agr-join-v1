package cn.fundview.app.action.msg;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.fundview.app.action.BaseAction;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.view.AsyncTaskCompleteListener;
import cz.msebera.android.httpclient.Header;

/**
 * 丰景咨询 历史数据加载action
 */
public class FundviewInforHistoryAction extends BaseAction {

    /**
     * 参数
     */
    private int id;
    private int size;

    public FundviewInforHistoryAction(Context context,int id,int size, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        super(context,asyncTaskCompleteListener);
        execute(id,size);
    }

    @Override
    public void execute(Object... params) {

        this.id = (int)params[0];
        this.size = (int)params[1];
        super.execute(params);
    }

    @Override
    public void disConnectedHandler() {
    }

    @Override
    public void mobileHandler() {
    }

    @Override
    public void wifiHandler() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("currentId", id);
        params.put("pageSize", size);
        client.get(Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String result = response.getString("result");

                    List<FundviewInfor> list = JSON.parseArray(result, FundviewInfor.class);
                    mAsyncTaskCompleteListener.complete(0,1,list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }
}
