package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 丰景资讯
 *
 * @author ouda
 */
@Table(name = "t_fundview_infor")
public class FundviewInfor {

    @NoAutoIncrement
    private int id;

    @Column(column = "title")
    private String title;

    @JSONField(name = "imgUrl")
    @Column(column = "logo")
    private String logo;

    @Column(column = "logo_local_path")
    private String logoLocalPath;

    @JSONField(name = "intro")
    @Column(column = "introduction")
    private String introduction;// 介绍

    @JSONField(name = "updateDate")
    @Column(column = "update_date")
    private long updateDate;// 资讯修改的时间

    @Column(column = "delivery_date")
    private long deliveryDate;// // 资讯推送的时间(作假用) 只是显示用

    @Column(column = "publish_date")
    private long publishDate;//咨询的发布时间

    @Column(column = "read")
    private int read;//是否已读 1已读  0未读

    @Column(column = "url")
    private String url;//链接的文章url

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FundviewInfor{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", logo='" + logo + '\'' +
                ", logoLocalPath='" + logoLocalPath + '\'' +
                ", introduction='" + introduction + '\'' +
                ", updateDate=" + updateDate +
                ", deliveryDate=" + deliveryDate +
                ", publishDate=" + publishDate +
                ", read=" + read +
                ", url='" + url + '\'' +
                '}';
    }
}
