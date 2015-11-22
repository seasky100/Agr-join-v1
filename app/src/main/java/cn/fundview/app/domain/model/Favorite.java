package cn.fundview.app.domain.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by lict on 2015/10/22.
 * 收藏 model 收藏 之用于用户对成果和需求的收藏
 * 登录前收藏之存储在手机中，
 * 登录后收藏的需要同步到服务器端
 * 是要是收藏者和被收藏的 id 与类型相同的即认为是用一个收藏
 */
@Table(name = "t_favorite")
public class Favorite {

    public static final int FAVORITE_ACHV_TYPE = 1;
    public static final int FAVORITE_REQU_TYPE = 2;
    private int id;

    @Column(column = "device_id")
    private String deviceId;
    @Column(column = "account_id")
    private int accountId;//收藏用户的id.未登录时为0
    @Column(column = "account_type")
    private int accountType;//收藏用户的类型.未登录的时候为0
    @Column(column = "favorite_id")
    private int favoriteId;//被收藏 的id 这里是成果获需求的id
    @Column(column = "favorite_type")
    private int favoriteType;//被收藏的类型 1成果 2需求
    @Column(column = "favorite_date")
    private long favoriteDate;//  收藏时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(int favoriteType) {
        this.favoriteType = favoriteType;
    }

    public long getFavoriteDate() {
        return favoriteDate;
    }

    public void setFavoriteDate(long favoriteDate) {
        this.favoriteDate = favoriteDate;
    }
}
