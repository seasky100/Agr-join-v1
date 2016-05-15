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
 * 丰景资讯列表页 初始化,加载本地的所有未读的数据
 *
 * 返回的数据结构：
 * (1) 查询不到数据
 * {"message":"查询成功","result":[],"status":2}
 * (2) 查询到数据
 * {"message":"查询成功","result":[{"id":189,"imgUrl":"http://static.fundview.cn/thumb//userfiles/d40874fcd32f448192e45305b53ea2d1/images/user/2016/05/t01b78f96007439cde9.jpg","intro":"“我国对转基因成分检测的精度可达到被检验。","title":"转基因安全阀门","updateDate":1463105238000}],"status":2}
 */
public class InitFundviewInforListAction extends BaseAction {

    public InitFundviewInforListAction(Context context, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        super(context,asyncTaskCompleteListener);
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
        params.put("currentId", "0");
        params.put("pageSize", "1");
        client.get(Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String result = response.getString("result");

                    List<FundviewInfor> list = JSON.parseArray(result, FundviewInfor.class);
                    mAsyncTaskCompleteListener.complete(0,0,list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
