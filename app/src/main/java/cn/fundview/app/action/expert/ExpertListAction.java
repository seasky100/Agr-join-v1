package cn.fundview.app.action.expert;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.ExpertDao;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.expert.ExpertListView;

public class ExpertListAction extends ABaseAction {


    private int page;
    private int pageSize;
    private Map<String, String> condition;
    private boolean hasNext;//总的页数
    private int total;      //专家总数

    /**
     * 处理结果
     */
    private List<Expert> results = null;

    public ExpertListAction(Context context, ABaseWebView webView) {
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

        ExpertDao expertDao = DaoFactory.getInstance(context).getExpertDao();
        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Expert> resultBean = null;
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
                        cn.fundview.app.domain.webservice.util.Constants.GET_EXPERT_LIST_URL), Expert.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {
                        results = resultBean.getList();
                        for (Expert item : results) {

                            if (item != null) {

                                Expert localItem = expertDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新企业
                                    expertDao.save(item);
                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新企业信息
                                    localItem.setAreaName(item.getAreaName());
                                    localItem.setName(item.getName());
                                    localItem.setTradeName(item.getTradeName());
                                    localItem.setLogo(item.getLogo());
//                                    if (localItem.getLogo() != item.getLogo()) {
//
//                                        //专家logo发生了变化
//                                        localItem.setLogoLocalPath(localItem.getLogo());//删除老图片的时候用
//                                        localItem.setLogo(item.getLogo());
//                                    }

                                    localItem.setProfessionalTitle(item.getProfessionalTitle());
                                    localItem.setUpdateDate(item.getUpdateDate());
                                    expertDao.update(localItem);

//                                    item.setLogoLocalPath(localItem.getLogoLocalPath());
                                }
                            }
                        }
                        total = resultBean.getTotalSize();
                    } else {

                        //没有从服务器返回数据
                        total = (int)expertDao.countExpertByCondition(condition);
                        hasNext = expertDao.countExpertByCondition(condition) > page * pageSize;
                        results = DaoFactory.getInstance(context).getExpertDao().getExpertListByCondition(condition, page, pageSize);
                    }
                } else {

                    //没有从服务器返回数据
                    total = (int)expertDao.countExpertByCondition(condition);
                    hasNext = expertDao.countExpertByCondition(condition) > page * pageSize;
                    results = DaoFactory.getInstance(context).getExpertDao().getExpertListByCondition(condition, page, pageSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //没有从服务器返回数据
                total = (int)expertDao.countExpertByCondition(condition);
                hasNext = expertDao.countExpertByCondition(condition) > page * pageSize;
                results = DaoFactory.getInstance(context).getExpertDao().getExpertListByCondition(condition, page, pageSize);
            }
        } else {

            //没有网络
            total = (int)expertDao.countExpertByCondition(condition);
            hasNext = expertDao.countExpertByCondition(condition) > page * pageSize;
            results = DaoFactory.getInstance(context).getExpertDao().getExpertListByCondition(condition, page, pageSize);
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {

        if(total > 0) {
            if (ExpertListView.class.isInstance(webView)) {
                ((ExpertListView) webView).setTitle("专家列表(" + total + ")");
            }
        }
        if (null != results && results.size() > 0) {

            // 循环加载项目
            for (Expert item : results) {

//                String oldLogoName = item.getLogoLocalPath();
//                if (oldLogoName != null && oldLogoName.trim() != "") {
//
//                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
//                }
                //expertId, logo, expertName, professionalTitle, trade, /*workUnit, dept, */area, updateDate, oldFileName
                String js = JsMethod.createJs("javascript:Page.addExpert(${id}, ${logo}, ${name}, ${professionalTitle}, ${trade}, ${area}, ${time},${oldFileName});",
                        item.getId(), item.getLogo(), item.getName(), item.getProfessionalTitle(), item.getTradeName(), item.getAreaName(), item.getUpdateDate(), "");

                webView.loadUrl(js);
            }

            if (hasNext) {

                webView.loadUrl("javascript:Page.moreBtn('true');");
            } else {

                webView.loadUrl("javascript:Page.moreBtn('false');");
            }

            this.closeWaitDialog();
        } else {

            // 加载失败
            this.closeWaitDialog();

            if (this.page == 1) {

                //第一页没有加载到数据的话提示加载失败
                webView.loadUrl("javascript:Page.loadExpertFailed();");
            }
            webView.loadUrl("javascript:Page.moreBtn('false');");
        }


    }

}