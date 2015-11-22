package cn.fundview.app.action.msg;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.FundviewInforDao;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 丰景资讯 异步加载action  根据id进行查询
 */
public class LoadFundviewInforAction {

    /**
     * param
     */
    private int id;
    private Context context;
    private FundviewInforDao fundviewInforDao;

    /**
     * 处理结果
     */
    private String result;

    public LoadFundviewInforAction(Context context) {

    }

    public void execute(int id) {

        this.id = id;
        fundviewInforDao = DaoFactory.getInstance(context).getFundviewInforDao();
        doAsynchHandle();
    }

    /**
     * 执行异步处理 根据id查询服务器
     */
    protected void doAsynchHandle() {

        RequestParams params = new RequestParams();

        params.addQueryStringParameter("currentId", id + "");
        params.addQueryStringParameter("pageSize", "1");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL,
                params,
                new RequestCallBack<String>() {

                    private ResultBean resultBean = null;
                    private List<FundviewInfor> results;

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        result = responseInfo.result;
                        try {
                            resultBean = JSONTools.parseResult(result);
                            if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                                //请求成功
                                if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {

                                    results = JSON.parseArray(resultBean.getResult(), FundviewInfor.class);
                                    if (results != null && results.size() > 0) {
                                        for (FundviewInfor item : results) {

                                            if (item != null) {

                                                FundviewInfor localItem = fundviewInforDao.getById(item.getId());
                                                if (localItem == null) {

                                                    //添加资讯
                                                    item.setPublishDate(item.getUpdateDate());
                                                    fundviewInforDao.save(item);
                                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                                    //更新资讯
                                                    if (localItem.getLogo() != null && !localItem.getLogo().equals(item.getLogo())) {

                                                        //资讯图片有更新
                                                        item.setLogoLocalPath(localItem.getLogo());
                                                    }

                                                    item.setRead(localItem.getRead());
                                                    //第一次加载设置发布时间
                                                    if (localItem.getPublishDate() == 0)
                                                        item.setPublishDate(item.getUpdateDate());

                                                    fundviewInforDao.update(item);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        result = "error";
                    }
                });
//
    }

}
