package cn.fundview.app.action.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.FavoriteDao;
import cn.fundview.app.domain.dao.RequDao;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.InstallationId;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 收藏需求列表 action
 */
public class FavoriteRequListAction extends ABaseAction {

    //param
    private int pageSize;
    private int page;
    private boolean isClean;
    /**
     * 处理结果 关注的成果列表
     **/
    private List<Requ> favorites;

    public FavoriteRequListAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(int page, int pageSize, boolean isClean) {

        this.page = page;
        this.pageSize = pageSize;
        this.isClean = isClean;

        handle(true);
        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    protected void doAsynchHandle() {

        FavoriteDao favoriteDao = DaoFactory.getInstance(context).getFavoriteDao();
        RequDao requDao = DaoFactory.getInstance(context).getRequDao();
        if (PreferencesUtils.getInt(context, Constants.LOGIN_STATUS_KEY) == Constants.LOGIN_STATUS) {

            //用户已经登录
            //服务器端查询收藏列表,返回值和成果列表一样
            Map<String, String> param = new HashMap<>();
            param.put("beFavoriteType", Favorite.FAVORITE_REQU_TYPE + "");
            param.put("favoriteId", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID) + "");//关注者id

            try {
                ResultBean resultBean = JSONTools.parseResult(RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.FAVORITE_LIST_URL));

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    String favoriteListString = resultBean.getResult();
                    if (favoriteListString != null && !favoriteListString.trim().equals("")) {

                        List<Requ> requs = JSON.parseArray(favoriteListString, Requ.class);
                        if (requs != null && requs.size() > 0) {

                            //更新同步收藏表
                            for (Requ item : requs) {

                                if (item != null) {

                                    Favorite favorite = favoriteDao.findFavoriteByFavoriteIdAndFavoriteType(item.getId(), Favorite.FAVORITE_REQU_TYPE);
                                    if (favorite == null) {

                                        //添加
                                        Favorite favorite1 = new Favorite();
                                        favorite1.setDeviceId(InstallationId.getDriverId(context));
                                        favorite1.setAccountId(PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));
                                        favorite1.setAccountType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));
                                        favorite1.setFavoriteDate(item.getFavoriteTime());
                                        favorite1.setFavoriteId(item.getId());
                                        favorite1.setFavoriteType(Favorite.FAVORITE_REQU_TYPE);
                                        favoriteDao.save(favorite1);
                                    }

                                    //同步更新成果列表
                                    Requ locaItem = requDao.getById(item.getId());
                                    if (locaItem == null) {

                                        //添加
                                        requDao.save(item);
                                    } else if (locaItem.getUpdateTime() != item.getUpdateTime()) {

                                        //更新
                                        if (!item.getLogo().equals(locaItem.getLogo())) {

                                            //更新图片
                                            item.setLogoLocalPath(locaItem.getLogo());
                                        }

                                        requDao.update(item);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //从本地查询收藏的成果列表
        List<Favorite> list = favoriteDao.findFavoritesByFavoriteType(Favorite.FAVORITE_REQU_TYPE, page, pageSize);
        if (list != null && list.size() > 0) {

            if (favorites == null) {

                favorites = new ArrayList<>();
            }
            for (Favorite item : list) {

                if (item != null) {

                    favorites.add(requDao.getById(item.getFavoriteId()));
                }
            }
        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (isClean) {

            //清空当前页的数据
            webView.loadUrl("javascript:Page.cleanData();");
        }
        if (this.favorites != null && favorites.size() > 0) {

            // 循环加载项目
            for (int i = 0; i < favorites.size(); i++) {

                Requ item = favorites.get(i);
                String oldLogoName = item.getLogoLocalPath();
                if (oldLogoName != null && oldLogoName.trim() != "") {

                    oldLogoName = oldLogoName.substring(oldLogoName.lastIndexOf("/") + 1);
                }

                //requId, logo, name, hj, price, oldLogo, ownerName,lastModify
                String js = JsMethod.createJs("javascript:Page.addRequ(${id}, ${logo}, ${name}, ${hj},${price},${oldLogo}, ${ownerName}, ${time});",
                        item.getId(), item.getLogo(), item.getName(), item.getHj(), item.getFinPlan(), oldLogoName, item.getOwnerName(), item.getUpdateTime());

                webView.loadUrl(js);
            }

            if (favorites.size() < pageSize) {

                webView.loadUrl("javascript:Page.moreBtn('false');");
            } else {

                webView.loadUrl("javascript:Page.moreBtn('true');");
            }


        } else {

            //加载失败
            webView.loadUrl("javascript:Page.loadFailed();");
        }

        this.closeWaitDialog();
    }

}
