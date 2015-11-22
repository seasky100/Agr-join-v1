package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;

/**
 * 企业Entity
 *
 * @version 1.0
 */
@Table(name = "t_company")
public class Company extends EntityBase {

    @JSONField(name = "accountId")
    @NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
    private int id;

    @JSONField(name = "name")
    @Column(column = "name")
    private String name;

    @JSONField(name = "logo")
    @Column(column = "logo")
    private String logo;//logo 网络路径

    @Column(column = "local_logo")
    private String localLogo;//logo的本地路径

    @JSONField(name = "tradeNames")
    @Column(column = "trade_name")
    private String tradeName;

    @JSONField(name = "areaNames")
    @Column(column = "area_name")
    private String areaName;

    @Column(column = "attention")
    private int attention;

    @Column(column = "recommend_num")
    private int recommendNum;//推荐值

    @JSONField(name = "updateDate")
    @Column(column = "update_date")
    private long updateDate;

    @Column(column = "addr")
    private String addr; // 详细地址 企业

    @Column(column = "jy_range")
    private String jyRange;// 经营范围 企业

    @Column(column = "expo_no")
    private String expoNo;//展位号

    @Transient
    @JSONField(name = "attentDate")
    private long attentDate;//关注时间

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getJyRange() {
        return jyRange;
    }

    public void setJyRange(String jyRange) {
        this.jyRange = jyRange;
    }

    /**
     * constructor
     */
    public Company() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLocalLogo() {
        return localLogo;
    }

    public void setLocalLogo(String localLogo) {
        this.localLogo = localLogo;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(int recommendNum) {
        this.recommendNum = recommendNum;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getExpoNo() {
        return expoNo;
    }

    public void setExpoNo(String expoNo) {
        this.expoNo = expoNo;
    }

    public long getAttentDate() {
        return attentDate;
    }

    public void setAttentDate(long attentDate) {
        this.attentDate = attentDate;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", localLogo='" + localLogo + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", areaName='" + areaName + '\'' +
                ", attention=" + attention +
                ", recommendNum=" + recommendNum +
                ", updateDate=" + updateDate +
                ", addr='" + addr + '\'' +
                ", jyRange='" + jyRange + '\'' +
                ", expoNo='" + expoNo + '\'' +
                ", attentDate=" + attentDate +
                '}';
    }
}
