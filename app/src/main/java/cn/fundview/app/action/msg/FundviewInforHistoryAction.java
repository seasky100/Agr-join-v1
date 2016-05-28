package cn.fundview.app.action.msg;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import cn.fundview.app.action.BaseAction;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 丰景咨询 历史数据加载action
 */
public class FundviewInforHistoryAction extends BaseAction {

    /**
     * 参数
     */
    private int id;
    private int size;

    public FundviewInforHistoryAction(Context context, int id, int size, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        super(context, asyncTaskCompleteListener);
        execute(id, size);
    }

    @Override
    public void execute(Object... params) {

        this.id = (int) params[0];
        this.size = (int) params[1];
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

        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("currentId", id + "");
        params.addQueryStringParameter("pageSize", size + "");
        http.send(HttpRequest.HttpMethod.GET, Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
//                testTextView.setText(current + / + total);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                textView.setText(responseInfo.result);

                        String result = responseInfo.result;
                        ResultBean resultBean = JSON.parseObject(result,ResultBean.class);
                        List<FundviewInfor> list = JSON.parseArray(resultBean.getResult(), FundviewInfor.class);
                        mAsyncTaskCompleteListener.complete(0, 1, list);

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }
}
