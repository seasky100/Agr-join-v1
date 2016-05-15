package cn.fundview.app.action.home;

import android.content.Context;
import android.support.v7.internal.widget.ActivityChooserView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.CompanyDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.ExpertDao;
import cn.fundview.app.domain.dao.ProductDao;
import cn.fundview.app.domain.dao.RequDao;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.Product;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.FundProject;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 首页action
 */
public class IndexAction extends ABaseAction {

    // result
    private List<Company> companyList;// 企业列表
    private List<Expert> expertList;// 专家
    private List<Achv> achvList;// 优质成果
    private List<Requ> requList;// 技术需求
    private List<Product> productList;// 产品列表
    private List<FundProject> fundProjectList;//融资项目

    //param
    private boolean firstIncome;


    public IndexAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    public void execute(boolean firstIncome) {

        this.firstIncome = firstIncome;
        // 执行异步操作
        handle(true);
    }

    /**
     * 异步处理
     * 成果的推荐处理  可以从server下载数据的时候,使用server 端的数据,不再从本地查询,只是更新/保存本地数据
     * 无法连接server的时候,从本地查询推荐的成果
     **/
    protected void doAsynchHandle() {

        // 查询网络成功,将数据添加或更新到本地,显示
        // 失败 查询本地.成功,显示 失败,提示查询失败

        //推荐企业
        CompanyDao companyDao = DaoFactory.getInstance(context).getCompDao();
        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            Map<String, String> param = new HashMap<>();
            param.put("currentPage", 1 + "");
            param.put("pageSize", 2 + "");

            try {

                ResultListBean<Company> resultBean = null;
                resultBean = JSONTools.parseList(RService.doPostSync(param,
                        cn.fundview.app.domain.webservice.util.Constants.GET_HOME_COMPANY_LIST_URL), Company.class);
                if (resultBean != null) {
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {

                        companyList = resultBean.getList();
                        for (Company item : companyList) {

                            if (item != null) {

                                Company localItem = companyDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新企业
                                    item.setRecommendNum(1);
                                    companyDao.save(item);

                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新企业信息
                                    localItem.setAreaName(item.getAreaName());
                                    localItem.setName(item.getName());
                                    localItem.setTradeName(item.getTradeName());
                                    localItem.setLocalLogo(localItem.getLogo());//删除老图片的时候用
                                    localItem.setLogo(item.getLogo());
                                    localItem.setRecommendNum(1);
                                    localItem.setUpdateDate(item.getUpdateDate());
                                    companyDao.update(localItem);
                                }
                            }
                        }
                    } else {

                        companyList = DaoFactory.getInstance(context).getCompDao().getRecommendList(2);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    companyList = DaoFactory.getInstance(context).getCompDao().getRecommendList(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //没有从服务器取得数据的时候,从本地查询是否还有数据
                companyList = DaoFactory.getInstance(context).getCompDao().getRecommendList(2);
            }
        } else {

            //没有从服务器取得数据的时候,从本地查询是否还有数据
            companyList = DaoFactory.getInstance(context).getCompDao().getRecommendList(2);
        }



        ExpertDao expertDao = DaoFactory.getInstance(context).getExpertDao();
        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Expert> resultBean = null;
            Map<String, String> param = new HashMap<>();
            param.put("currentPage", "1");
            param.put("pageSize", "2");

            try {

                resultBean = JSONTools.parseList(RService.doPostSync(param,
                        Constants.GET_HOME_EXPERT_LIST_URL), Expert.class);
                if (resultBean != null) {
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {
                        expertList = resultBean.getList();
                        for (Expert item : expertList) {

                            if (item != null) {

                                Expert localItem = expertDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新企业
                                    item.setRecommendNum(1);
                                    expertDao.save(item);
                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新企业信息
                                    localItem.setAreaName(item.getAreaName());
                                    localItem.setName(item.getName());
                                    localItem.setTradeName(item.getTradeName());
                                    localItem.setRecommendNum(1);
                                    if (localItem.getLogo() != item.getLogo()) {

                                        //专家logo发生了变化
                                        localItem.setLogoLocalPath(localItem.getLogo());//删除老图片的时候用
                                        localItem.setLogo(item.getLogo());
                                    }

                                    localItem.setProfessionalTitle(item.getProfessionalTitle());
                                    localItem.setUpdateDate(item.getUpdateDate());
                                    expertDao.update(localItem);

                                    item.setLogoLocalPath(localItem.getLogoLocalPath());
                                }
                            }
                        }
                    } else {

                        //没有从服务器返回数据
                        expertList = DaoFactory.getInstance(context).getExpertDao().getRecommendList(2);
                    }
                } else {

                    //没有从服务器返回数据
                    expertList = DaoFactory.getInstance(context).getExpertDao().getRecommendList(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //没有从服务器返回数据
                expertList = DaoFactory.getInstance(context).getExpertDao().getRecommendList(2);
            }
        } else {

            //没有网络
            expertList = DaoFactory.getInstance(context).getExpertDao().getRecommendList(2);
        }



        Map<String, String> param = new HashMap<>();
        param.put("pageSize", "2");

        try {
            ResultBean resultBean = null;
            if (NetWorkUtils.checkNetwork(context)) {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_HOME_ACHV_LIST_URL));
            }
            if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                JSONObject jsonObject = JSON.parseObject(resultBean.getResult());
                if (jsonObject != null) {

                    String jsonResult = jsonObject.getString("resultList");
                    if (!StringUtils.isBlank(jsonResult)) {

                        achvList = JSON.parseArray(jsonResult, Achv.class);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        AchvDao achvDao = DaoFactory.getInstance(context).getAchvDao();

        if (achvList != null && achvList.size() > 0) {

            //保存/更新成果
            for (Achv item : achvList) {

                Achv localAchv = achvDao.getById(item.getId());
                if (localAchv != null) {

                    //更新本地
                    localAchv.setUpdataDate(item.getUpdataDate());
                    if (localAchv.getLogo() != item.getLogo()) {

                        //本地的logo 和服务端的logo 不一致的时候,更新logo
                        item.setOldLocalPath(localAchv.getLogo());
                        localAchv.setOldLocalPath(localAchv.getLogo());
                        localAchv.setLogo(item.getLogo());
                    }
                    localAchv.setName(item.getName());
                    localAchv.setPrice(item.getPrice());
                    localAchv.setOwnerName(item.getOwnerName());
                    localAchv.setTradeName(item.getTradeName());
                    localAchv.setRecommend(1);//设置为推荐成果
                    achvDao.update(localAchv);//更新本地
                } else {

                    item.setRecommend(1);//设置为推荐成果
                    achvDao.save(item);//保存成果
                }
            }
        } else {

            achvList = achvDao.getRecommendList(2);
        }


        //需求
        Map<String, String> paramRequ = new HashMap<>();
        paramRequ.put("pageSize", "2");

        try {
            ResultBean resultBeanRequ = null;
            if (NetWorkUtils.checkNetwork(context)) {
                resultBeanRequ = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_HOME_REQU_LIST_URL));
            }
            if (resultBeanRequ != null && resultBeanRequ.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                JSONObject jsonObject = JSON.parseObject(resultBeanRequ.getResult());
                if (jsonObject != null) {

                    String jsonResult = jsonObject.getString("resultList");
                    if (!StringUtils.isBlank(jsonResult)) {

                        requList = JSON.parseArray(jsonResult, Requ.class);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        RequDao requDao = DaoFactory.getInstance(context).getRequDao();

        if (requList != null && requList.size() > 0) {

            //保存/更新成果
            for (Requ item : requList) {

                Requ localRequ = requDao.getById(item.getId());
                if (localRequ != null) {

                    //更新本地
                    localRequ.setUpdateTime(item.getUpdateTime());
                    if (localRequ.getLogo() != item.getLogo()) {

                        //本地的logo 和服务端的logo 不一致的时候,更新logo
                        item.setLogoLocalPath(localRequ.getLogo());
                        localRequ.setLogoLocalPath(localRequ.getLogo());
                        localRequ.setLogo(item.getLogo());
                    }
                    localRequ.setName(item.getName());
                    localRequ.setFinPlan(item.getFinPlan());
                    localRequ.setOwnerName(item.getOwnerName());
                    localRequ.setTradeName(item.getTradeName());
                    localRequ.setRecommend(1);//设置为推荐成果
                    requDao.update(localRequ);//更新本地
                } else {

                    item.setRecommend(1);//设置为推荐成果
                    requDao.save(item);//保存成果
                }
            }
        } else {

            requList = requDao.getRecommendList(2);
        }


        //产品
        ProductDao productDao = DaoFactory.getInstance(context).getProductDao();
        // 检查网络
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Product> resultBean = null;
            Map<String, String> productParam = new HashMap<>();
            productParam.put("currentPage", "1");
            productParam.put("pageSize", "2");

            try {

                resultBean = JSONTools.parseList(RService.doPostSync(param,
                        Constants.GET_HOME_PRODUCT_LIST_URL), Product.class);
                if (resultBean != null) {
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {

                        productList = resultBean.getList();
                        for (Product item : productList) {

                            if (item != null) {

                                Product localItem = productDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新需求
                                    item.setRecommend(1);
                                    productDao.save(item);
                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新需求信息
                                    localItem.setName(item.getName());//需求名
                                    localItem.setCompName(item.getCompName());//企业名称
                                    localItem.setPrice(item.getPrice());//产品价格
                                    localItem.setUnit(item.getUnit());//产品规格
                                    localItem.setRecommend(1);
                                    if(!localItem.getLogo().equals(item.getLogo())) {

                                        //图片修改的时候
                                        localItem.setLocaLogo(localItem.getLogo());//删除老图片的时候用
                                        localItem.setLogo(item.getLogo());
                                    }

                                    localItem.setUpdateDate(item.getUpdateDate());


                                    productDao.update(localItem);
                                }
                            }
                        }
                    } else {

                        productList = productDao.getRecommendList(2);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    productList = productDao.getRecommendList(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                productList = productDao.getRecommendList(2);
            }
        } else {

            productList = productDao.getRecommendList(2);
        }

        //融资项目
        if (NetWorkUtils.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<FundProject> fundProjectResultListBean = null;
            Map<String, String> fundProjectParam = new HashMap<>();
            fundProjectParam.put("currentPage", 1 + "");
            fundProjectParam.put("pageSize", 2 + "");

            try {
                fundProjectResultListBean = JSONTools.parseList(RService.doPostSync(param,
                        cn.fundview.app.domain.webservice.util.Constants.GET_FUND_PROJ_LIST_URL), FundProject.class);

                fundProjectList = fundProjectResultListBean.getList();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 执行处理结果
     **/

    protected void doHandleResult() {

        String[] imgs = new String[10];
        imgs[0] = "http://static.fundview.cn/thumb/ad/banner1.jpg";
        imgs[1] = "http://static.fundview.cn/thumb/ad/banner2.jpg";
        imgs[2] = "http://static.fundview.cn/thumb/ad/banner3.jpg";

        String js = "";
        if (NetWorkUtils.checkNetwork(context)) {

            webView.loadUrl("javascript:Page.clearImg();");
            for (String path : imgs) {

                if (path != null && path.trim() != "") {
                    js = JsMethod.createJs("javascript:Page.addImg(${path});", path);
                    System.out.println(js);
                    webView.loadUrl(js);
                }
            }
//
            if (firstIncome) {

                js = JsMethod.createJs("javascript:Page.initSwiper();");
                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.hideSlideImg();");
        }


//        清空数据
        webView.loadUrl("javascript:Page.clearData();");

        //企业
        if (null != companyList && companyList.size() > 0) {

            // 循环加载项目
            for (Company item : companyList) {

                String oldLogoName = item.getLocalLogo();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                js = JsMethod.createJs("javascript:Page.addCompany(${id}, ${logo}, ${name}, ${trade}, ${area}, ${time}, ${oldFileName},${expoNo});",
                        item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getAreaName(), item.getUpdateDate(), oldLogoName, item.getExpoNo());

                System.out.println(js);
                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadCompanyFailed();");
        }

        //专家
        if (null != expertList && expertList.size() > 0) {

            // 循环加载项目
            for (Expert item : expertList) {

                String oldLogoName = item.getLogoLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //expertId, logo, expertName, professionalTitle, trade, /*workUnit, dept, */area, updateDate, oldFileName
                js = JsMethod.createJs("javascript:Page.addExpert(${id}, ${logo}, ${name}, ${professionalTitle}, ${trade}, ${area}, ${time},${oldFileName});",
                        item.getId(), item.getLogo(), item.getName(), item.getProfessionalTitle(), item.getTradeName(), item.getAreaName(), item.getUpdateDate(), oldLogoName);

                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadExpertFailed();");
        }


        if (null != achvList && achvList.size() > 0) {

            // 循环加载项目
            for (Achv item : achvList) {

                String oldLogoName = item.getOldLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                js = JsMethod.createJs(
                        "javascript:Page.addAchv(${id}, ${logo}, ${name}, ${trade}, ${price}, ${ownerName}, ${oldLogo}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getPrice(), item.getOwnerName(), oldLogoName, item.getUpdataDate());
                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadAchvFailed();");
        }

        //加载需求
        if (null != requList && requList.size() > 0) {
            // 循环加载项目
            for (Requ item : requList) {

                String oldLogoName = item.getLogoLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //requId, logo, name, hj, price, oldLogo, ownerName,lastModify
                js = JsMethod.createJs("javascript:Page.addRequ(${id}, ${logo}, ${name}, ${hj},${price},${oldLogo}, ${ownerName}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getHj(), item.getFinPlan(), oldLogoName, item.getOwnerName(), item.getUpdateTime());

                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadRequFailed();");
        }

        //产品
        if (null != productList && productList.size() > 0) {

            // 循环加载项目
            for (Product item : productList) {

                String oldLogoName = item.getLocaLogo();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //Page.addProduct = function(id, logo, name, unit, price, oldLogo, ownerName,lastModify)
                js = JsMethod.createJs("javascript:Page.addProduct(${id}, ${logo}, ${name}, ${unit}, ${price},${oldLogo}, ${ownerName}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getUnit(), item.getPrice(), oldLogoName, item.getCompName(), item.getUpdateDate());

                webView.loadUrl(js);
            }

        } else {

            webView.loadUrl("javascript:Page.loadProductFailed();");
        }



        if (null != fundProjectList && fundProjectList.size() > 0) {

            // 循环加载项目
            for (FundProject item : fundProjectList) {

                //(id, logo, name, jd, compName, invest)
                js = JsMethod.createJs(
                        "javascript:Page.addItem(${id}, ${logo}, ${name}, ${jd}, ${compName}, ${invest});",
                        item.getId(), item.getLogo(), item.getProjName(), item.getJdName(), item.getName(), item.getInvest());
                webView.loadUrl(js);
            }
        } else {

            webView.loadUrl("javascript:Page.loadProjFailed();");
        }


        this.closeWaitDialog();
    }

}
