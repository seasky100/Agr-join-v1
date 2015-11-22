package cn.fundview.app.domain.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * Created by Administrator on 2015/10/15 0015.
 * 关注用户
 */
@Table(name = "t_attent_user")
public class AttentUser {

    private int id;
    @Column(column = "attent_id")
    private int attentId;//关注者id
    @Column(column = "attent_type")
    private int attentType;//关注者类型
    @Column(column = "be_attent_id")
    private int beAttentId;//被关注者id
    @Column(column = "be_attent_type")
    private int beAttentType;//被关注者的类型
    @Column(column = "attent_time")
    private long attentTime;//关注时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttentId() {
        return attentId;
    }

    public void setAttentId(int attentId) {
        this.attentId = attentId;
    }

    public int getAttentType() {
        return attentType;
    }

    public void setAttentType(int attentType) {
        this.attentType = attentType;
    }

    public int getBeAttentId() {
        return beAttentId;
    }

    public void setBeAttentId(int beAttentId) {
        this.beAttentId = beAttentId;
    }

    public int getBeAttentType() {
        return beAttentType;
    }

    public void setBeAttentType(int beAttentType) {
        this.beAttentType = beAttentType;
    }

    public long getAttentTime() {
        return attentTime;
    }

    public void setAttentTime(long attentTime) {
        this.attentTime = attentTime;
    }


    @Override
    public String toString() {
        return "AttentUser{" +
                "id=" + id +
                ", attentId=" + attentId +
                ", attentType=" + attentType +
                ", beAttentId=" + beAttentId +
                ", beAttentType=" + beAttentType +
                ", attentTime=" + attentTime +
                '}';
    }
}
