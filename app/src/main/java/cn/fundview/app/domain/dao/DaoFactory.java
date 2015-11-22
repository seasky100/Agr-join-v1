package cn.fundview.app.domain.dao;

import android.content.Context;

import cn.fundview.app.domain.model.FundviewInfor;

public class DaoFactory {

    private static DaoFactory instance = null;

    private ExpertDao expertDao;                    // 专家Dao
    private CompanyDao compDao;                        // 企业Dao
    private SearchHistoryDao searchHistoryDao;        // 搜索历史Dao
    private AchvDao achvDao;                        //成果Dao
    private RequDao requDao;                        //技术需求dao
    private UserInforDao userInforDao;                //用户信息表
    private FundviewInforDao fundviewInforDao;        //丰景资讯
    private AttentUserDao attentUserDao;            //关注用户Dao
    private OrgDao orgDao;                            //参展机构dao
    private FavoriteDao favoriteDao;                //收藏Dao
    private ProductDao productDao;                    //企业产品Dao

    private DaoFactory(Context context) {
        init(context);
    }

    public static DaoFactory getInstance(Context context) {
        if (instance == null) {
            instance = new DaoFactory(context);
        }
        return instance;
    }

    private void init(Context context) {

        expertDao = new ExpertDao(context);
        compDao = new CompanyDao(context);
        fundviewInforDao = new FundviewInforDao(context);
        searchHistoryDao = new SearchHistoryDao(context);
        requDao = new RequDao(context);
        achvDao = new AchvDao(context);
        userInforDao = new UserInforDao(context);
        attentUserDao = new AttentUserDao(context);
        orgDao = new OrgDao(context);
        favoriteDao = new FavoriteDao(context);
        productDao = new ProductDao(context);

    }

    /**
     * 获取专家Dao
     **/
    public ExpertDao getExpertDao() {

        return expertDao;
    }

    /**
     * 获取企业Dao
     **/
    public CompanyDao getCompDao() {

        return compDao;
    }


    /**
     * 获取搜索历史Dao
     **/
    public SearchHistoryDao getSearchHistoryDao() {

        return searchHistoryDao;
    }


    public AchvDao getAchvDao() {

        return achvDao;
    }

    public RequDao getRequDao() {

        return requDao;
    }

    /**
     * 用户基本信息表
     *
     * @return UserInforDao
     */
    public UserInforDao getUserInforDao() {

        return userInforDao;
    }

    /**
     * 丰景资讯
     *
     * @return FundviewInforDao
     */
    public FundviewInforDao getFundviewInforDao() {

        return fundviewInforDao;
    }

    /**
     * 查询关注用户Dao
     *
     * @return AttentUserDao
     */
    public AttentUserDao getAttentUserDao() {

        return attentUserDao;
    }

    /**
     * 查询参展机构Dao
     *
     * @return OrgDao
     */
    public OrgDao getOrgDao() {

        return orgDao;
    }

    /**
     * 获取收藏Dao
     *
     * @return FavoriteDao
     */
    public FavoriteDao getFavoriteDao() {

        return this.favoriteDao;
    }

    public ProductDao getProductDao() {

        return this.productDao;
    }
}	

