package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.achv.AchvListAction;
import cn.fundview.app.action.company.CompanyListAction;
import cn.fundview.app.action.expert.ExpertListAction;
import cn.fundview.app.action.my.FavoriteAchvListAction;
import cn.fundview.app.action.my.FavoriteAction;
import cn.fundview.app.action.my.FavoriteRequListAction;
import cn.fundview.app.action.org.OrgListAction;
import cn.fundview.app.action.product.ProductListAction;
import cn.fundview.app.action.requ.RequListAction;
import cn.fundview.app.activity.achv.DetailActivity;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.ABaseWebView;

/**
 * 我的收藏 view
 */
public class FavoritesView extends ABaseWebView {

    private int favoritesType;
    private int page = 1;

    public FavoritesView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        favoritesType = ((Activity) context).getIntent().getIntExtra("favorite-type", 0);//1收藏的成果  2 收藏的需求
        if (favoritesType == Favorite.FAVORITE_ACHV_TYPE) {

            this.loadUrl("file:///android_asset/page/my/favorites-achv.html");

        } else {

            this.loadUrl("file:///android_asset/page/my/favorites-requ.html");
        }
    }

    @Override
    public void init() {

        if (favoritesType == Favorite.FAVORITE_ACHV_TYPE) {

            int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1;

            FavoriteAchvListAction action = new FavoriteAchvListAction(context, this);
            action.execute(page, pageSize, false);
        } else {

            int pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1;

            FavoriteRequListAction action = new FavoriteRequListAction(context, this);
            action.execute(page, pageSize, false);
        }
        page ++;
    }


    /**
     * 打开新页面
     **/
    @JavascriptInterface
    public void openPage(String page) {

        Intent intent = null;

        intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(page));

        context.startActivity(intent);
    }


    /**
     * 打开专家详细页面
     **/
    @JavascriptInterface
    public void openRequDetail(int requId, String requName, String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.requ.DetailActivity.class);
        intent.putExtra("requName", requName);
        intent.putExtra("requId", requId);
        intent.putExtra("lastModify", lastModify);
        intent.putExtra("pageSize", DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1);
        intent.putExtra("page", page);
        ((Activity) context).startActivityForResult(intent, Favorite.FAVORITE_REQU_TYPE);

    }

    /**
     * 打开专家详细页面
     **/
    @JavascriptInterface
    public void openAchvDetail(int achvId, String achvName, String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("achvName", achvName);
        intent.putExtra("achvId", achvId);
        intent.putExtra("lastModify", lastModify);
        intent.putExtra("pageSize", DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1);
        intent.putExtra("page", page);
        ((Activity) context).startActivityForResult(intent, Favorite.FAVORITE_ACHV_TYPE);
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }


    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void nextPage() {

        int pageSize = 0;
        switch (favoritesType) {

            case Favorite.FAVORITE_ACHV_TYPE:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.ACHV_ITEM_HEIGHT + 1;
                FavoriteAchvListAction favoriteAchvListAction = new FavoriteAchvListAction(context, this);
                favoriteAchvListAction.execute(page, pageSize, false);
                this.page = page + 1;
                break;
            case Favorite.FAVORITE_REQU_TYPE:
                pageSize = DensityUtil.px2dip(context, this.getHeight()) / Constants.REQU_ITEM_HEIGHT + 1;
                FavoriteRequListAction favoriteRequListAction = new FavoriteRequListAction(context, this);
                favoriteRequListAction.execute(page, pageSize, false);
                this.page = page + 1;
                break;

        }

    }
}
