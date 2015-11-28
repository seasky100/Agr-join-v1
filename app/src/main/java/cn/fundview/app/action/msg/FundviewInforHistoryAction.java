package cn.fundview.app.action.msg;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class FundviewInforHistoryAction extends cn.fundview.app.action.ABaseAction {

    /**
     * 参数
     */
    private int size;
    private int id;
    private cn.fundview.app.domain.model.FundviewInfor last;
    /**
     * 处理结果
     */
    private List<FundviewInfor> results = null;

    public FundviewInforHistoryAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     *
     * @param id   页面中的最小id
     * @param size 拉取的资讯条数
     */
    public void execute(int id, int size) {

        this.size = size;
        this.id = id - 1;
        handle(true);

    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        if (this.id != 0) {
            FundviewInforDao fundviewInforDao = DaoFactory.getInstance(context).getFundviewInforDao();
            // 检查网络
            if (NetWorkConfig.checkNetwork(context)) {

                // 首先从网上下载相应的json信息  uid 用户id currentId=100&pageSize=10
                ResultBean resultBean = null;
                Map<String, String> param = new HashMap<>();
                param.put("currentId", id + "");
                param.put("pageSize", size + "");

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
                                    } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                        //更新资讯
                                        if (!localItem.getLogo().equals(item.getLogo())) {

                                            //资讯图片有更新
                                            item.setLogoLocalPath(localItem.getLogo());
                                        }

                                        item.setPublishDate(localItem.getPublishDate()); //显示用
                                        fundviewInforDao.update(localItem);
                                    }
                                    item.setPublishDate(localItem.getPublishDate()); //显示用
                                }
                            }
                        } else {

                            //从服务器端获得的数据是空
                            results = fundviewInforDao.getHistory(id, size);
                        }
                    } else {

                        //没有从服务器取得数据的时候,从本地查询是否还有数据
                        results = fundviewInforDao.getHistory(id, size);
                    }
                } catch (Exception e) {

                    //解析数据失败
                    e.printStackTrace();
                    results = fundviewInforDao.getHistory(id, size);
                }
            } else {

                //手机没有网络,从本地取数据
                results = fundviewInforDao.getHistory(id, size);
            }
        }
    }

    /**
     * 处理结果
     */
    protected void doHandleResult() {

        if (null != results && results.size() > 0) {

            // 循环加载项目
            for (int i = 0; i < results.size(); i++) {

                FundviewInfor infor = results.get(i);
                if (i == results.size() - 1) {

                    //当前查询的第一条
                    webView.loadUrl("javascript:Page.setId(" + infor.getId() + ");");//将当前查询的最小id 传递到页面,再由页面传递到view,解决view 取不到页面中展示的最小的id
                }

                String js = JsMethod.createJs("javascript:Page.addHistoryData(${id}, ${title}, ${logo}, ${introduction}, ${deliveryTime}, ${publishTime}, ${updateDate});", infor.getId(), infor.getTitle(),
                        infor.getLogo(), infor.getIntroduction(), infor.getPublishDate(), infor.getPublishDate(), infor.getUpdateDate());

                webView.loadUrl(js);
            }

        }

        webView.loadUrl("javascript:Page.loadHistoryComplete();");

        /*else {

            webView.loadUrl("javascript:Page.onNoHistoryData();");
        }*/
    }
}
