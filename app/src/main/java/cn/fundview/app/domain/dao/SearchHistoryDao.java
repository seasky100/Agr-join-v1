package cn.fundview.app.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SearchView;

import cn.fundview.app.domain.model.SearchHistory;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class SearchHistoryDao extends BaseDao<SearchHistory> {

    public SearchHistoryDao(Context context) {

        super(context, SearchHistory.class);
    }

    /**
     * 获取所有的搜索历史 根据类型查询 排序
     *
     * @param type 1 专家 2 企业
     */
    public List<SearchHistory> getSearchHistorys(int type) {
        try {
            return dbUtils.findAll(Selector.from(SearchHistory.class).where("type", "=", type + "").orderBy("search_time", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据关键字查找历史记录
     **/
    public List<SearchHistory> getSearchHistorys(String key, int type) {
        try {
            return dbUtils.findAll(Selector.from(SearchHistory.class).where("type", "=", type + "").and("words", "like", "%" + key + "%").orderBy("search_time", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定类型的指定关键字
     **/
    public boolean deleteByTypeAndWords(String key, int type) {
        try {
            dbUtils.delete(SearchHistory.class, WhereBuilder.b("type", "=", type + "").and("words", "=", key));//);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询指定类型和关键字的搜索历史,精确查询
     *
     * @param key  关键字
     * @param type 类型
     * @return SearchHistory
     */
    public SearchHistory getSearchHistoryByNameAndType(String key, int type) {

        try {
            return dbUtils.findFirst(Selector.from(SearchHistory.class).where("type", "=", type + "").and("words", "=", key).orderBy("search_time"));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
