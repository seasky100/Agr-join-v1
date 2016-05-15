package cn.fundview.app.action.achv;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.achv.AchvListView;

public class AchvListAction extends ABaseAction {

    private int pageSize;
    private int page;
    private Map<String, String> condition;
    private boolean hasNext;
    private int total;


    /**
     * 处理结果
     */
    private List<Achv> results = null;

    public AchvListAction(Context context, ABaseWebView webView) {
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

        AchvDao achvDao = DaoFactory.getInstance(context).getAchvDao();
        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Achv> resultBean = null;
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
                        cn.fundview.app.domain.webservice.util.Constants.GET_ACHV_LIST_URL), Achv.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {

                        results = resultBean.getList();
                        for (Achv item : results) {

                            if (item != null) {

                                Achv localItem = achvDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新成果
                                    achvDao.save(item);
                                } else if (localItem.getUpdataDate() != item.getUpdataDate()) {

                                    //更新成果信息
                                    localItem.setName(item.getName());
                                    localItem.setPrice(item.getPrice());
                                    localItem.setOwnerName(item.getOwnerName());
                                    localItem.setTradeName(item.getTradeName());
//                                    localItem.setOldLocalPath(localItem.getLogo());//删除老图片的时候用
                                    localItem.setLogo(item.getLogo());
                                    localItem.setUpdataDate(item.getUpdataDate());

//                                    item.setOldLocalPath(localItem.getLogo());//为显示图片使用

                                    achvDao.update(localItem);
                                }
                            }
                        }

                        total = resultBean.getTotalSize();
                    } else {

                        total = (int) achvDao.countAchvByCondition(condition);
                        hasNext = achvDao.countAchvByCondition(condition) > page * pageSize;
                        results = DaoFactory.getInstance(context).getAchvDao().getAchvListByCondition(condition, page, pageSize);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    total = (int) achvDao.countAchvByCondition(condition);
                    hasNext = achvDao.countAchvByCondition(condition) > page * pageSize;
                    results = DaoFactory.getInstance(context).getAchvDao().getAchvListByCondition(condition, page, pageSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                total = (int) achvDao.countAchvByCondition(condition);
                hasNext = achvDao.countAchvByCondition(condition) > page * pageSize;
                results = DaoFactory.getInstance(context).getAchvDao().getAchvListByCondition(condition, page, pageSize);
            }
        } else {

            total = (int) achvDao.countAchvByCondition(condition);
            hasNext = achvDao.countAchvByCondition(condition) > page * pageSize;
            results = DaoFactory.getInstance(context).getAchvDao().getAchvListByCondition(condition, page, pageSize);
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {

        if (total > 0) {
            if (AchvListView.class.isInstance(webView)) {
                ((AchvListView) webView).setTitle("成果列表(" + total + ")");
            }
        }
        if (null != results && results.size() > 0) {

            // 循环加载项目
            for (Achv item : results) {

                String oldLogoName = item.getOldLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //achvId, logo, name, trade, price, ownerName, old, lastModify

                String js = JsMethod.createJs(
                        "javascript:Page.addItem(${id}, ${logo}, ${name}, ${trade}, ${price}, ${ownerName}, ${oldLogo}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getPrice(), item.getOwnerName(), oldLogoName, item.getUpdataDate());

                webView.loadUrl(js);
            }


            if (hasNext) {

                webView.loadUrl("javascript:Page.moreBtn('true');");
            } else {

                webView.loadUrl("javascript:Page.moreBtn('false');");
            }


        } else {

            //加载失败
            webView.loadUrl("javascript:Page.moreBtn('false');");
            if (this.page == 1) {

                //第一页没有加载到数据的话提示加载失败
                webView.loadUrl("javascript:Page.loadFailed();");
            }
        }

        this.closeWaitDialog();

    }
}