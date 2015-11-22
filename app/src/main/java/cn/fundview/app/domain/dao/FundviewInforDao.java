package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import cn.fundview.app.domain.model.FundviewInfor;

/**
 * 丰景咨询 Dao
 */
public class FundviewInforDao extends BaseDao<FundviewInfor> {

    public FundviewInforDao(Context context) {

        super(context, FundviewInfor.class);
    }

    /**
     * 统计未读的丰景咨询的数量 在消息列表用
     *
     * @return 未读的条数
     */
    public int countUnReadFundviewInfor() {

        try {
            return (int) dbUtils.count(Selector.from(FundviewInfor.class).where("read", "=", "0"));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 查询本地存储的第一条资讯 id 最小
     *
     * @return 资讯
     */
    public FundviewInfor getFirst() {

        try {
            return dbUtils.findFirst(Selector.from(FundviewInfor.class).orderBy("id", true));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查询本地存储的最新的一条资讯 id 最大
     *
     * @return 资讯
     */
    public List<FundviewInfor> getLastest(int size) {

        try {
            return dbUtils.findAll(Selector.from(FundviewInfor.class).orderBy("id", true).offset(0).limit(size));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查询本地的所有 未读的丰景资讯
     *
     * @param size 0 查看全部未读  1查看最新的未读数据
     * @return 资讯列表
     */
    public List<FundviewInfor> getAllUnRead(int size) {

        try {
            if (size == 0)
                return dbUtils.findAll(Selector.from(FundviewInfor.class).where("read", "=", "0").offset(0).orderBy("id", true));
            else
                return dbUtils.findAll(Selector.from(FundviewInfor.class).offset(0).limit(size).orderBy("id", true));

        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 设置指定id 之前的资讯全部已读
     *
     * @param id 需要设置已读的最大的id
     * @return true 成功
     */
    public boolean setRead(int id) {

        try {
            List<FundviewInfor> list = dbUtils.findAll(Selector.from(FundviewInfor.class).where("id", "<=", id));
            if (list != null && list.size() > 0) {

                for (FundviewInfor item : list) {

                    item.setRead(1);
                }
                dbUtils.updateAll(list, "read");

            }
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 查询本地的历史数据
     *
     * @param id   已经展示的最小的id
     * @param size 查询的条数
     * @return
     */
    public List<FundviewInfor> getHistory(int id, int size) {

        try {
            return dbUtils.findAll(Selector.from(FundviewInfor.class).where("id", "<", id).orderBy("id", true).offset(0).limit(size));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }
}
