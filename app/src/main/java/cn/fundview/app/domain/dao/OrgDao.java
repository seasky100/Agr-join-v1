package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.Map;

import cn.fundview.app.domain.model.Org;

/**
 * 项目名称：agr-join-v2.0.0 类名称：OrgDao 类描述： 参展机构dao 创建人：lict 创建时间：2015年6月8日 下午5:06:55
 * 修改人：lict 修改时间：2015年6月8日 下午5:06:55 修改备注：
 */
public class OrgDao extends BaseDao<Org> {


    public OrgDao(Context context) {
        super(context, Org.class);
    }

    /**
     * 参展机构的模糊查询
     *
     * @param map      搜索条件
     * @param page     当前页数
     * @param pageSize 每页显示条数
     * @return 机构列表
     */
    public List<Org> getOrgListByCondition(Map<String, String> map, int page, int pageSize) {

        try {

            if (map == null || map.size() == 0) {

                return dbUtils.findAll(Selector.from(Org.class));
            }
            String words = map.get("searcher");
            return dbUtils.findAll(Selector.from(Org.class).where("name", "like", "%" + words + "%").limit(pageSize).offset((pageSize - 1) * page).orderBy("update_date", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 参展机构条件查询总数量
     */
    public long countOrgByCondition(Map<String, String> map) {

        try {
            if (map == null || map.size() == 0) {

                return dbUtils.count(Selector.from(Org.class));
            }
            String words = map.get("searcher");
            return dbUtils.count(Selector.from(Org.class).where("name", "like", "%" + words + "%"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return 0;
    }


}
