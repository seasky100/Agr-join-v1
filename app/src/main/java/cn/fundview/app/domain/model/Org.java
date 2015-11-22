package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 参展机构(科研机构)
 *
 * @version 1.0
 */
@Table(name = "t_org")
public class Org {

    //@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @JSONField(name = "accountId")
    @NoAutoIncrement
    private int id;

    @Column(column = "create_date")
    private long createDate;// 机构的添加时间

    @JSONField(name = "logo")
    @Column(column = "logo")
    private String logo;// 机构logo

    @JSONField(name = "name")
    @Column(column = "name")
    private String name;

    @JSONField(name = "areaNames")
    @Column(column = "area")
    private String area;

    @JSONField(name = "updateDate")
    @Column(column = "update_date")
    private long updateDate;// 机构的更新时间

    @Column(column = "old_local_path")
    private String oldLocalPath;//logo 老图

    @Column(column = "expo_no")
    private String expoNo;//展位号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Org() {
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getOldLocalPath() {
        return oldLocalPath;
    }

    public void setOldLocalPath(String oldLocalPath) {
        this.oldLocalPath = oldLocalPath;
    }

    public String getExpoNo() {
        return expoNo;
    }

    public void setExpoNo(String expoNo) {
        this.expoNo = expoNo;
    }

    @Override
    public String toString() {
        return "Org{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", updateDate=" + updateDate +
                ", oldLocalPath='" + oldLocalPath + '\'' +
                ", expoNo='" + expoNo + '\'' +
                '}';
    }
}