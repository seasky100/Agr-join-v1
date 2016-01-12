package cn.fundview.app.domain.webservice.util;

public class Constants {

    /**
     * 服务器使用的地址
     **/
    public static final String SERVER = "http://192.168.1.133:8080/api";
//    public static final String SERVER = "http://m.fundview.cn/api/";


//    public static final String QR_CODE_IMG_PATH = "http://192.168.1.10:8080/static/";
    public static final String QR_CODE_IMG_PATH = "http://static.fundview.cn/";

    //二维码传递参数 http://m.fundview.cn/
//    public static final String M_DOMAIN = "m.fundview.cn";
    public static final String M_DOMAIN = "m.fundview.cn";

    /**
     * 获取上传个人信息头像
     **/
    public static final String PROFILE_ICON_SAVE_SERVER_URL = SERVER + "/account/updateLogo.action";

    /**
     * 登录url
     **/
    public static final String ACCOUNT_LOGIN_URL = SERVER + "/account/login.action";

    /**
     * 退出url
     **/
    public static final String ACCOUNT_LOGOUT_URL = SERVER + "/account/logout";

    /**
     * 发送手机验证码.同时验证手机号码的唯一性
     */
    public static final String CHECK_PHONE_SEND_CODE = SERVER + "/account/check-phone-send-code.action";

    /**
     * 根据用户名 发送手机验证码.
     */
    public static final String FORGET_PASSWORD_SEND_CODE_URL = SERVER + "/account/sendPhoneCodeByAccount.action";

    /**
     * 根据用户名 修改密码
     */
    public static final String FORGET_PASSWORD_UPDATE_PASSWORD_URL = SERVER + "/account/updatePasswordByUsername.action";


    /**
     * 手机注册,用户名(手机号) 类型(1 企业  2 专家  3 个人)  密码
     */
    public static final String REGIST_URL = SERVER + "/account/reg.action";

    /**
     * 更新用户的基本信息
     */
    public static final String UPDATE_USERINFOR_ATTR_URL = SERVER + "/account/update.action";

    /**
     * 获取我的基本信息uri 企业用户
     **/
    public static final String MY_PROFILE_COMPANY_URL = SERVER + "/company/info.action";

    /**
     * 获取我的基本信息uri 专家用户
     **/
    public static final String MY_PROFILE_EXPERT_URL = SERVER + "/expert/info.action";

    /**
     * 获取我的基本信息uri 个人用户
     **/
    public static final String MY_PROFILE_PERSONAL_URL = SERVER + "/persion/info.action";

    /**
     * 用户关注列表/添加/取消关注
     **/
    public static final String MY_ATTENTION_LIST_URL = SERVER + "/account/attention/list.action";
    public static final String ATTENT_URL = SERVER + "/account/attention/add.action";
    public static final String CANCEL_ATTENT_URL = SERVER + "/account/attention/cancel.action";
    public static final String IS_ATTENTION_URL = SERVER + "/account/attention/isAtt.action";
    public static final String FIND_ATTENTION_NUM = SERVER + "/account/attention/findAttNum.action";


    /**
     * 首页获取优质成果列表 ?page=0&size=5
     **/
    public static final String GET_HOME_ACHV_LIST_URL = SERVER + "/achv/recommend.action";

    /**
     * 首页企业需求列表
     **/
    public static final String GET_HOME_COMPANY_LIST_URL = SERVER + "/company/recommend.action";

    /**
     * 首页专家需求列表
     **/
    public static final String GET_HOME_EXPERT_LIST_URL = SERVER + "/expert/recommend.action";

    /**
     * 首页产品需求列表
     **/
    public static final String GET_HOME_PRODUCT_LIST_URL = SERVER + "/product/recommend.action";


    /**
     * 首页技术需求列表
     **/
    public static final String GET_HOME_REQU_LIST_URL = SERVER + "/requ/recommend.action";



