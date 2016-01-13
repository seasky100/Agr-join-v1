package cn.fundview.app.view.achv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

import cn.fundview.app.action.achv.DetailAction;
import cn.fundview.app.action.achv.OwnerAction;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.action.my.FavoriteAction;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 成果详细view
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：DetailView
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年6月10日 上午10:34:07
 * 修改人：lict
 * 修改时间：2015年6月10日 上午10:34:07
 * 修改备注：
 */
public class DetailView extends ABaseWebView {

    private Integer achvId;// 企业的帐号id

    public DetailView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/achv/detail.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        achvId = intent.getIntExtra("achvId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间

        //titleBarView.setTitleBar(true, "更多", name);

        DetailAction detailAction = new DetailAction(context, this);
        detailAction.execute(achvId, lastModify);

    }

    /**
     * 拨打电话
     **/
    @JavascriptInterface
    public void call(String tel) {

        CallAction callAction = new CallAction(context, this);
        callAction.parseParams(tel);
        callAction.execute();
    }

    /**
     * 查找成果对应的专家
     *
     * @param ownerId   拥有者id
     * @param ownerType 拥有者类型 1企业  2 专家
     */
    @JavascriptInterface
    public void findOwner(int ownerId, int ownerType, String ownerName) {

        OwnerAction action = new OwnerAction(context, this);
        action.execute(ownerId, ownerType, ownerName);
    }

    /**
     * 分享到微信
     **/
    @JavascriptInterface
    public void shareWX() {
        //
        // ShareProjToWXAction action = new ShareProjToWXAction(context, this);
        // action.execute(projId);
    }

    @JavascriptInterface
    public void loadheadIcon(String logo) {

        FileTools.downFile(logo, DeviceConfig.getSysPath(context) + "/fundView/data/images/achv/logo/"  + logo.substring(logo.lastIndexOf("/") + 1), this);
    }

    /**
     * 收藏/取消收藏 成果
     *
     * @param achvId         成果id
     * @param favoriteStatus 收藏状态 1收藏 0未收藏
     */
    @JavascriptInterface
    public void favorite(int achvId, int favoriteStatus) {

        FavoriteAction action = new FavoriteAction(context, this);
        action.execute(Favorite.FAVORITE_ACHV_TYPE, achvId, favoriteStatus);
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {

        String logo = responseInfo.result.getPath();
        this.loadUrl("javascript:Page.loadhead('" + logo + "');");
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
