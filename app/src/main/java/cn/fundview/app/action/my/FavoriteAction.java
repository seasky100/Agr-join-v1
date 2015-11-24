package cn.fundview.app.action.my;

import android.content.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.Installation;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * Created by Administrator on 2015/10/23 0023.
 * 收藏action
 */
public class FavoriteAction extends ABaseAction {


    /**
     * 参数
     */
    private int favoriteStatus;//收藏状态  1收藏  2取消收藏
    private int type;//被收藏的类型 1 成果 2需求
    private int favoriteId;//被收藏 的id

    /**
     * 处理结果
     **/
    private boolean result = false;

    public FavoriteAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 1为关注 2为取消关注
     **/
    public void execute(int type, int favoriteId, int favoriteStatus) {

        this.type = type;
        this.favoriteId = favoriteId;
        this.favoriteStatus = favoriteStatus;

        handle(true);
    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        Integer accountId = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);//当前登录用户的id
        String deviceId = Installation.getDriverId(context);//设备id

        Map<String, String> param = new HashMap<>();
        if (this.favoriteStatus == 1) {

            //取消收藏
            result = DaoFactory.getInstance(context).getFavoriteDao().deleteFavoriteByDeviceIdAndFavoriteId(deviceId, this.favoriteId, this.type);
            //通知服务器端
            if (PreferencesUtils.getInt(context, Constants.LOGIN_STATUS_KEY) == Constants.LOGIN_STATUS) {

                //用户已经登录
                param.put("favoriteId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");           //我的id
                param.put("favoriteType", PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY) + "");   //我的类型
                param.put("beFavoriteId", favoriteId + "");                                                    //被收藏id
                param.put("beFavoriteType", type + "");                                                  //被收藏id
                try {
                    ResultBean resultBean = JSONTools.parseResult(RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.CANCEL_FAVORITE_URL));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {

            //添加收藏
            Favorite favorite = new Favorite();

            favorite.setDeviceId(deviceId);
            if (PreferencesUtils.getInt(context, Constants.LOGIN_STATUS_KEY) == Constants.LOGIN_STATUS) {

                //已经登录
                favorite.setAccountId(accountId);
                favorite.setAccountType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));
                //同时server
                try {
                    param.put("favoriteId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");           //我的id
                    param.put("favoriteType", PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY) + "");   //我的类型
                    param.put("beFavoriteId", favoriteId + "");                                                    //被收藏id
                    param.put("beFavoriteType", type + "");
                    ResultBean resultBean = JSONTools.parseResult(RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.ADD_FAVORITE_URL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                favorite.setAccountId(0);
                favorite.setAccountType(0);
            }
            favorite.setFavoriteId(this.favoriteId);
            favorite.setFavoriteType(this.type);
            favorite.setFavoriteDate(new Date().getTime());

            result = DaoFactory.getInstance(context).getFavoriteDao().save(favorite);

        }
    }

    /**
     * 处理结果
     **/
    protected void doHandleResult() {


        String js = "";

        if (this.favoriteStatus == 1) {

            js = JsMethod.createJs("javascript:Page.showHintDialog(${message},${result},${type});", "取消收藏成功", result, this.favoriteStatus);
        } else {

            js = JsMethod.createJs("javascript:Page.showHintDialog(${message},${result},${type});", "收藏成功", result, this.favoriteStatus);
        }
        webView.loadUrl(js);
    }
}
