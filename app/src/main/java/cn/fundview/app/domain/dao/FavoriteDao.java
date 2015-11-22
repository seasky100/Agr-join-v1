package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.model.FundviewInfor;

/**
 * 收藏Dao
 */
public class FavoriteDao extends BaseDao<Favorite> {

    public FavoriteDao(Context context) {

        super(context, Favorite.class);
    }

    /**
     * 根据设备id 和被收藏的id进行删除
     *
     * @param deviceId     　设备id
     * @param favoriteId   被收藏的id
     * @param favoriteType 被收藏的类型
     * @return boolean
     */
    public boolean deleteFavoriteByDeviceIdAndFavoriteId(String deviceId, int favoriteId, int favoriteType) {

        try {
            dbUtils.delete(Favorite.class, WhereBuilder.b("device_id", "=", deviceId).and("favorite_id", "=", favoriteId).and("favorite_type", "=", favoriteType));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 根据被收藏者id 和类型查询
     *
     * @param favoriteId
     * @param favoriteType
     * @return
     */
    public Favorite findFavoriteByFavoriteIdAndFavoriteType(int favoriteId, int favoriteType) {

        try {
            return dbUtils.findFirst(Selector.from(Favorite.class).where("favorite_id", "=", favoriteId).and("favorite_type", "=", favoriteType));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询指定类型的收藏列表  分页
     *
     * @param favoriteType
     * @return
     */
    public List<Favorite> findFavoritesByFavoriteType(int favoriteType, int page, int pageSize) {

        try {
            return dbUtils.findAll(Selector.from(Favorite.class).where("favorite_type", "=", favoriteType).orderBy("favorite_date", true).offset((page - 1) * pageSize).limit(pageSize));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

}
