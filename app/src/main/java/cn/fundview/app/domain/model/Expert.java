package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;

/**
 * 专家Entity
 *
 * @version 1.0
 */
@Table(name = "t_expert")
public class Expert {

    private static final long serialVersionUID = 1L;

    public static final int EXPERT_TYPE_GUEST = 1;
    public static final int EXPERT_TYPE_NORMAL = 0;

    //@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @JSONField(name = "accountId")
    @NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
    private int id;

    @JSONField(name = "name")
    @Column(column = "name")
    private String name;

    @JSONField(name = "logo")
    @Column(column = "logo")
    private String logo;// logo 的网络路径

    @Column(column = "logo_local_path")
    private String logoLocalPath;// logo 的本地路径

    @JSONField(name = "tradeNames")
    @Column(column = "trade_name")
    private String tradeName;

    @JSONField(name = "areaNames")
    @Column(column = "area_name")
    private String areaName;

    @JSONField(name="attentNum")
    @Column(column = "attention")
    private int attention;

    @Column(column = "recommend_num")
    private int recommendNum;//推荐值

    @JSONField(name = "updateDate")
    @Column(column = "update_date")
    private long updateDate;

    @JSONField(name = "professionalTitle")
    @Column(column = "professional_title")
    private String professionalTitle;//专业职称

    private String specialty;//擅长领域
    private int expertType;//专家类型 1特约专家 0普通专家

    private boolean phoneConsulation;//是否接受电话咨询 true 接受
    private boolean videoConsulation;//是否接受视频咨询 true 接受

    @Transient
    @JSONField(name = "attentDate")
    private long attentDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getResearchField() {
        return researchField;
    }

    public void setResearchField(String researchField) {
        this.researchField = researchField;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    @Column(column = "json_save_path")

    private String jsonSavePath;// json 文件保存路径

    @Column(column = "dept")
    private String dept;// 部门

    @Column(column = "research_field")
    private String researchField;// 研究领域

    @Column(column = "workUnit")
    private String workUnit;// 工作单位


    public Expert() {
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

    public String getLogoLocalPath() {
        return logoLocalPath;
    }

    public void setLogoLocalPath(String logoLocalPath) {
        this.logoLocalPath = logoLocalPath;
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

    public String getJsonSavePath() {
        return jsonSavePath;
    }

    public void setJsonSavePath(String jsonSavePath) {
        this.jsonSavePath = jsonSavePath;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public long getAttentDate() {
        return attentDate;
    }

    public void setAttentDate(long attentDate) {
        this.attentDate = attentDate;
    }

    @Override
    public String toString() {
        return "Expert{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", logoLocalPath='" + logoLocalPath + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", areaName='" + areaName + '\'' +
                ", attention=" + attention +
                ", recommendNum=" + recommendNum +
                ", updateDate=" + updateDate +
                ", professionalTitle='" + professionalTitle + '\'' +
                ", attentDate=" + attentDate +
                ", jsonSavePath='" + jsonSavePath + '\'' +
                ", dept='" + dept + '\'' +
                ", researchField='" + researchField + '\'' +
                ", workUnit='" + workUnit + '\'' +
                '}';
    }
}
