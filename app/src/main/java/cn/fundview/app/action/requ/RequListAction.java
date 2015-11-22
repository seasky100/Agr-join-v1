package cn.fundview.app.action.requ;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.RequDao;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.org.OrgListView;
import cn.fundview.app.view.requ.RequListView;

public class RequListAction extends ABaseAction {


    private int pageSize;
    private int page;
    private Map<String, String> condition;
    private boolean hasNext;
    private int total;      //需求总数

    /**
     * 处理结果
     */
    private List<Requ> results = null;

    public RequListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(int page, int pageSize, Map<String, String> condition) {

        this.page = page;
        this.pageSize = pageSize;
        this.condition = condition;
        this.showWaitDialog();

        handle(true);
    }

    /**
     * 执行异步处理
     */
    @Override
    protected void doAsynchHandle() {

        RequDao requDao = DaoFactory.getInstance(context).getRequDao();
        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Requ> resultBean = null;
            Map<String, String> param = new HashMap<>();
            param.put("currentPage", page + "");
            param.put("pageSize", pageSize + "");

            try {
                if (condition != null && condition.size() > 0) {

                    Set<String> keys = condition.keySet();
                    for (String key : keys) {

                        param.put(key, condition.get(key));
                    }
                }


                resultBean = JSONTools.parseList(RService.doPostSync(param,
                        cn.fundview.app.domain.webservice.util.Constants.GET_REQU_LIST_URL), Requ.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {

                        results = resultBean.getList();
                        for (Requ item : results) {

                            if (item != null) {

                                Requ localItem = requDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新需求
                                    requDao.save(item);
                                } else if (localItem.getUpdateTime() != item.getUpdateTime()) {

                                    //更新需求信息
                                    localItem.setName(item.getName());//需求名
                                    localItem.setFinPlan(item.getFinPlan());//需求的拟投入
                                    localItem.setOwnerName(item.getOwnerName());//需求的拥有者姓名(企业名)
                                    localItem.setTradeName(item.getTradeName());//需求所属行业
                                    localItem.setLogoLocalPath(localItem.getLogo());//删除老图片的时候用
                                    localItem.setLogo(item.getLogo());
                                    localItem.setOtherHj(item.getOtherHj());
                                    localItem.setHj(item.getHj());
                                    localItem.setUpdateTime(item.getUpdateTime());

                                    item.setLogoLocalPath(localItem.getLogo());//为显示图片使用

                                    requDao.update(localItem);
                                }
                            }
                        }

                        total = resultBean.getTotalSize();
                    } else {

                        total = (int) requDao.countRequByCondition(condition);
                        hasNext = requDao.countRequByCondition(condition) > page * pageSize;
                        results = requDao.getRequListByCondition(condition, page, pageSize);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    total = (int) requDao.countRequByCondition(condition);
                    hasNext = requDao.countRequByCondition(condition) > page * pageSize;
                    results = requDao.getRequListByCondition(condition, page, pageSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                total = (int) requDao.countRequByCondition(condition);
                hasNext = requDao.countRequByCondition(condition) > page * pageSize;
                results = requDao.getRequListByCondition(condition, page, pageSize);
            }
        } else {

            total = (int) requDao.countRequByCondition(condition);
            hasNext = requDao.countRequByCondition(condition) > page * pageSize;
            results = requDao.getRequListByCondition(condition, page, pageSize);
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {
        if (total > 0) {
            if (RequListView.class.isInstance(webView)) {
                ((RequListView) webView).setTitle("需求列表(" + total + ")");
            }
        }
        // 技术需求
        if (null != results && results.size() > 0) {
            // 循环加载项目
            for (Requ item : results) {

                String oldLogoName = item.getLogoLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //requId, logo, name, hj, price, oldLogo, ownerName,lastModify
                String js = JsMethod.createJs("javascript:Page.addRequ(${id}, ${logo}, ${name}, ${hj},${price},${oldLogo}, ${ownerName}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getHj(), item.getFinPlan(), oldLogoName, item.getOwnerName(), item.getUpdateTime());

                webView.loadUrl(js);
            }

            if (hasNext) {

                webView.loadUrl("javascript:Page.moreBtn('true');");
            } else {

                webView.loadUrl("javascript:Page.moreBtn('false');");
            }

        } else {

            // 加载失败
            webView.loadUrl("javascript:Page.moreBtn('false');");
            if (this.page == 1) {

                //第一页没有加载到数据的话提示加载失败
                webView.loadUrl("javascript:Page.loadFailed();");
            }

        }
        this.closeWaitDialog();

    }
}