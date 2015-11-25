package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.Map;

import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Requ;

public class CompanyDao extends BaseDao<Company> {


    public CompanyDao(Context context) {

        super(context, Company.class);
    }


    /**
     * 企业按条件查询
     **/
    public List<Company> getCompListByCondition(Map<String, String> map, int page, int pageSize) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.findAll(Selector.from(Company.class).orderBy("update_date", true).limit(pageSize).offset(pageSize * (page - 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return null;
        }


        String words = map.get("searcher");
        try {
            return dbUtils.findAll(Selector.from(Company.class).where("name", "like", "%" + words + "%").or("trade_name", "like", "%" + words + "%").orderBy("update_date", true).
                    limit(pageSize).
                    offset(pageSize * (page - 1)));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 企业条件查询总数量
     */
    public long countCompByCondition(Map<String, String> map) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.count(Selector.from(Company.class));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return 0;
        }
        String words = map.get("key");
        try {
            return dbUtils.count(Selector.from(Company.class).where("name", "like", "%" + words + "%"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 保存/更新列表
     *
     * @param list 操作data
     * @return true 操作成功
     */
    public boolean saveOrUpdateList(List<Company> list) {

        if (list != null && list.size() > 0) {

            for (Company item : list) {

                if (item != null) {

                    Company company = getById(item.getId());
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
    public List<Company> getRecommendList(int size) {

        try {
            return dbUtils.findAll(Selector.from(Company.class).where("recommend_num", "=", 1).orderBy("update_date", true).offset(0).limit(size));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
