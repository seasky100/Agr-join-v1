package cn.fundview.app.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import cn.fundview.app.model.Area;

/**
 * 用户基本信息
 **/
@Table(name = "t_user")
public class UserInfor {

    public static final int COMPANY_TYPE = 1;
    public static final int EXPERT_TYPE = 2;
    public static final int PERSON_TYPE = 6;//个人用户类型
    public static final int KYORG_TYPE = 5;//科研机构用户类型

    //字段名
    @JSONField(name = "accountId")
    @NoAutoIncrement//
    private int id;
    //数据库字段(本地)
    public static final String AREA = "area";
    public static final String NAME = "name";
    public static final String HEADICONLOCALPATH = "head_icon_local_path";
    public static final String TEL = "tel";
    public static final String ADDR = "addr";
    public static final String QRCODEIMGLOCALPATH = "qr_code_img_localpath";
    public static final String LINKMAN = "link_man";
    public static final String PROFESSIONAL_TITLE = "professional_title";
    public static final String SPECIALTY = "specialty";
    public static final String WORK = "work";
    public static final String SERVICE = "service";
    public static final String COMPTYPE = "comp_type";

    //server 端属性名
    public static final String SERVER_NAME = "name";
    public static final String SERVER_LINKMAN = "linkman";
    public static final String SERVER_PHONE = "phone";
    public static final String SERVER_ADDR = "addr";
    public static final String SERVER_SERVICE = "service";
    public static final String SERVER_PROFESSIONALTITLE = "professionalTitle";
    public static final String SERVER_SPECIALTY = "specialty";
    public static final String SERVER_ADDRESS = "address";
    public static final String SERVER_WORK = "work";
    public static final String SERVER_COMP_TYPE = "compType";
    public static final String SERVER_AREA_IDS = "areaIds";
    public static final String SERVER_AREA_NAMES = "areaNames";


    @Column(column = "account")
    private String account;// 登录帐号

    @Column(column = "password")
    private String password;// 密码

    @JSONField(name = "areaNames")
    @Column(column = "area")
    private String area; // 省市县

    @JSONField(name = "areaIds")
    @Column(column = "area_ids")
    private String areaIds;     //省市县ids

    @JSONField(name = "name")
    @Column(column = "name")
    private String name;// 专家真是姓名/企业名称

    @JSONField(name = "logo")
    @Transient
    private String headIcon;// 头像

    @Column(column = "head_icon_local_path")
    private String headIconLocalPath;//头像本地路径

    @JSONField(name = "phone")
    @Column(column = "tel")
    private String tel;// 固定电话

    @JSONField(name = "type")
    @Column(column = "type")
    private int type;// 1 企业 2专家

    @Column(column = "create_date")
    private long createDate;

    @Column(column = "update_date")
    private long updateDate;

    @Column(column = "device_id")
    private String deviceId;// 设备id

    @JSONField(name = "addr")
    @Column(column = "addr")
    private String addr;//用户详细地址

    @Column(column = "qr_code_img")
    private String qrCodeImg;//二维码线上路径


    @Column(column = "qr_code_img_localpath")
    private String qrCodeImgLocalPath;//二维码本地路径

    @JSONField(name = "linkman")
    @Column(column = "link_man")
    private String linkMan;//企业联系人

    @JSONField(name = "compType")
    @Column(column = "comp_type")
    private int compType;//企业类型 1加工  2农资  3生产  4流通 5 其他

    @JSONField(name = "professionalTitle")
    @Column(column = "professional_title")
    private String professionalTitle;//专家专业职称

    @JSONField(name = "auth")
    @Column(column = "auth")
    private boolean auth;//认证状态

    @JSONField(name = "service")
    @Column(column = "service")
    private String service;//

    @JSONField(name = "work")
    @Column(column = "work")
    private String work;//从事行业

    @JSONField(name = "specialty")
    @Column(column = "specialty")
    private String specialty;

    @Transient
    @JSONField(name = "address")
    private String address;//个人地址(个人服务器地址与专家和企业不对应)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfor() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getHeadIconLocalPath() {
        return headIconLocalPath;
    }

    public void setHeadIconLocalPath(String headIconLocalPath) {
        this.headIconLocalPath = headIconLocalPath;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getQrCodeImgLocalPath() {
        return qrCodeImgLocalPath;
    }

    public void setQrCodeImgLocalPath(String qrCodeImgLocalPath) {
        this.qrCodeImgLocalPath = qrCodeImgLocalPath;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCompType() {
        return compType;
    }

    public void setCompType(int compType) {
        this.compType = compType;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }
    

    @Override
    public String toString() {
        return "UserInfor{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", area='" + area + '\'' +
                ", name='" + name + '\'' +
                ", headIcon='" + headIcon + '\'' +
                ", headIconLocalPath='" + headIconLocalPath + '\'' +
                ", tel='" + tel + '\'' +
                ", type=" + type +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", deviceId='" + deviceId + '\'' +
                ", addr='" + addr + '\'' +
                ", qrCodeImg='" + qrCodeImg + '\'' +
                ", qrCodeImgLocalPath='" + qrCodeImgLocalPath + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", professionalTitle='" + professionalTitle + '\'' +
                ", auth=" + auth +
                ", service='" + service + '\'' +
                ", work='" + work + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}
