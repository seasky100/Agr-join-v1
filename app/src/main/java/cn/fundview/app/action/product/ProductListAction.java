package cn.fundview.app.action.product;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.ProductDao;
import cn.fundview.app.domain.model.Product;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.product.ProductListView;

/**
 * 产品列表 action
 */
public class ProductListAction extends ABaseAction {


    private int pageSize;
    private int page;
    private Map<String, String> condition;
    private boolean hasNext;
    private int total;      //需求总数

    /**
     * 处理结果
     */
    private List<Product> results = null;

    public ProductListAction(Context context, ABaseWebView webView) {
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

        ProductDao productDao = DaoFactory.getInstance(context).getProductDao();

        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

            // 首先从网上下载相应的json信息  uid 用户id
            ResultListBean<Product> resultBean = null;
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
                        cn.fundview.app.domain.webservice.util.Constants.GET_PRODUCT_LIST_URL), Product.class);
                if (resultBean != null) {

                    hasNext = resultBean.isHasNext();
                    if (resultBean.getList() != null && resultBean.getList().size() > 0) {

                        results = resultBean.getList();
                        for (Product item : results) {

                            if (item != null) {

                                Product localItem = productDao.getById(item.getId());
                                if (localItem == null) {

                                    //添加新需求
                                    productDao.save(item);
                                } else if (localItem.getUpdateDate() != item.getUpdateDate()) {

                                    //更新需求信息
                                    localItem.setName(item.getName());//需求名
                                    localItem.setCompName(item.getCompName());//企业名称
                                    localItem.setPrice(item.getPrice());//产品价格
                                    localItem.setUnit(item.getUnit());//产品规格
                                    localItem.setLogo(item.getLogo());
//                                    if(!localItem.getLogo().equals(item.getLogo())) {
//
//                                        //图片修改的时候
//                                        localItem.setLocaLogo(localItem.getLogo());//删除老图片的时候用
//                                        localItem.setLogo(item.getLogo());
//                                    }

                                    localItem.setUpdateDate(item.getUpdateDate());


                                    productDao.update(localItem);
                                }
                            }
                        }

                        total = resultBean.getTotalSize();
                    } else {

                        total = (int)productDao.countProductByCondition(condition);
                        hasNext = productDao.countProductByCondition(condition) > page * pageSize;
                        results = productDao.getProductListByCondition(condition, page, pageSize);
                    }
                } else {

                    //没有从服务器取得数据的时候,从本地查询是否还有数据
                    total = (int)productDao.countProductByCondition(condition);
                    hasNext = productDao.countProductByCondition(condition) > page * pageSize;
                    results = productDao.getProductListByCondition(condition, page, pageSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                total = (int)productDao.countProductByCondition(condition);
                hasNext = productDao.countProductByCondition(condition) > page * pageSize;
                results = productDao.getProductListByCondition(condition, page, pageSize);
            }
        } else {

            total = (int)productDao.countProductByCondition(condition);
            hasNext = productDao.countProductByCondition(condition) > page * pageSize;
            results = productDao.getProductListByCondition(condition, page, pageSize);
        }
    }

    /**
     * 处理结果
     */
    @Override
    protected void doHandleResult() {
        if(total > 0) {

            if(ProductListView.class.isInstance(webView)) {

                ((ProductListView) webView).setTitle("产品列表(" + total + ")");
            }
        }
        // 技术需求
        if (null != results && results.size() > 0) {
            // 循环加载项目
            for (Product item : results) {

                String oldLogoName = item.getLocaLogo();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //Page.addProduct = function(id, logo, name, unit, price, oldLogo, ownerName,lastModify)
                String js = JsMethod.createJs("javascript:Page.addProduct(${id}, ${logo}, ${name}, ${unit}, ${price},${oldLogo}, ${ownerName}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getUnit(), item.getPrice(), oldLogoName, item.getCompName(), item.getUpdateDate());

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