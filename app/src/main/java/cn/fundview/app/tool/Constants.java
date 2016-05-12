package cn.fundview.app.tool;

public class Constants {

    public static final boolean DEBUG = true;//标示运行模式 true
    public static final String TAG = "cn.fundview";
    /**
     * 数据库名字
     **/
    public static final String DATABASE_NAME = "fundview.db";

    /**
     * 数据库版本
     */
    public static final int DATABASE_VERSION = 5;

    public static final String DOWN_SERVICE = "http://m.fundview.cn/app/version.xml";
    public static final String APK_DOWN_PATH = "http://m.fundview.cn/app/fundview.apk";
//    public static final String APK_DOWN_PATH = "http://192.168.1.108:8080/app/fundview.apk";
//    public static final String DOWN_SERVICE = "http://192.168.1.108:8080/app/version.xml";

    /************************************
     * 本地存储登录用户名和密码的key
     ***************************************************************/
    public static final String ACCOUNT_KEY = "fundview_account";
    public static final String PASSWORD_KEY = "fundview_password";
    public static final String ACCOUNT_TYPE_KEY = "account_type_key";//用户类型
    public static final String LOGIN_STATUS_KEY = "fundview_account_login_status";
    public static final String ACCOUNT_ID = "fundview_account_id";

    public static final int LOGIN_STATUS = 1;//已登录
    public static final int LOGIN_OUT_STATUS = 0;//未登录或是退出登录

    /****************************
     * 我的头像保存的路径以及默认图片
     ***********************************************/
    //个人账户图片的存储位置
    public static final String MY_ICON_SAVE_DIR = "/fundview/data/userInfor/icon/";


    // 丰景资讯json
    public static final String INFORS_JSON_DIR = "/fundview/data/infors/json/";
    public static final String INFORS_IMG_DIR = "/fundview/data/infors/img/";

    // 技术需求
    public static final String REQU_JSON_DIR = "/fundview/data/requ/json/";// 企业需求json
    // 文件存放路径
    // 文件存放路径

    // 专家成果
    public static final String ACHV_JSON_DIR = "/fundview/data/achv/json/";

    /**
     * apk的下载存放路径 包括文件名
     **/
    public static final String APK_PATH = "/fundview/apk/fundview.apk";

    /**
     * versino 文件的存放路径 包括文件名
     **/
    public static final String VERSION_PATH = "/fundview/version/version.xml";

    // 临时路径
    public static final String TEMP_PATH = "/fundview/data/temp/";

    /**
     * 应用第一次打开的标识
     */
    public static final String FIRST_OPEN_TAG = "first_open_tag";

    /**
     * 第一次安装标示
     */
    public static final String FIRST_INSTALL_TAG = "first_install_tag";


    public static final String GUIDE_FRAGMENT_IMG_ID = "drawableId";

    // 专家json文件保存路径
    public static final String EXPERT_JSON_DIR = "/fundview/data/expert/json/";

    // 企业json文件保存的路径
    public static final String COMPANY_JSON_DIR = "/fundview/data/comp/json/";

    //企业产品详细 json 文件存的路径
    public static final String COMPANY_PRODUCT_JSON_DIR = "/fundview/data/comp/product/json/";

    public static final String ORG_JSON_DIR = "/fundview/data/org/json/";

    //搜索类型常量
    public static final int COMPANY_SEARCH_TYPE = 0;
    public static final int ORG_SEARCH_TYPE = 1;
    public static final int EXPERT_SEARCH_TYPE = 2;
    public static final int REQU_SEARCH_TYPE = 3;
    public static final int ACHV_SEARCH_TYPE = 4;
    //新的版本号 表示
    public static final String NEW_VERSION = "new_version";  // 1 有新的版本号
}
