package cn.fundview.app.action.msg;

import android.content.Context;

import com.alibaba.fastjson.JSON;

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
 * 丰景资讯列表页 初始化,加载本地的所有未读的数据
 */
public class InitFundviewInforListAction extends ABaseAction {

    /**
     * param
     */
    private int id;
    private int size;
    /**
     * 处理结果
     */
    private List<FundviewInfor> results = null;

    public InitFundviewInforListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(int id, int size) {

        this.id = id;
        this.size = size;
        handle(true);
    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        FundviewInforDao fundviewInforDao =  DaoFactory.getInstance(context).getFundviewInforDao();
        results = fundviewInforDao.getAllUnRead(size);
        if (null == results || results.size() == 0) {

            //本地没有未读的消息,查询本地的最新的2条资讯, 从列表中进入的
            results = DaoFactory.getInstance(context).getFundviewInforDao().getLastest(2);
            if (null == results || results.size() == 0) {

                //本地没有任何的资讯信息,默认从服务器拉取最新的一条
                if (NetWorkConfig.checkNetwork(context)) {

                    // 首先从网上下载相应的json信息  uid 用户id currentId=100&pageSize=10
                    ResultBean resultBean = null;
                    Map<String, String> param = new HashMap<>();
                    param.put("currentId", "0");
                    param.put("pageSize", "1");

                    try {

                        resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_FUNDVIEW_INFOR_LIST_HISTORY_URL));
                        if (resultBean != null && resultBean.getStatus() == Constants.REQUEST_SUCCESS) {

                            //请求成功
                            if (resultBean.getResult() != null && !resultBean.getResult().trim().equals("")) {

                                results = JSON.parseArray(resultBean.getResult(), FundviewInfor.class);
                                for (FundviewInfor item : results) {

                                    if (item != null) {

                                        FundviewInfor localItem = fundviewInforDao.getById(item.getId());
                                        if (localItem == null) {

                                            //添加资讯
                                            item.setRead(1);//设置历史已读
                                            item.setPublishDate(item.getUpdateDate());
                                            fundviewInforDao.save(item);
                                        } else {

                                            //更新资讯
                                            if (!item.getLogo().equals(localItem.getLogo())) {

                                                //资讯图片有更新
                                                item.setLogoLocalPath(localItem.getLogo());
                                            }

                                            item.setPublishDate(localItem.getPublishDate()); //显示用
                                            fundviewInforDao.update(item);
                                        }
                                        item.setPublishDate(localItem.getPublishDate()); //显示用
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {

                        //解析数据失败
                        e.printStackTrace();
                    }
                }
            }
        }

        //设置所有的消息已读
        DaoFactory.getInstance(context).getFundviewInforDao().setRead(id);
    }

    /**
     * 处理结果
     */
    protected void doHandleResult() {

        String js = "";
        if (null != results && results.size() > 0) {

            for (int i = 0; i < results.size(); i++) {

                FundviewInfor infor = results.get(i);
                if (i == results.size() - 1) {

                    //当前查询的第一条
                    webView.loadUrl("javascript:Page.setId(" + infor.getId() + ");");//将当前查询的最小id 传递到页面,再由页面传递到view,解决view 取不到页面中展示的最小的id
                }
                js = JsMethod.createJs(
                        "javascript:Page.addInfor" + "(${id}, ${title}, ${logo}, ${introduction}, ${deliverTime}, ${publishTime}, ${updateDate});", infor.getId(),
                        infor.getTitle(), infor.getLogo(), infor.getIntroduction(), infor.getPublishDate(), infor.getPublishDate(), infor.getUpdateDate());

                System.out.println(js);
                webView.loadUrl(js);
            }

            webView.loadUrl("javascript:Page.init();");
        }

    }
}
