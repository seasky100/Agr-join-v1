package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 专家的科技成果
 *
 * @version 1.0
 */
@Table(name = "t_achv")
public class Achv {

    //@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @JSONField(name = "id")
    @NoAutoIncrement
    private int id;

    @Column(column = "create_date")
    private long createDate;// 成果的添加时间

    @JSONField(name = "firstImg")
    @Column(column = "logo")
    private String logo;// 成果的第一张图片 网络路径

    @JSONField(name = "name")
    @Column(column = "name")
    private String name;

    @JSONField(name = "price")
    @Column(column = "price")
    private double price;//参考价格

    @JSONField(name = "tradeNames")
    @Column(column = "trade_name")
    private String tradeName;

    @JSONField(name = "updateDate")
    @Column(column = "update_date")
    private long updataDate;// 成果的更新时间

    @JSONField(name = "publisherName")
    @Column(column = "owner_name")
    private String ownerName;//拥有者名称

    @Column(column = "old_local_path")
    private String oldLocalPath;//成果第一张图片

    @JSONField(name = "favoriteTime")
    @Column(column = "favorite_time")
    private long favoriteTime;

    @Column(column = "recommend")
    public int recommend;//标识成果是否是推荐成果,1是 0 不是

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Achv() {
        super();
        // TODO Auto-generated constructor stub
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public long getUpdataDate() {
        return updataDate;
    }

    public void setUpdataDate(long updataDate) {
        this.updataDate = updataDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOldLocalPath() {
        return oldLocalPath;
    }

    public void setOldLocalPath(String oldLocalPath) {
        this.oldLocalPath = oldLocalPath;
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

    @Override
    public String toString() {
        return "Achv{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", tradeName='" + tradeName + '\'' +
                ", updataDate=" + updataDate +
                ", ownerName='" + ownerName + '\'' +
                ", oldLocalPath='" + oldLocalPath + '\'' +
                ", favoriteTime=" + favoriteTime +
                ", recommend=" + recommend +
                '}';
    }
}