    /**
     * 企业搜索列表 ?page=0&size=2&key=aaa
     **/
    public static final String GET_COMP_LIST_URL = SERVER + "/company/list.action";
    public static final String GET_COMP_DETAIL = SERVER + "/company/detail.action";
    public static final String GET_COMP_PRODUCT_LIST_URL = SERVER + "/company/product/list.action";//
    public static final String GET_COMP_PRODUCT_DETAIL_URL = SERVER + "/company/product/detail.action";//

    /**
     * 成果搜索
     **/
    public static final String GET_ACHV_LIST_URL = SERVER + "/achv/list.action";
    public static final String GET_ACHV_LIST_BY_ACCOUNTID_URL = SERVER + "/achv/listByAccount.action";
    public static final String GET_ACHV_DETAIL = SERVER + "/achv/detail.action";

    /**
     * 专家
     **/
    public static final String GET_EXPERT_LIST_URL = SERVER + "/expert/list.action";
    public static final String GET_EXPERT_DETAIL = SERVER + "/expert/detail.action";
    /**
     * 需求搜索
     **/
    public static final String GET_REQU_LIST_URL = SERVER + "/requ/list.action";
    public static final String GET_REQU_BY_ACCOUNTID_LIST_URL = SERVER + "/requ/listByAccount.action";
    public static final String GET_REQU_DETAIL = SERVER + "/requ/detail.action";

    /**
     * 产品列表
     */
    public static final String GET_PRODUCT_LIST_URL = SERVER + "/product/list.action";
    /**
     * 机构
     **/
    public static final String GET_ORG_DETAIL = SERVER + "/kyorg/detail.action";
    public static final String GET_ORG_LIST_URL = SERVER + "/kyorg/list.action";

    /**
     * 融资项目
     */
    public static final String GET_FUND_PROJ_LIST_URL = SERVER + "/fund/list.action";
    public static final String GET_FUND_PROJ_DETAIL_URL = SERVER + "/fund/detail.action";

    /**
     * 成果/需求的收藏和取消收藏
     * 参数是 favoriteId,favoriteType,beFavoriteId,beFavoriteType
     */
    public static final String ADD_FAVORITE_URL = SERVER + "/account/favorite/add.action";
    public static final String CANCEL_FAVORITE_URL = SERVER + "/account/favorite/delete.action";
    public static final String FAVORITE_LIST_URL = SERVER + "/account/favorite/list.action";
    /**
     * 丰景资讯请求
     */
    public static final String GET_FUNDVIEW_INFOR_LIST_HISTORY_URL = SERVER + "/information/list.action";        //历史数据
    public static final String GET_FUNDVIEW_INFOR_DETAIL_URL = SERVER + "/information/detail.action";            //详细
    public static final String FUNDVIEW_INFOR_KEY = "id";

    public static final String GET_AREA_LIST = SERVER + "/util/queryAreaByParentId.action";


    /**
     * 安装/卸载统计url
     */
    public static final String INSTALL_COUNT_URL = SERVER + "/install/install.action";
    public static final String UN_INSTALL_COUNT_URL = SERVER + "/install/uninstall.action";
    /************
     * 请求成功/失败返回值
     ******************/
    public static final int REQUEST_SUCCESS = 2;//请求成功
    public static final int USERNAME_PW_ERROR = 0;//登录时用户名或密码错误
    public static final int USER_FORBBIT = 1;//登录的时候用户被禁用


    /******************
     * 列表页面 item 高度 用于分页查询每页的条数
     ********************/
    public static final int EXPERT_ITEM_HEIGHT = 76;
    public static final int COMPANY_ITEM_HEIGHT = 95;
    public static final int ACHV_ITEM_HEIGHT = 83;
    public static final int REQU_ITEM_HEIGHT = 83;
    public static final int PRODUCT_ITEM_HEIGHT = 83;
    public static final int ORG_ITEM_HEIGHT = 65;//机构
    public static final int PROJECT_ITEM_HEIGHT = 87;//融资项目

}