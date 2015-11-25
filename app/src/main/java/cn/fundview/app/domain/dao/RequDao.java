package cn.fundview.app.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Requ;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 项目名称：agr-join-v2.0.0
 * 类名称：TechRequDao
 * 类描述： 技术需求Dao
 * 创建人：lict
 * 创建时间：2015年6月9日 上午8:41:57
 * 修改人：lict
 * 修改时间：2015年6月9日 上午8:41:57
 * 修改备注：
 */
public class RequDao extends BaseDao<Requ> {

    public RequDao(Context context) {

        super(context, Requ.class);
    }

    //根据关键字查询
    public List<Requ> getRequListByCondition(Map<String, String> map, int page, int pageSize) {

        if (map != null) {

            String key = map.get("searcher");
            try {
                return dbUtils.findAll(Selector.from(Requ.class).where("name", "like", "%" + key + "%").
                        orderBy("update_date", true).
                        limit(pageSize).
                        offset(pageSize * (page - 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {

            try {
                return dbUtils.findAll(Selector.from(Requ.class).
                        limit(pageSize).
                        offset(pageSize * (page - 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    /**
     * 需求条件查询总数量
     */
    public long countRequByCondition(Map<String, String> map) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.count(Selector.from(Requ.class));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return 0;
        }
        String words = map.get("searcher");
        try {
            return dbUtils.count(Selector.from(Requ.class).where("name", "like", "%" + words + "%"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 保存/更新成果列表
     *
     * @param list 操作数据
     * @return true 操作成功
     */
    public boolean saveOrUpdateList(List<Requ> list) {

        if (list != null && list.size() > 0) {

            for (Requ item : list) {

                if (item != null) {

                    Requ requ = getById(item.getId());
                    if (requ != null) {

                        //更新
                        item.setId(requ.getId());
                        update(item);
                    } else {

                        //保存
                        save(item);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 查询推荐需求 根据更新时间排序
     *
     * @param size 查询的个数
     * @return
     */
    public List<Requ> getRecommendList(int size) {

        try {
            return dbUtils.findAll(Selector.from(Requ.class).where("recommend", "=", 1).orderBy("update_date", true).offset(0).limit(size));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
