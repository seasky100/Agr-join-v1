package cn.fundview.app.action.company;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.CompanyDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.company.CompanyListView;

public class  CompanyListAction extends ABaseAction {

    /**
     * 参数
     */
    private int page;
    private int pageSize;
    private boolean hasNext;
    private int total;//    企业总数
    private Map<String, String> condition;//搜索条件

    /**
     * 处理结果
     */
    private List<Company> results = null;

    public CompanyListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程  查询条件为关键字查询 key 是searcher
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

        CompanyDao companyDao = DaoFactory.getInstance(context).getCompDao();
        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id

            ResultListBean<Company> resultBean = null;
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
                        cn.fundview.app.domain.webservice.util.Constants.GET_COMP_LIST_URL), Company.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {
                        results = resultBean.getList();
                        for (Company item : results) {

                            if (item != null) {

                                Company localItem = companyDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新企业
                                    companyDao.save(item);
                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新企业信息
                                    localItem.setAreaName(item.getAreaName());
                                    localItem.setName(item.getName());
                                    localItem.setTradeName(item.getTradeName());
//                                    localItem.setLocalLogo(localItem.getLogo());//删除老图片的时候用
                                    localItem.setLogo(item.getLogo());
                                    localItem.setUpdateDate(item.getUpdateDate());
                                    companyDao.update(localItem);
                                }
                            }
                        }
                        total = resultBean.getTotalSize();
                    } else {

                        //没有从服务器取得数据的时候,从本地查询是否还有数据
                        total = (int)companyDao.countCompByCondition(condition);
                        hasNext = companyDao.countCompByCondition(condition) > page * pageSize;
                        results = DaoFactory.getInstance(context).getCompDao().getCompListByCondition(condition, page, pageSize);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    total = (int)companyDao.countCompByCondition(condition);
                    hasNext = companyDao.countCompByCondition(condition) > page * pageSize;
                    results = DaoFactory.getInstance(context).getCompDao().getCompListByCondition(condition, page, pageSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //没有从服务器取得数据的时候,从本地查询是否还有数据
                total = (int)companyDao.countCompByCondition(condition);
                hasNext = companyDao.countCompByCondition(condition) > page * pageSize;
                results = DaoFactory.getInstance(context).getCompDao().getCompListByCondition(condition, page, pageSize);
            }
        } else {

            //没有从服务器取得数据的时候,从本地查询是否还有数据
            total = (int)companyDao.countCompByCondition(condition);
            hasNext = companyDao.countCompByCondition(condition) > page * pageSize;
            results = DaoFactory.getInstance(context).getCompDao().getCompListByCondition(condition, page, pageSize);
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {

        if(total > 0) {
            if (CompanyListView.class.isInstance(webView)) {
                ((CompanyListView) webView).setTitle("企业列表(" + total + ")");
            }
        }
        // 加载企业
        if (null != results && results.size() > 0) {
            // 循环加载项目
            for (Company item : results) {

//                String oldLogoName = item.getLocalLogo();
//                if (oldLogoName != null && oldLogoName.trim() != "") {
//
//                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
//                }

                //compId, logo, compName, trade, area, updateDate, oldLogo, expoNo
                String js = JsMethod.createJs("javascript:Page.addCompany(${id}, ${logo}, ${name}, ${trade}, ${area}, ${time}, ${oldFileName},${expoNo});",
                        item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getAreaName(), item.getUpdateDate(), "", item.getExpoNo());

                System.out.println(js);
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
                webView.loadUrl("javascript:Page.loadCompFailed();");
            }
//
        }
        //
        this.closeWaitDialog();

    }

}
