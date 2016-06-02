package cn.fundview.app.domain.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.Map;

import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;

/**
 * 专家dao
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：ExpertDao 类描述： 创建人：lict 创建时间：2015年6月9日 上午10:00:11
 * 修改人：lict 修改时间：2015年6月9日 上午10:00:11 修改备注：
 *
 * @version 1.0
 */
public class ExpertDao extends BaseDao<Expert> {


    public ExpertDao(Context context) {

        super(context, Expert.class);
    }


    /**
     * 专家的模糊查询
     **/
    public List<Expert> getExpertListByCondition(Map<String, String> map, int page, int pageSize) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.findAll(Selector.from(Expert.class).limit(pageSize).offset(pageSize * (page - 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return null;
        }
        String words = map.get("searcher");
        try {
            return dbUtils.findAll(Selector.from(Expert.class).where("name", "like", "%" + words + "%").or("trade_name", "like", "%" + words + "%").orderBy("update_date", true).
                    limit(pageSize).
                    offset(pageSize * (page - 1)));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 专家条件查询总数量
     */
    public long countExpertByCondition(Map<String, String> map) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.count(Selector.from(Expert.class));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return 0;
        }
        String words = map.get("searcher");
        try {
            return dbUtils.count(Selector.from(Expert.class).where("name", "like", "%" + words + "%").or("trade_name", "like", "%" + words + "%"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * 保存/更新专家列表
     *
     * @param list 操作数据
     * @return true 操作成功
     */
    public boolean saveOrUpdateList(List<Expert> list) {

        if (list != null && list.size() > 0) {

            for (Expert item : list) {

                if (item != null) {

                    Expert company = getById(item.getId());
                    if (company != null) {

                        //更新
                        item.setId(company.getId());
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
    public List<Expert> getRecommendList(int size) {

        try {
            return dbUtils.findAll(Selector.from(Expert.class).where("recommend_num", "=", 1).orderBy("update_date", true).offset(0).limit(size));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 特约专家 分页查询
     * @param pageSize 每页显示的条数
     * @param currentPage 当前页数
     * @param searcher 搜索关键字  name tradeName areaName 职称 擅长领域
     * @return
     */
    public List<Expert> findGuestExpert(int pageSize, int currentPage, String searcher) {

        return null;
    }
}
