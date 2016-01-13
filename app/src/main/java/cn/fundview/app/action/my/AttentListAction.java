package cn.fundview.app.action.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AttentUserDao;
import cn.fundview.app.domain.dao.CompanyDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.ExpertDao;
import cn.fundview.app.domain.model.AttentUser;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 *
 */
public class AttentListAction extends ABaseAction {

    //param
    private int beAttentType; // 被关注者角色类型 1 专家  2企业
    private Integer uid;
    private int pageSize;
    private int page;
    private boolean isCLean;//标示是否清空当前页面

    /**
     * 处理结果
     **/
    private List<Expert> experts;
    private List<Company> companys;

    public AttentListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(int beAttentType, Integer uid, int page, int pageSize, boolean isClean) {

        this.beAttentType = beAttentType;
        this.uid = uid;
        this.page = page;
        this.pageSize = pageSize;
        this.isCLean = isClean;
        handle(true);
        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    protected void doAsynchHandle() {



        AttentUserDao attentUserDao = DaoFactory.getInstance(context).getAttentUserDao();
        CompanyDao companyDao = DaoFactory.getInstance(context).getCompDao();//企业Dao
        ExpertDao expertDao = DaoFactory.getInstance(context).getExpertDao();//专家Dao
        //如果网络获取失败，则获取本地的
        if (NetWorkConfig.checkNetwork(context)) {

            //网络连接正常
            Map<String, String> param = new HashMap<>();
            param.put("accountId", uid + "");//登录用户id
            param.put("attentType", beAttentType + "");//被关注着的类型 1企业  2专家
            param.put("currentPage", page + "");//当前页数
            param.put("pageSize", pageSize + "");//每页显示的条数
            ResultBean resultBean;
            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.MY_ATTENTION_LIST_URL));
                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    if (this.beAttentType == 1) {

                        //关注企业
                        companys = JSON.parseArray(resultBean.getResult(), Company.class);

                        if (companys != null && companys.size() > 0) {

                            //服务器端返回了关注列表
                            long startAttentTime = companys.get(0).getAttentDate();
                            long endAttentTime = companys.get(companys.size() - 1).getAttentDate();

                            //本地根据关注时间查询,替换为服务器端的列表
                            attentUserDao.deleteAttentListBy(this.uid, startAttentTime, endAttentTime, this.beAttentType);
                            //将服务器端的数据存储到本地
                            for (Company item : companys) {

                                AttentUser attentUser = new AttentUser();
                                attentUser.setBeAttentId(item.getId());//被关注者id
                                attentUser.setAttentTime(item.getAttentDate());//关注时间
                                attentUser.setAttentId(PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));//设置关注者id
                                attentUser.setAttentType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));//设置关注者类型
                                attentUser.setBeAttentType(this.beAttentType);//被关注者类型
                                attentUserDao.save(attentUser);

                                //将企业更新到本地
                                Company localItem = companyDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新企业item
                                    companyDao.save(item);
                                } else {

                                    localItem.setUpdateDate(item.getUpdateDate());
                                    localItem.setAreaName(item.getAreaName());
                                    localItem.setLocalLogo(localItem.getLogo());
                                    localItem.setLogo(item.getLogo());
                                    localItem.setName(item.getName());
                                    localItem.setTradeName(item.getTradeName());
                                    companyDao.update(localItem);
                                }
                            }
                        }
                    } else if (this.beAttentType == 2) {

                        //关注专家
                        experts = JSON.parseArray(resultBean.getResult(), Expert.class);

                        if (experts != null && experts.size() > 0) {

                            //服务器端返回了关注列表
                            long startAttentTime = experts.get(0).getAttentDate();
                            long endAttentTime = experts.get(experts.size() - 1).getAttentDate();

                            //本地根据关注时间查询,替换为服务器端的列表
                            attentUserDao.deleteAttentListBy(this.uid, startAttentTime, endAttentTime, this.beAttentType);
                            //将服务器端的数据存储到本地
                            for (Expert item : experts) {

                                AttentUser attentUser = new AttentUser();
                                attentUser.setBeAttentId(item.getId());//被关注者id
                                attentUser.setAttentTime(item.getAttentDate());//关注时间
                                attentUser.setAttentId(PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));//设置关注者id
                                attentUser.setAttentType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));//设置关注者类型
                                attentUser.setBeAttentType(this.beAttentType);//被关注者类型
                                attentUserDao.save(attentUser);

                                //将企业更新到本地
                                Expert localItem = expertDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新企业item
                                    expertDao.save(item);
                                } else {

                                    localItem.setUpdateDate(item.getUpdateDate());
                                    localItem.setAreaName(item.getAreaName());
                                    localItem.setLogoLocalPath(localItem.getLogo());
                                    localItem.setLogo(item.getLogo());
                                    localItem.setName(item.getName());
                                    localItem.setTradeName(item.getTradeName());
                                    expertDao.update(localItem);
                                }
                            }

                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            //从本地查询
            List<AttentUser> attentUsers = attentUserDao.getListByAttentUser(uid, this.beAttentType, page, pageSize);
            if (attentUsers != null && attentUsers.size() > 0) {

                if (this.beAttentType == 1) {

                    //企业
                    for (AttentUser item : attentUsers) {

                        if (companys == null) {

                            companys = new ArrayList<>();
                        }

                        Company company = companyDao.getById(item.getBeAttentId());
                        if (company != null) {

                            company.setAttentDate(item.getAttentTime());
                            companys.add(company);
                        }
                    }
                } else if (this.beAttentType == 2) {

                    //专家
                    for (AttentUser item : attentUsers) {

                        if (experts == null) {

                            experts = new ArrayList<>();
                        }

                        Expert expert = expertDao.getById(item.getBeAttentId());
                        if (expert != null) {

                            expert.setAttentDate(item.getAttentTime());
                            experts.add(expert);
                        }
                    }
                }
            }

        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {


        if (isCLean) {

            webView.loadUrl("javascript:Page.cleanData();");
        }

        if (this.beAttentType == 1) {

            //企业列表
            if (this.companys != null && companys.size() > 0) {

                for (Company item : companys) {

                    String oldFileName = "";
                    if (item.getLocalLogo() != null && item.getLocalLogo().trim() != "") {

                        oldFileName = item.getLocalLogo().substring(item.getLocalLogo().lastIndexOf("/") + 1);
                    }
                    String js = JsMethod.createJs("javascript:Page.addItem(${id}, ${name}, ${logo}, ${trade}, ${type}, ${attentTime}, ${area}, ${oldLogo}, ${updateDate});",
                            item.getId(), item.getName(), item.getLogo(), item.getTradeName(), this.beAttentType, item.getAttentDate(), item.getAreaName(), oldFileName, item.getUpdateDate());

                    webView.loadUrl(js);


                }

                if (companys.size() < pageSize) {

                    webView.loadUrl(JsMethod.createJs("javascript:Page.setMore(${flag});", false));
                }
                this.companys.clear();
                this.closeWaitDialog();
            } else {

                this.closeWaitDialog();
                webView.loadUrl("javascript:Page.hintFindCompanyFailed();");
                if (companys.size() < pageSize) {

                    webView.loadUrl(JsMethod.createJs("javascript:Page.setMore(${flag});", false));
                }
            }
        } else {

            //专家列表
            if (this.experts != null && experts.size() > 0) {

                for (Expert item : experts) {

                    //id, name, logo, trade, type, attentTime, area, oldLogoName, updateDate, container
                    String oldFileName = "";
                    if (item.getLogoLocalPath() != null && item.getLogoLocalPath().trim() != "") {

                        oldFileName = item.getLogoLocalPath().substring(item.getLogoLocalPath().lastIndexOf("/") + 1);
                    }
                    String js = JsMethod.createJs("javascript:Page.addItem(${id}, ${name}, ${logo}, ${trade}, ${type}, ${attentTime}, ${area}, ${oldLogo}, ${updateDate});",
                            item.getId(), item.getName(), item.getLogo(), item.getTradeName(), this.beAttentType, item.getAttentDate(), item.getAreaName(), oldFileName, item.getUpdateDate());

                    webView.loadUrl(js);


                }

                if (experts.size() < pageSize) {

                    webView.loadUrl(JsMethod.createJs("javascript:Page.setMore(${flag});", false));
                }
                this.experts.clear();
                this.closeWaitDialog();
            } else {

                this.closeWaitDialog();
                webView.loadUrl("javascript:Page.hintFindExpertFailed();");

                if (experts.size() < pageSize) {

                    webView.loadUrl(JsMethod.createJs("javascript:Page.setMore(${flag});", false));
                }
            }
        }
    }
}
