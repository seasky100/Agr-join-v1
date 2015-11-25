package cn.fundview.app.domain.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * Created by Administrator on 2015/10/31 0031.
 * <p/>
 * 企业产品
 */
@Table(name = "t_product")
public class Product {

    //对应服务器端的产品id
    @NoAutoIncrement
    private int id;

    @Column(column = "comp_id")
    private int compId;     //企业id

    @Column(column = "logo")
    private String logo;    //产品第一张图片

    @Column(column = "local_logo")
    private String locaLogo;//本地logo

    @Column(column = "name")
    private String name;    //产品名称

    @Column(column = "updateDate")
    private String updateDate;

    @Column(column = "comp_name")
    private String compName;

    @Column(column = "price")
    private double price;

    @Column(column = "unit")
    private String unit;

    @Column(column = "recommend")
    private int recommend;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLocaLogo() {
        return locaLogo;
    }

    public void setLocaLogo(String locaLogo) {
        this.locaLogo = locaLogo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", compId=" + compId +
                ", logo='" + logo + '\'' +
                ", locaLogo='" + locaLogo + '\'' +
                ", name='" + name + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", compName='" + compName + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", recommend=" + recommend +
                '}';
    }
}


