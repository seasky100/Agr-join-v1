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
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.FundviewInforDao;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.ToastUtils;
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
    private FundviewInforDao fundviewInforDao;

    public FundviewInforHistoryAction(Context context, int id, int size, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        super(context, asyncTaskCompleteListener);
        fundviewInforDao = DaoFactory.getInstance(mContext).getFundviewInforDao();
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
        List<FundviewInfor> results = fundviewInforDao.getHistory(id, size);

        mAsyncTaskCompleteListener.complete(0, 1, results);
    }

    @Override
    public void mobileHandler() {
        wifiHandler();
    }

    @Override
    public void wifiHandler() {

        // 首先从网上下载相应的json信息  uid 用户id currentId=100&pageSize=10
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("currentId", id + "");
        params.addQueryStringParameter("pageSize", size + "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
//                testTextView.setText(current + / + total);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String result = responseInfo.result;

                        ResultBean resultBean = JSON.parseObject(result, ResultBean.class);
                        List<FundviewInfor> list = JSON.parseArray(resultBean.getResult(), FundviewInfor.class);

                        //save to DB
                        if (list != null && list.size() > 0) {

                            for (FundviewInfor item : list) {

                                if (item != null) {

                                    FundviewInfor localItem = fundviewInforDao.getById(item.getId());
                                    if (localItem == null) {

                                        //添加资讯
                                        item.setRead(1);//设置历史已读
                                        item.setPublishDate(item.getUpdateDate());
                                        fundviewInforDao.save(item);
                                    } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                        item.setPublishDate(localItem.getPublishDate()); //显示用
                                        fundviewInforDao.update(localItem);
                                        item.setPublishDate(localItem.getPublishDate()); //显示用
                                    }
                                }
                            }
                            mAsyncTaskCompleteListener.complete(0, 1, list);
                            //设置所有的消息已读
                            DaoFactory.getInstance(mContext).getFundviewInforDao().setRead(id);
                        } else {

                            //没有从server获取到
                            list = fundviewInforDao.getHistory(id, size);
                            mAsyncTaskCompleteListener.complete(0, 1, list);
                        }

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ToastUtils.show(mContext, "网络连接失败");
                        List<FundviewInfor> list = fundviewInforDao.getHistory(id, size);
                        mAsyncTaskCompleteListener.complete(0, 1, list);
                    }
                });

    }
}
