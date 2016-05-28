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
 * 丰景资讯列表页 初始化,加载本地的所有未读的数据
 * <p/>
 * 返回的数据结构：
 * (1) 查询不到数据
 * {"message":"查询成功","result":[],"status":2}
 * (2) 查询到数据
 * {"message":"查询成功","result":[{"id":189,"imgUrl":"http://static.fundview.cn/thumb//userfiles/d40874fcd32f448192e45305b53ea2d1/images/user/2016/05/t01b78f96007439cde9.jpg","intro":"“我国对转基因成分检测的精度可达到被检验。","title":"转基因安全阀门","updateDate":1463105238000}],"status":2}
 */
public class InitFundviewInforListAction extends BaseAction {

    private int id;
    private int size;

    private FundviewInforDao fundviewInforDao;

    public InitFundviewInforListAction(Context context, int id, int size, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        super(context, asyncTaskCompleteListener);
        fundviewInforDao = DaoFactory.getInstance(context).getFundviewInforDao();
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

        super.disConnectedHandler();
        //本地没有未读的消息,查询本地的最新的2条资讯, 从列表中进入的
        List<FundviewInfor> results = DaoFactory.getInstance(mContext).getFundviewInforDao().getLastest(1);
        if (null != results && results.size() > 0) {

            mAsyncTaskCompleteListener.complete(0, 0, results);
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

                        ResultBean resultBean = JSON.parseObject(result, ResultBean.class);
                        List<FundviewInfor> list = JSON.parseArray(resultBean.getResult(), FundviewInfor.class);

                        //save to DB
                        if (list != null && list.size() > 0) {

                            for (FundviewInfor item : list) {
                                FundviewInfor localItem = fundviewInforDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加资讯
                                    item.setRead(1);//设置历史已读
                                    item.setPublishDate(item.getUpdateDate());
                                    fundviewInforDao.save(item);
                                } else {

                                    item.setPublishDate(localItem.getPublishDate()); //显示用
                                    fundviewInforDao.update(item);
                                }
                                item.setPublishDate(localItem.getPublishDate()); //显示用
                            }

                            mAsyncTaskCompleteListener.complete(0, 0, list);
                            //设置所有的消息已读
                            DaoFactory.getInstance(mContext).getFundviewInforDao().setRead(id);
                        } else {

                            //没有从server获取到
                            list = DaoFactory.getInstance(mContext).getFundviewInforDao().getLastest(1);
                            mAsyncTaskCompleteListener.complete(0, 0, list);
                        }

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ToastUtils.show(mContext,"网络连接失败");
                        List<FundviewInfor> list = DaoFactory.getInstance(mContext).getFundviewInforDao().getLastest(1);
                        mAsyncTaskCompleteListener.complete(0, 0, list);
                    }
                });
    }


//    /**
//     * 执行异步处理
//     */
//    protected void doAsynchHandle() {
//
//        FundviewInforDao fundviewInforDao =  DaoFactory.getInstance(context).getFundviewInforDao();
//        results = fundviewInforDao.getAllUnRead(size);
//        if (null == results || results.size() == 0) {
//
//            //本地没有未读的消息,查询本地的最新的2条资讯, 从列表中进入的
//            results = DaoFactory.getInstance(context).getFundviewInforDao().getLastest(2);
//            if (null == results || results.size() == 0) {
//
//                //本地没有任何的资讯信息,默认从服务器拉取最新的一条
//                if (NetWorkUtils.checkNetwork(context)) {
//
//                    // 首先从网上下载相应的json信息  uid 用户id currentId=100&pageSize=10
//                    ResultBean resultBean = null;
//                    Map<String, String> param = new HashMap<>();
//                    param.put("currentId", "0");
//                    param.put("pageSize", "1");
//
//                    try {
//
//                        resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL));
//                        if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {
//
//                            //请求成功
//                            if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {
//
//                                results = JSON.parseArray(resultBean.getResult(), FundviewInfor.class);
//                                for (FundviewInfor item : results) {
//
//                                    if (item != null) {
//
//                                        FundviewInfor localItem = fundviewInforDao.getById(item.getId());
//                                        if (localItem == null) {
//
//                                            //添加资讯
//                                            item.setRead(1);//设置历史已读
//                                            item.setPublishDate(item.getUpdateDate());
//                                            fundviewInforDao.save(item);
//                                        } else {
//
//                                            //更新资讯
//                                            if (!item.getLogo().equals(localItem.getLogo())) {
//
//                                                //资讯图片有更新
//                                                item.setLogoLocalPath(localItem.getLogo());
//                                            }
//
//                                            item.setPublishDate(localItem.getPublishDate()); //显示用
//                                            fundviewInforDao.update(item);
//                                        }
//                                        item.setPublishDate(localItem.getPublishDate()); //显示用
//                                    }
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//
//                        //解析数据失败
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        //设置所有的消息已读
//        DaoFactory.getInstance(context).getFundviewInforDao().setRead(id);
//    }

}
