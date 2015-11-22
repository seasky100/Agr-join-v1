package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;
import java.util.Map;

import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Product;
import cn.fundview.app.domain.model.Requ;

/**
 * 企业产品Dao
 */
public class ProductDao extends BaseDao<Product> {


    public ProductDao(Context context) {

        super(context, Product.class);
    }

    public List<Product> getAll() {

        try {
            return dbUtils.findAll(Product.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }
    //根据关键字查询
    public List<Product> getProductListByCondition(Map<String, String> map, int page, int pageSize) {

        if (map != null) {

            String key = map.get("searcher");
            try {
                return dbUtils.findAll(Selector.from(Product.class).where("name", "like", "%" + key + "%").
                        orderBy("update_date", true).
                        limit(pageSize).
                        offset(pageSize * (page - 1)));
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {

            try {
                return dbUtils.findAll(Selector.from(Product.class).
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
    public long countProductByCondition(Map<String, String> map) {

        if (map == null || map.size() == 0) {

            try {
                return dbUtils.count(Selector.from(Product.class));
            } catch (DbException e) {
                e.printStackTrace();
            }

            return 0;
        }
        String words = map.get("searcher");
        try {
            return dbUtils.count(Selector.from(Product.class).where("name", "like", "%" + words + "%"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return 0;
    }


}
