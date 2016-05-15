package cn.fundview.app.action.org;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.CompanyDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.OrgDao;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.Org;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.expert.ExpertListView;
import cn.fundview.app.view.org.OrgListView;

/**
 * Created by Administrator on 2015/10/19 0019.
 * <p/>
 * 机构列表
 */
public class OrgListAction extends ABaseAction {

    //param
    private int pageSize;
    private int page;
    private Map<String, String> condition;
    private boolean hasNext;
    private int total;      //院所总数

    private List<Org> results;

    public OrgListAction(Context context, ABaseWebView webView) {
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

        OrgDao orgDao = DaoFactory.getInstance(context).getOrgDao();
        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Org> resultBean = null;
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
                        cn.fundview.app.domain.webservice.util.Constants.GET_ORG_LIST_URL), Org.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {
                        results = resultBean.getList();
                        for (Org item : results) {

                            if (item != null) {

                                Org localItem = orgDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新机构
                                    orgDao.save(item);
                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新企业信息
                                    localItem.setArea(item.getArea());
                                    localItem.setName(item.getName());
                                    if (!localItem.getLogo().equals(item.getLogo())) {

                                        localItem.setOldLocalPath(localItem.getLogo());//删除老图片的时候用
                                        localItem.setLogo(item.getLogo());
                                    }

                                    localItem.setUpdateDate(item.getUpdateDate());
                                    orgDao.update(localItem);

                                    item.setOldLocalPath(localItem.getOldLocalPath());//用于页面删除老图片使用
                                }
                            }
                        }
                        total = resultBean.getTotalSize();
                    } else {

                        //没有从服务器取得数据的时候,从本地查询是否还有数据
                        total = (int) orgDao.countOrgByCondition(condition);
                        hasNext = orgDao.countOrgByCondition(condition) > page * pageSize;
                        results = orgDao.getOrgListByCondition(condition, page, pageSize);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    total = (int) orgDao.countOrgByCondition(condition);
                    hasNext = orgDao.countOrgByCondition(condition) > page * pageSize;
                    results = orgDao.getOrgListByCondition(condition, page, pageSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //没有从服务器取得数据的时候,从本地查询是否还有数据
                total = (int) orgDao.countOrgByCondition(condition);
                hasNext = orgDao.countOrgByCondition(condition) > page * pageSize;
                results = orgDao.getOrgListByCondition(condition, page, pageSize);
            }
        } else {

            //没有从服务器取得数据的时候,从本地查询是否还有数据
            total = (int) orgDao.countOrgByCondition(condition);
            hasNext = orgDao.countOrgByCondition(condition) > page * pageSize;
            results = orgDao.getOrgListByCondition(condition, page, pageSize);
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {

        if (total > 0) {
            if (OrgListView.class.isInstance(webView)) {
                ((OrgListView) webView).setTitle("院所列表(" + total + ")");
            }
        }
        if (null != results && results.size() > 0) {

            // 循环加载项目
            for (Org item : results) {

                String oldLogoName = item.getOldLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }
                //orgId, logo, name, trade, area, expoNo, updateDate, oldFileName
                String js = JsMethod.createJs("javascript:Page.addOrg(${id}, ${logo}, ${name}, ${area}, ${expoNo},${${time},${oldFileName});",
                        item.getId(), item.getLogo(), item.getName(), item.getArea(), item.getExpoNo(), item.getUpdateDate(), oldLogoName);

                webView.loadUrl(js);
            }

            if (hasNext) {

                webView.loadUrl("javascript:Page.moreBtn('true');");
            } else {

                webView.loadUrl("javascript:Page.moreBtn('false');");
            }

        } else {

            // 加载失败
            this.closeWaitDialog();

            if (this.page == 1) {

                //第一页没有加载到数据的话提示加载失败
                webView.loadUrl("javascript:Page.loadFailed();");
            }
            webView.loadUrl("javascript:Page.moreBtn('false');");
        }
        this.closeWaitDialog();
    }

}
