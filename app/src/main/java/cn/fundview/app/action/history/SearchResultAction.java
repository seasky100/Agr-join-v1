package cn.fundview.app.action.history;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.view.ABaseWebView;

/**
 * 高级搜索action
 */
public class SearchResultAction extends ABaseAction {

    /**
     * 参数
     **/
    private int pageSize;
    private int page;
    private int totalPage;

    private Map<String, String> map;
    /**
     * 执行结果
     **/
    private List<Expert> expertList = null;
    private List<Company> compList = null;
    private List<Achv> achvList = null;
    private List<Requ> requList = null;

    public SearchResultAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Map<String, String> map, int page, int pageSize) {

        this.map = map;// 条件类型 0 行业 1地区 2只能搜索
        this.page = page;
        this.pageSize = pageSize;
        handle(true);
        this.showWaitDialog();
    }

    @Override
    protected void doAsynchHandle() {

        int type = Integer.parseInt(map.get("type"));
        if (type == 0) {

            // 查询专家 areaName 地区名称 tradeName 行业名称 orderName
            // 排序字段名(默认为时间排序：update_time,可选参数：人气排序：recommend_num )
            expertList = RService.searchExpert(map, page - 1, pageSize);
            if (null != expertList && expertList.size() > 0) {
                // 保存服务器
                DaoFactory.getInstance(context).getExpertDao().saveOrUpdateList(expertList);
            }
            // // TODO 判断网络是否连接 连接了 到数据库中获取 否则返回null
            expertList = DaoFactory.getInstance(context).getExpertDao().getExpertListByCondition(map, page, pageSize);
        } else if (type == 1) {

            // 查询成果
            achvList = RService.searchAchv(map, page - 1, pageSize);
            if (null != achvList && achvList.size() > 0) {

                // 保存服务器
                DaoFactory.getInstance(context).getAchvDao().saveOrUpdateList(achvList);
            }
            // // TODO 判断网络是否连接 连接了 到数据库中获取 否则返回null
            achvList = DaoFactory.getInstance(context).getAchvDao().getAchvListByCondition(map, page, pageSize);
        } else if (type == 2) {

//            // 查询企业 关键字匹配企业名称和行业名称  按照修改时间倒序
//            compList = RService.searchComp(map, page - 1, pageSize);
//            if (null != compList && compList.size() > 0) {
//                // 保存服务器
//                DaoFactory.getInstance(context).getCompDao().saveOrUpdateList(compList);
//            }
            // // TODO 判断网络是否连接 连接了 到数据库中获取 否则返回null
            compList = DaoFactory.getInstance(context).getCompDao().getCompListByCondition(map, page, pageSize);
        } else if (type == 3) {

            // 查询需求
            requList = RService.searchRequ(map, page - 1, pageSize);
            if (null != requList && requList.size() > 0) {
                // 保存服务器
                DaoFactory.getInstance(context).getRequDao().saveOrUpdateList(requList);
            }
            // // TODO 判断网络是否连接 连接了 到数据库中获取 否则返回null
            requList = DaoFactory.getInstance(context).getRequDao().getRequListByCondition(map, page, pageSize);
        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        int type = Integer.parseInt(map.get("type"));
        webView.loadUrl("javascript:Page.cleanData(" + map.get("type") + ")");
        if (type == 0) {

            expertList = getResults4();
            if (null != expertList && expertList.size() > 0) {
                // 循环加载项目
                for (Expert item : expertList) {

                    String oldLogoName = item.getLogoLocalPath();
                    if (oldLogoName != null && oldLogoName.trim() != "") {

                        oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                    }
                    //expertId, logo, expertName, professionalTitle, trade, /*workUnit, dept, */area, updateDate, oldFileName
                    String js = JsMethod.createJs("javascript:Page.addExpert(${id}, ${logo}, ${name}, ${professionalTitle}, ${trade}, ${area}, ${time},${oldFileName});",
                            item.getId(), item.getLogo(), item.getName(), item.getProfessionalTitle(), item.getTradeName(), item.getAreaName(), item.getUpdateDate(), item.getLogoLocalPath());

                    webView.loadUrl(js);
                }

                totalPage = 10;
                if (page < totalPage) {

                    webView.loadUrl("javascript:Page.moreBtn('true');");
                } else {

                    webView.loadUrl("javascript:Page.moreBtn('false');");
                }
                webView.loadUrl("javascript:Page.hintError(" + false + ");");
            } else {

                // 加载失败
                webView.loadUrl("javascript:Page.hintError(" + true + ");");
            }

        } else if (type == 1) {

            // 填充成果
            achvList = getResults1();
            if (null != achvList && achvList.size() > 0) {
                // 循环加载项目

                for (Achv item : achvList) {

                    String oldLogoName = item.getOldLocalPath();
                    if (oldLogoName != null && oldLogoName.trim() != "") {

                        oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                    }

                    //achvId, logo, name, trade, price, ownerName, lastModify

                    String js = JsMethod.createJs(
                            "javascript:Page.addItem(${id}, ${logo}, ${name}, ${trade}, ${price}, ${ownerName}, ${time});",
                            item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getPrice(), item.getOwnerName(), item.getUpdataDate());

                    webView.loadUrl(js);
                }

                totalPage = 10;
                if (page < totalPage) {

                    webView.loadUrl("javascript:Page.moreBtn('true');");
                } else {

                    webView.loadUrl("javascript:Page.moreBtn('false');");
                }

                webView.loadUrl("javascript:Page.hintError(" + false + ");");
            } else {

                // 加载失败
                webView.loadUrl("javascript:Page.hintError(" + true + ");");
            }
        } else if (type == 2) {

            // 填充企业
            compList = getResults2();
            if (null != compList && compList.size() > 0) {
                // 循环加载项目
                for (Company item : compList) {

                    String oldLogoName = item.getLocalLogo();
                    if (oldLogoName != null && oldLogoName.trim() != "") {

                        oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                    }

                    //compId, logo, compName, trade, area, updateDate, oldLogo, expoNo
                    String js = JsMethod.createJs("javascript:Page.addCompany(${id}, ${logo}, ${name}, ${trade}, ${area}, ${time}, ${oldFileName},${expoNo});",
                            item.getId(), item.getLogo(), item.getName(), item.getTradeName(), item.getAreaName(), item.getUpdateDate(), oldLogoName, item.getExpoNo());

                    System.out.println(js);
                    webView.loadUrl(js);
                }

                totalPage = 10;
                if (page < totalPage) {

                    webView.loadUrl("javascript:Page.moreBtn('true');");
                } else {

                    webView.loadUrl("javascript:Page.moreBtn('false');");
                }
                webView.loadUrl("javascript:Page.hintError(" + false + ");");
            } else {

                // 加载失败
                webView.loadUrl("javascript:Page.hintError(" + true + ");");
            }
        } else if (type == 3) {

            // 填充需求
            requList = getResults3();
            if (null != requList && requList.size() > 0) {
                // 循环加载项目
                for (Requ item : requList) {

                    String oldLogoName = item.getLogoLocalPath();
                    if (oldLogoName != null && oldLogoName.trim() != "") {

                        oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                    }

                    //requId, logo, name, hj, price, oldLogo, ownerName,lastModify
                    String js = JsMethod.createJs("javascript:Page.addRequ(${id}, ${logo}, ${name}, ${hj},${price},${oldLogo}, ${ownerName}, ${time});",
                            item.getId(), item.getLogo(), item.getName(), item.getHj(), item.getFinPlan(), item.getLogoLocalPath(), item.getOwnerName(), item.getUpdateTime());

                    webView.loadUrl(js);
                }

                totalPage = 10;
                if (page < totalPage) {

                    webView.loadUrl("javascript:Page.moreBtn('true');");
                } else {

                    webView.loadUrl("javascript:Page.moreBtn('false');");
                }
                webView.loadUrl("javascript:Page.hintError(" + false + ");");
            } else {

                // 加载失败
                webView.loadUrl("javascript:Page.hintError(" + true + ");");
            }
        }

        this.closeWaitDialog();
    }


    private List<Achv> getResults1() {

        List<Achv> list = new ArrayList<>();
        for (int i = 0; i < this.pageSize; i++) {

            Achv achv = new Achv();
            achv.setName("成果" + i);
            achv.setTradeName("果蔬加工行业");
            achv.setCreateDate(new Date().getTime());
            achv.setOwnerName("专家01" + i);
            achv.setPrice(10000);
            achv.setUpdataDate(new Date().getTime());
            achv.setLogo("http://static.fundview.cn/thumb/achv/img/20140903//2014090312421482354_104_78.jpg");
            achv.setId(100 + i);

            list.add(achv);
        }

        return list;
    }

    private List<Company> getResults2() {

        List<Company> list = new ArrayList<>();
        for (int i = 0; i < this.pageSize; i++) {

            Company company = new Company();
            company.setName("企业" + i);
            company.setAddr("企业地址" + i);
            company.setAreaName("山东,济南,历下" + i);
            company.setAttention(100);
            company.setExpoNo("12341");
            company.setLogo("http://static.fundview.cn/thumb/comp/logo/20140929/2014092909563543921_104_78.png");
            company.setRecommendNum(123);
            company.setTradeName("果蔬加工");
            company.setUpdateDate(new Date().getTime());
            company.setId(100 + i);

            list.add(company);
        }

        return list;
    }

    private List<Requ> getResults3() {

        List<Requ> list = new ArrayList<>();
        for (int i = 0; i < this.pageSize; i++) {

            Requ requ = new Requ();
            requ.setName("需求" + i);
            requ.setTradeName("果蔬加工行业");
            requ.setCreateDate(new Date().getTime());
            requ.setOwnerName("企业01" + i);
            requ.setFinPlan(10000);
            requ.setUpdateTime(new Date().getTime());
            requ.setLogo("http://static.fundview.cn/thumb/comp/logo/20141009/201410090906358676_104_78.jpg");
            requ.setId(100 + i);
            requ.setHj("需求环节" + i);
            list.add(requ);
        }

        return list;
    }

    private List<Expert> getResults4() {

        List<Expert> list = new ArrayList<>();
        for (int i = 0; i < this.pageSize; i++) {

            Expert expert = new Expert();
            expert.setName("专家" + i);
            expert.setWorkUnit("农业部");
            expert.setAreaName("山东,济南,历下" + i);
            expert.setAttention(100);
            expert.setDept("果蔬加工部门");
            expert.setProfessionalTitle("教授");
            expert.setLogo("http://static.fundview.cn/thumb/comp/logo/20140929/2014092909563543921_104_78.png");
            expert.setRecommendNum(123);
            expert.setTradeName("果蔬加工");
            expert.setUpdateDate(new Date().getTime());
            expert.setId(100 + i);

            list.add(expert);
        }

        return list;
    }
}
