package cn.fundview.app.action.expert;

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
import cn.fundview.app.domain.dao.ExpertDao;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * Created by Administrator on 2016/6/2.
 * 特约专家 列表action
 * 参数:
 * pageSize 每页显示的条数
 * currentPage 当前第几页
 * searcher 搜索关键字
 */
public class GuestExpertListAction extends BaseAction {

    private int pageSize = 10;
    private int currentPage = 1;
    private String searcher;

    private ExpertDao expertDao;

    public GuestExpertListAction(Context context, int currentPage, int pageSize, String searcher, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        super(context, asyncTaskCompleteListener);
        expertDao = DaoFactory.getInstance(context).getExpertDao();
        execute(pageSize, currentPage, searcher);
    }

    @Override
    public void execute(Object... params) {

        this.pageSize = (int) params[0];
        this.currentPage = (int) params[1];
        this.searcher = (String) params[2];
        super.execute(params);
    }

    @Override
    public void disConnectedHandler() {

        super.disConnectedHandler();
        //本地没有未读的消息,查询本地的最新的2条资讯, 从列表中进入的
        List<Expert> results = expertDao.findGuestExpert(pageSize, currentPage, searcher);
        if (null != results && results.size() > 0) {

            mAsyncTaskCompleteListener.complete(0, currentPage, results);
        }
    }


    @Override
    public void mobileHandler() {
        wifiHandler();
    }

    @Override
    public void wifiHandler() {

        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("currentPage", currentPage + "");
        params.addQueryStringParameter("pageSize", pageSize + "");
        if (!StringUtils.isBlank(searcher)) {
            params.addQueryStringParameter("searcher", searcher);
        }
        http.send(HttpRequest.HttpMethod.GET, Constants.GUEST_EXPERT_LIST_URL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String result = responseInfo.result;

                        ResultListBean resultListBean = JSON.parseObject(result, ResultListBean.class);
                        List<Expert> list = JSON.parseArray(resultListBean.getResultList(), Expert.class);

                        //save to DB
                        if (list != null && list.size() > 0) {

                            for (Expert item : list) {
                                Expert localItem = expertDao.getById(item.getId());
                                if (localItem == null) {

                                    expertDao.save(item);
                                } else {

                                    expertDao.update(item);
                                }
                            }

                            mAsyncTaskCompleteListener.complete(0, currentPage, list);
                        } else {

                            //没有从server获取到
                            list = expertDao.findGuestExpert(pageSize, currentPage, searcher);
                            mAsyncTaskCompleteListener.complete(0, currentPage, list);
                        }

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ToastUtils.show(mContext, "网络连接失败");
                        List<Expert> list = expertDao.findGuestExpert(pageSize, currentPage, searcher);
                        mAsyncTaskCompleteListener.complete(0, currentPage, list);
                    }
                });
    }

}
