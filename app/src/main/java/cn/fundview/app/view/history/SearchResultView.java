package cn.fundview.app.view.history;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.achv.AchvListAction;
import cn.fundview.app.action.company.CompanyListAction;
import cn.fundview.app.action.expert.ExpertListAction;
import cn.fundview.app.action.history.SearchResultAction;
import cn.fundview.app.action.history.SearchHistoryAction;
import cn.fundview.app.action.org.OrgListAction;
import cn.fundview.app.action.product.ProductListAction;
import cn.fundview.app.action.requ.RequListAction;
import cn.fundview.app.activity.company.CompanyProductDetailActivity;
import cn.fundview.app.activity.expert.DetailActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.SearchHistory;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.SearchTitleBar;

/**
 * 搜索历史 result view
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：SearchResultView
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月25日 上午11:46:49
 * 修改人：lict
 * 修改时间：2015年6月25日 上午11:46:49
 * 修改备注：
 */
public class SearchResultView extends ABaseWebView {

    private int page = 1;
    private int pageSize;
    private Map<String, String> map;//高级搜索的条件集合
    private SearchTitleBar searchTitleBar;

    public SearchResultView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        Intent intent = ((Activity) context).getIntent();
        int type = intent.getIntExtra("type", 0);
        switch (type) {//0专家 1成果  2 企业 3 需求

            case 0:
                this.loadUrl("file:///android_asset/page/expert/list.html");
                break;
            case 1:
                this.loadUrl("file:///android_asset/page/achv/list.html");
                break;
            case 2:
                this.loadUrl("file:///android_asset/page/company/list.html");
                break;
            case 3:
                this.loadUrl("file:///android_asset/page/requ/list.html");
                break;
            case 4:
                this.loadUrl("file:///android_asset/page/organ/list.html");
                break;
            case 5:
                this.loadUrl("file:///android_asset/page/product/list.html");
                break;
        }

    }

    @Override
    public void init() {

        searchTitleBar = this.getTitleBarView();
        Intent intent = ((Activity) context).getIntent();
        String key = intent.getStringExtra("key");
        int type = intent.getIntExtra("type", 0);
        map = new HashMap<>();
        map.put("searcher", key);
        // 执行搜索action
        switch (type) {

            case 0:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.EXPERT_ITEM_HEIGHT + 1;
                ExpertListAction expertListAction = new ExpertListAction(context, this);
                expertListAction.execute(page, pageSize, map);
                break;
            case 1:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1;
                AchvListAction achvListAction = new AchvListAction(context, this);
                achvListAction.execute(page, pageSize, map);
                break;
            case 2:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.COMPANY_ITEM_HEIGHT + 1;
                CompanyListAction companyListAction = new CompanyListAction(context, this);
                companyListAction.execute(page, pageSize, map);
                break;
            case 3:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1;
                RequListAction requListAction = new RequListAction(context, this);
                requListAction.execute(page, pageSize, map);
                break;
            case 4:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ORG_ITEM_HEIGHT + 1;
                OrgListAction orgListAction = new OrgListAction(context, this);
                orgListAction.execute(page, pageSize, map);
                break;

            case 5:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.PRODUCT_ITEM_HEIGHT + 1;
                ProductListAction productListAction = new ProductListAction(context, this);
                productListAction.execute(page, pageSize, map);
                break;
        }
        page++;
    }


    /**
     * 打开专家详细
     **/
    @JavascriptInterface
    public void openExpertDetail(final int expertId, final String expertName, final String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("expertName", expertName);
        intent.putExtra("expertId", expertId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开企业详细
     **/
    @JavascriptInterface
    public void openCompDetail(final int compId, final String compName, final String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.company.DetailActivity.class);
        intent.putExtra("compName", compName);
        intent.putExtra("compId", compId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开成果详细
     **/
    @JavascriptInterface
    public void openAchvDetail(final int achvId, final String achvName, final String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.achv.DetailActivity.class);
        intent.putExtra("achvName", achvName);
        intent.putExtra("achvId", achvId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开需求详细
     **/
    @JavascriptInterface
    public void openRequDetail(final int requId, final String requName, final String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.requ.DetailActivity.class);
        intent.putExtra("requName", requName);
        intent.putExtra("requId", requId);
        intent.putExtra("lastModify",lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开需求详细
     **/
    @JavascriptInterface
    public void openOrgDetail(final int orgId, final String orgName, final String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.org.DetailActivity.class);
        intent.putExtra("orgName", orgName);
        intent.putExtra("orgId", orgId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开产品详细
     **/
    @JavascriptInterface
    public void openProductDetail(final int productId, final String productName, final String lastModify) {

        Intent intent = new Intent(context, CompanyProductDetailActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("productId", productId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 执行搜索
     **/
    @Override
    public void onClickRight() {

//		String key = searchTitleBar.getKey();
//		int type = searchTitleBar.getType();
//		// 保存 到本地
//		SearchHistory item = new SearchHistory();
//		item.setWords(key);
//		item.setType(type);
//
//		SearchHistory oldItem = DaoFactory.getInstance(context).getSearchHistoryDao().getSearchHistoryByNameAndType(key,type);
//		if(oldItem == null) {
//			DaoFactory.getInstance(context).getSearchHistoryDao()
//					.save(item);
//		}
//		map.put("key", key);
//		SearchResultAction action = new SearchResultAction(context, this);
//		action.execute(map,1,pageSize);
//		page = 2;


        //跳转到对应的搜索历史页面
        Intent intent = new Intent(context, SearchHistoryActivity.class);
        intent.putExtra("type", ((Activity) context).getIntent().getIntExtra("type", 0));
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void nextPage() {

        Intent intent = ((Activity) context).getIntent();
        int type = intent.getIntExtra("type", 0);
        int pageSize = 0;
        switch (type) {//0专家 1成果  2 企业 3 需求

            case 0:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.EXPERT_ITEM_HEIGHT + 1;
                ExpertListAction action = new ExpertListAction(context, this);
                action.execute(page, pageSize, map);
                this.page = page + 1;
                break;
            case 1:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1;
                AchvListAction achvListAction = new AchvListAction(context, this);
                achvListAction.execute(page, pageSize, map);
                this.page = page + 1;
                break;
            case 2:

                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.COMPANY_ITEM_HEIGHT + 1;
                CompanyListAction companyListAction = new CompanyListAction(context, this);
                companyListAction.execute(page, pageSize, map);
                this.page = page + 1;
                break;
            case 3:

                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1;
                RequListAction requListAction = new RequListAction(context, this);
                requListAction.execute(page, pageSize, map);
                this.page = page + 1;
                break;
            case 4:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ORG_ITEM_HEIGHT + 1;
                OrgListAction orgListAction = new OrgListAction(context, this);
                orgListAction.execute(page, pageSize, map);
                this.page = page + 1;
                break;
            case 5:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.PRODUCT_ITEM_HEIGHT + 1;
                ProductListAction productListAction = new ProductListAction(context, this);
                productListAction.execute(page, pageSize, map);
                this.page = page + 1;
                break;
        }

    }
}
