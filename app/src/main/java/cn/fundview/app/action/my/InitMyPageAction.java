package cn.fundview.app.action.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.dao.UserInforDao;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.Installation;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 初始化加载我的资料
 **/
public class InitMyPageAction extends ABaseAction {

    /**
     * 处理结果
     **/
    private String myIconPath;
    private String name;//用户真实名
    private String username;// 用户名

    //result
    private int uid;

    public InitMyPageAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute() {

        handle(true);
    }

    @Override
    protected void doAsynchHandle() {
        super.doAsynchHandle();

        //获取登录用户信息
        int type = PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY);
        //根据用户的类型和id 获取用户的信息
        UserInfor profile = null;

        UserInforDao userInforDao = DaoFactory.getInstance(context).getUserInforDao();

        uid = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);

        if (Constants.LOGIN_STATUS == PreferencesUtils.getInt(context, Constants.LOGIN_STATUS_KEY)) {

            //获取我的头像  从服务器端查询个人信息
            if (NetWorkUtils.checkNetwork(context)) {

                // 首先从网上下载相应的json信息  uid 用户id
                Map<String, String> param = new HashMap<>();
                param.put("accountId", uid + "");
                String url = "";
                switch (type) {

                    case UserInfor.EXPERT_TYPE:
                        url = cn.fundview.app.domain.webservice.util.Constants.MY_PROFILE_EXPERT_URL;
                        break;
                    case UserInfor.PERSON_TYPE:
                        url = cn.fundview.app.domain.webservice.util.Constants.MY_PROFILE_PERSONAL_URL;
                        break;
                    case UserInfor.COMPANY_TYPE:
                        url = cn.fundview.app.domain.webservice.util.Constants.MY_PROFILE_COMPANY_URL;
                        break;
                }

                try {

                    ResultBean resultBean = JSONTools.parseResult(RService.doPostSync(param, url));

                    if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                        profile = JSON.parseObject(resultBean.getResult(), UserInfor.class);
                        //获取数据成功
                        // 根据更新时间判断是否需要修该本地
                        UserInfor localUserInfor = userInforDao.getById(uid);

                        if (localUserInfor == null) {

                            // 保存
                            // 将网络logo 保存成本地的logo
                            FileTools tools = new FileTools();

                            String headIcon = profile.getHeadIcon();
                            String qrcodeImg = profile.getQrCodeImg();
                            if (!StringUtils.isBlank(headIcon)) {

                                String fileName = headIcon.substring(headIcon.lastIndexOf("/") + 1);
                                try {
                                    boolean result = FileTools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName,
                                            tools.doGet(profile.getHeadIcon()));
                                    if (result) {
                                        profile.setHeadIconLocalPath(DeviceConfig.getSysPath(context)
                                                + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (!StringUtils.isBlank(qrcodeImg)) {

                                String fileName = qrcodeImg.substring(qrcodeImg.lastIndexOf("/") + 1);
                                try {
                                    boolean result = FileTools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName,
                                            tools.doGet(profile.getQrCodeImg()));
                                    if (result) {
                                        profile.setQrCodeImgLocalPath(DeviceConfig.getSysPath(context)
                                                + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            //个人角色特殊处理
                            if (profile.getType() == UserInfor.PERSON_TYPE) {
                                profile.setAddr(profile.getAddress());
                            }
                            profile.setDeviceId(Installation.getDriverId(context));
                            profile.setAccount(PreferencesUtils.getString(context, Constants.ACCOUNT_KEY));//设置登录账号
                            profile.setPassword(PreferencesUtils.getString(context, Constants.PASSWORD_KEY));//设置用户密码
                            profile.setType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));//设置用户类型
                            userInforDao.save(profile);
                        } else {//有内容更新的时候调用

                            // 更新
                            profile.setId(localUserInfor.getId());
                            FileTools tools = new FileTools();

                            String headIcon = profile.getHeadIcon();
                            String qrcodeImg = profile.getQrCodeImg();
                            if (!StringUtils.isBlank(headIcon) && !headIcon.equals(localUserInfor.getHeadIcon())) {

                                String fileName = headIcon.substring(headIcon.lastIndexOf("/") + 1);
                                try {
                                    boolean result = FileTools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName, tools.doGet(headIcon));
                                    if (result) {
                                        profile.setHeadIconLocalPath(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                    } else {

                                        profile.setHeadIconLocalPath(localUserInfor.getHeadIconLocalPath());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    profile.setHeadIconLocalPath(localUserInfor.getHeadIconLocalPath());
                                }
                            }

                            if (!StringUtils.isBlank(qrcodeImg) && !qrcodeImg.equals(localUserInfor.getQrCodeImg())) {

                                String fileName = qrcodeImg.substring(qrcodeImg.lastIndexOf("/") + 1);
                                try {
                                    boolean result = FileTools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName,
                                            tools.doGet(profile.getQrCodeImg()));
                                    if (result) {
                                        profile.setQrCodeImgLocalPath(DeviceConfig.getSysPath(context)
                                                + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                    } else {

                                        profile.setQrCodeImgLocalPath(localUserInfor.getQrCodeImgLocalPath());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    profile.setQrCodeImgLocalPath(localUserInfor.getQrCodeImgLocalPath());
                                }
                            }

                            //个人角色特殊处理
                            if (profile.getType() == UserInfor.PERSON_TYPE) {
                                profile.setAddr(profile.getAddress());
                            }

                            profile.setDeviceId(Installation.getDriverId(context));
                            profile.setAccount(PreferencesUtils.getString(context, Constants.ACCOUNT_KEY));//设置登录账号
                            profile.setPassword(PreferencesUtils.getString(context, Constants.PASSWORD_KEY));//设置用户密码
                            profile.setType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));//设置用户类型
                            userInforDao.saveOrUpdate(profile);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            profile = DaoFactory.getInstance(context).getUserInforDao().getById(uid);

            if (profile != null) {

                myIconPath = profile.getHeadIconLocalPath();// 获取我的头像
                if (null == myIconPath || !FileTools.isFileExist(myIconPath)) {

                    myIconPath = "../img/default-head-icon.jpg";
                }
                name = profile.getName();
                username = PreferencesUtils.getString(context, Constants.ACCOUNT_KEY);
            }
        } else {

            myIconPath = "../img/default-head-icon.jpg";
            username = "";
            name = "";
        }

    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        String myIconJs = JsMethod.createJs("javascript:Page.setMyIcon(${path});", myIconPath);
        webView.loadUrl(myIconJs);
//
        String profileJs = JsMethod.createJs(
                "javascript:Page.setProfile(${username},${name},${type});", username, name, PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));
        webView.loadUrl(profileJs);

        //提醒版本更新
        if(PreferencesUtils.getInt(context, Constants.NEW_VERSION) == 1) {

            //有新的版本
            webView.loadUrl("javascript:Page.setNewVersion(1);");
        }else {

            webView.loadUrl("javascript:Page.setNewVersion(0);");
        }
    }
}
