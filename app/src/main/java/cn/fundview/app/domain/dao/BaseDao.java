package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * Created by Administrator on 2015/9/20 0020.
 */
public abstract class BaseDao<T> {

    protected DbUtils dbUtils;
    protected Class<T> clazz;

    public BaseDao(Context context, Class<T> clazz) {

        this.dbUtils = DbHelper.getInstance(context).getDbUtils();
        this.clazz = clazz;
    }

    /**
     * 分页查询 最新
     *
     * @param pageSize
     * @param page
     * @return
     */
    public List<T> getList(int pageSize, int page) {

        try {
            return dbUtils.findAll(Selector.from(clazz).orderBy("update_date").limit(pageSize).offset((page - 1) * pageSize));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 精确查询  根据id
     **/
    public T getById(int id) {

        try {
            return dbUtils.findById(clazz, id);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 保存列表
     *
     * @param list
     * @return
     */
    public boolean saveList(List<T> list) {

        try {
            dbUtils.saveAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 保存
     *
     * @param item
     * @return
     */
    public boolean save(T item) {
        try {
            dbUtils.save(item);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 更新
     *
     * @param item
     * @return
     */
    public boolean update(T item) {

        try {
            dbUtils.saveOrUpdate(item);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 删除表中所有的数据
     *
     * @return
     */
    public boolean deleteAll() {

        try {
            dbUtils.deleteAll(clazz);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除表中指定成果
     *
     * @return
     */
    public boolean deleteById(int id) {

        try {
            dbUtils.deleteById(clazz, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 关闭数据库资源
     */
    public void colseDb() {

        dbUtils.close();
    }
}
