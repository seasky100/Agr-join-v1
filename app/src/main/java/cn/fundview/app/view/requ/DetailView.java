package cn.fundview.app.view.requ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.achv.OwnerAction;
import cn.fundview.app.action.my.FavoriteAction;
import cn.fundview.app.action.requ.DetailAction;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.view.ABaseWebView;

/**
 * 技术需求详细 view
 */
public class DetailView extends ABaseWebView {


    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.loadUrl("file:///android_asset/page/requ/detail.html");
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        DetailAction action = new DetailAction(context, this);

        Intent intent = ((Activity) context).getIntent();
        int requId = intent.getIntExtra("requId", 0);
        String lastModify = intent.getStringExtra("lastModify");
        action.execute(requId, lastModify);
    }

    /**
     * 查找需求对应的专家
     *
     * @param ownerId   拥有者id
     * @param ownerName 拥有者名称
     */
    @JavascriptInterface
    public void findOwner(int ownerId, String ownerName) {

        OwnerAction action = new OwnerAction(context, this);
        action.execute(ownerId, 1, ownerName);
    }

    /**
     * 收藏/取消收藏 需求
     *
     * @param requId         需求id
     * @param favoriteStatus 收藏状态 1收藏 0未收藏
     */
    @JavascriptInterface
    public void favorite(int requId, int favoriteStatus) {

        FavoriteAction action = new FavoriteAction(context, this);
        action.execute(Favorite.FAVORITE_REQU_TYPE, requId, favoriteStatus);
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }

    @Override
    public void onClickLeftBtn() {

        ((Activity) context).setResult(Activity.RESULT_OK, ((Activity) context).getIntent());
        super.onClickLeftBtn();
    }

    @Override
    public void onKeyBack() {

        ((Activity) context).setResult(Activity.RESULT_OK, ((Activity) context).getIntent());
        super.onKeyBack();
    }
}
