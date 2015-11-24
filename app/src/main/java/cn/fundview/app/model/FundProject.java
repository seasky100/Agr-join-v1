package cn.fundview.app.model;

/**
 * 项目名称：Agr-join-v1
 * 类描述：融资项目
 * 创建人：lict
 * 创建时间：2015/11/24 0024 上午 9:45
 * 修改人：lict
 * 修改时间：2015/11/24 0024 上午 9:45
 * 修改备注：
 */
public class FundProject{

    private Integer id;
    private String projName;     //项目名
    private String jdName;
    private double invest;      // 投资额度;
    private String name;        //企业名称
    private Integer compId;     //企业id
    private String logo;        //企业logo
    private String projField;   //项目领域
    private String summary;     //融资项目介绍

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getJdName() {
        return jdName;
    }

    public void setJdName(String jdName) {
        this.jdName = jdName;
    }

    public double getInvest() {
        return invest;
    }

    public void setInvest(double invest) {
        this.invest = invest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProjField() {
        return projField;
    }

    public void setProjField(String projField) {
        this.projField = projField;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
