package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;

/**
 * @author dell 企业需求
 */
@Table(name = "t_tech_requ")
public class Requ {

    @JSONField(name = "id")
    @NoAutoIncrement
    private int id;//主键.非自增

    @JSONField(name = "name")
    @Column(column = "name")
    private String name;

    @Column(column = "create_date")
    private long createDate;// 需求的发布时间

    @JSONField(name = "updateTime")
    @Column(column = "update_date")
    private long updateTime;// 需求的更新时间

    @JSONField(name = "tradeNames")
    @Column(column = "trade_name")
    private String tradeName;

    @JSONField(name = "finPlan")
    @Column(column = "fin_plan")
    private double finPlan;//计划资金

    @JSONField(name = "logo")
    @Column(column = "logo")
    private String logo;//企业logo

    @Column(column = "logo_local_path")
    private String logoLocalPath;//企业logo的本地路径

    @JSONField(name = "requHj")
    @Column(column = "hj")
    private String hj;//生产环节

    @JSONField(name = "otherRequHj")
    @Column(column = "other_hj")
    private String otherHj;//其他的生产环节

    @JSONField(name = "compName")
    @Column(column = "owner_name")
    private String ownerName;

    @Column(column = "favorite_time")
    private long favoriteTime;

    @Column(column = "recommend")
    private int recommend;//标识需求是否是推荐的 1是 0 不是

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public double getFinPlan() {
        return finPlan;
    }

    public void setFinPlan(double finPlan) {
        this.finPlan = finPlan;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoLocalPath() {
        return logoLocalPath;
    }

    public void setLogoLocalPath(String logoLocalPath) {
        this.logoLocalPath = logoLocalPath;
    }


    public String getHj() {
        return hj;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getFavoriteTime() {
        return favoriteTime;
    }

    public void setFavoriteTime(long favoriteTime) {
        this.favoriteTime = favoriteTime;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getOtherHj() {
        return otherHj;
    }

    public void setOtherHj(String otherHj) {
        this.otherHj = otherHj;
    }

    @Override
    public String toString() {
        return "Requ{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", updateTime=" + updateTime +
                ", tradeName='" + tradeName + '\'' +
                ", finPlan=" + finPlan +
                ", logo='" + logo + '\'' +
                ", logoLocalPath='" + logoLocalPath + '\'' +
                ", hj='" + hj + '\'' +
                ", otherHj='" + otherHj + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", favoriteTime=" + favoriteTime +
                ", recommend=" + recommend +
                '}';
    }
}
