package cn.fundview.app.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.ToastUtils;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class UserInforDao extends BaseDao<UserInfor> {

    public UserInforDao(Context context) {

        super(context, UserInfor.class);
    }

    public boolean updateUserInforHeadIcon(String headIcon, int accountId) {

        UserInfor infor = new UserInfor();
        infor.setHeadIconLocalPath(headIcon);
        try {
            dbUtils.update(infor, WhereBuilder.b("id", "=", accountId), "head_icon_local_path");
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 根据用户帐号号获取用户信息
     **/
    public UserInfor getUserInforByDeviceId(String deviceId) {

        try {

            return dbUtils.findFirst(Selector.from(UserInfor.class).where("device_id", "=", deviceId));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新 用户的非空字段
     *
     * @param userInfor
     * @return
     */
    public boolean saveOrUpdate(UserInfor userInfor) throws Exception {

        if (userInfor != null) {

            if (userInfor.getId() != 0 && getById(userInfor.getId()) == null) {

                //保存
                try {
                    dbUtils.save(userInfor);
                } catch (DbException e) {
                    e.printStackTrace();

                    return false;
                }
            } else {

                try {
                    UserInfor userInfor1 = dbUtils.findFirst(Selector.from(UserInfor.class).where("id", "=", userInfor.getId()));
                    if (userInfor.getAddr() != null && userInfor.getAddr().trim().length() > 0) {

                        userInfor1.setAddr(userInfor.getAddr());
                    }

                    if (userInfor.getArea() != null && userInfor.getArea().trim().length() > 0) {

                        userInfor1.setArea(userInfor.getArea());
                    }

                    if (userInfor.getAreaIds() != null && userInfor.getAreaIds().trim().length() > 0) {

                        userInfor1.setAreaIds(userInfor.getAreaIds());
                    }

                    if (userInfor.getHeadIconLocalPath() != null && userInfor.getHeadIconLocalPath().trim().length() > 0) {

                        userInfor1.setHeadIconLocalPath(userInfor.getHeadIconLocalPath());
                    }

                    if (userInfor.getService() != null && userInfor.getService().trim().length() > 0) {

                        userInfor1.setService(userInfor.getService());
                    }

                    if (userInfor.getSpecialty() != null && userInfor.getSpecialty().trim().length() > 0) {

                        userInfor1.setSpecialty(userInfor.getSpecialty());
                    }

                    userInfor1.setAuth(userInfor.isAuth());


                    if (userInfor.getWork() != null && userInfor.getWork().trim().length() > 0) {

                        userInfor1.setWork(userInfor.getWork());
                    }

                    if (userInfor.getLinkMan() != null && userInfor.getLinkMan().trim().length() > 0) {

                        userInfor1.setLinkMan(userInfor.getLinkMan());
                    }

                    if (userInfor.getName() != null && userInfor.getName().trim().length() > 0) {

                        userInfor1.setName(userInfor.getName());
                    }

                    if (userInfor.getProfessionalTitle() != null && userInfor.getProfessionalTitle().trim().length() > 0) {

                        userInfor1.setProfessionalTitle(userInfor.getProfessionalTitle());
                    }

                    if (userInfor.getQrCodeImgLocalPath() != null && userInfor.getQrCodeImgLocalPath().trim().length() > 0) {

                        userInfor1.setQrCodeImgLocalPath(userInfor.getQrCodeImgLocalPath());
                    }

                    if (userInfor.getTel() != null && userInfor.getTel().trim().length() > 0) {

                        userInfor1.setTel(userInfor.getTel());
                    }

                    if (userInfor.getCompType() != 0) {

                        userInfor1.setCompType(userInfor.getCompType());
                    }


                    userInfor1.setUpdateDate(new Date().getTime());

                    dbUtils.saveOrUpdate(userInfor1);
                } catch (DbException e) {
                    e.printStackTrace();
                    throw new Exception(e);
                }
            }
        }
        return true;
    }

}
