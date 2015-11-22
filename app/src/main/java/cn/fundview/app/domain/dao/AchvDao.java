package cn.fundview.app.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Expert;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 项目名称：agr-join-v2.0.0 类名称：AchvDao 类描述： 成果dao 创建人：lict 创建时间：2015年6月8日 下午5:06:55
 * 修改人：lict 修改时间：2015年6月8日 下午5:06:55 修改备注：
 */
public class AchvDao extends BaseDao<Achv> {


    public AchvDao(Context context) {
        super(context, Achv.class);
    }


    /**
     * 成果的模糊查询
     **/
    public List<Achv> getAchvListByCondition(Map<String, String> map, int page, int pageSize) {

        try {
            String words = "";
            if (map != null) {

                words = map.get("searcher");
            }
            return dbUtils.findAll(Selector.from(Achv.class).where("name", "like", "%" + words + "%").or("trade_name", "like", "%" + words + "%").limit(pageSize).offset((pageSize - 1) * page).orderBy("update_date", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 成果条件查询总数量
     */
    public long countAchvByCondition(Map<String, String> map) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.count(Selector.from(Achv.class));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return 0;
        }
        String words = map.get("searcher");
        try {
            //成果名称、应用行业、适用原料、适用产品字段，按照修改时间倒序
            return dbUtils.count(Selector.from(Achv.class).where("name", "like", "%" + words + "%").or("trade_name", "like", "%" + words + "%"));
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
    public boolean saveOrUpdateList(List<Achv> list) {

        if (list != null && list.size() > 0) {

            for (Achv item : list) {

                if (item != null) {

                    Achv achv = getById(item.getId());
                    if (achv != null) {

                        //更新
                        item.setId(achv.getId());
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
     * 查询推荐成果 根据更新时间排序
     *
     * @param size 查询的个数
     * @return
     */
    public List<Achv> getRecommendList(int size) {

        try {
            return dbUtils.findAll(Selector.from(Achv.class).where("recommend", "=", 1).orderBy("update_date", true).offset(0).limit(size));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
