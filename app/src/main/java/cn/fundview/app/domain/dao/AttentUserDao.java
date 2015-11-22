package cn.fundview.app.domain.dao;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import cn.fundview.app.domain.model.AttentUser;

/**
 * 关注用户
 */
public class AttentUserDao extends BaseDao<AttentUser> {

    public AttentUserDao(Context context) {

        super(context, AttentUser.class);
    }

    /**
     * 删除指定账户的关注者
     *
     * @param accountId 当前登录账户id
     * @return true 删除成功
     */
    public boolean deleteByAttentAccount(Integer accountId) {

        try {
            dbUtils.delete(AttentUser.class, WhereBuilder.b("attent_id", "=", accountId));
        } catch (DbException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * 根据当前的用户账号 查询关注列表 分页查询
     *
     * @param accountId 当前登录账号id
     * @return list
     */
    public List<AttentUser> getListByAttentUser(Integer accountId, int beAttentType, int page, int pageSize) {

        try {
            List<AttentUser> list = dbUtils.findAll(Selector.from(AttentUser.class).where("attent_id", "=", accountId).and("be_attent_type", "=", beAttentType).limit(pageSize).offset(pageSize * (page - 1)));
            return list;
        } catch (DbException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 根据关注者id和关注者类型 删除指定时间段的指定类型的关注列表
     *
     * @param attentId        关注者id
     * @param startAttentTime 关注上限时间
     * @param endAttentTime   关注下限时间
     * @param beAttentedType  被关注者类型
     * @return
     */
    public boolean deleteAttentListBy(int attentId, long startAttentTime, long endAttentTime, int beAttentedType) {

        try {
            dbUtils.delete(AttentUser.class, WhereBuilder.b("attent_id", "=", attentId).and("be_attent_type", "=", beAttentedType).
                    and("attent_time", ">=", endAttentTime).and("attent_time", "<=", startAttentTime));
        } catch (DbException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void deleteAttentByAccoundIdAndBeAttentId(int attentId, int beAttentId) {

        try {
            dbUtils.delete(AttentUser.class, WhereBuilder.b("attent_id", "=", attentId).and("be_attent_id", "=", beAttentId));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
