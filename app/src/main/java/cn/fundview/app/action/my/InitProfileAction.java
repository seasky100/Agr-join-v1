package cn.fundview.app.action.my;

import android.app.Activity;
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
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.Installation;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

public class InitProfileAction extends ABaseAction {

    /**
     * 处理结果
     ***/
    private UserInfor userInfor;
    private String result;

    public InitProfileAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute() {
        handle(true);
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        this.showWaitDialog();
        int uid = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);
        int type = PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY);
        final UserInforDao userInforDao = DaoFactory.getInstance(context).getUserInforDao();

        // 检查网络
        if (NetWorkConfig.checkNetwork(context)) {

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

                    userInfor = JSON.parseObject(resultBean.getResult(), UserInfor.class);

                    switch (type) {

                        case UserInfor.EXPERT_TYPE:

                            userInfor.setQrCodeImg(cn.fundview.app.domain.webservice.util.Constants.QR_CODE_IMG_PATH + "/thumb/qrcode/expert/" + uid + ".jpg");
                            break;
                        case UserInfor.PERSON_TYPE:
                            break;
                        case UserInfor.COMPANY_TYPE:

                            userInfor.setQrCodeImg(cn.fundview.app.domain.webservice.util.Constants.QR_CODE_IMG_PATH + "/thumb/qrcode/comp/" + uid + ".jpg");
                            break;
                    }
                    //获取数据成功
                    // 根据更新时间判断是否需要修该本地
                    UserInfor localUserInfor = userInforDao.getById(uid);

                    if (localUserInfor == null) {
                        // 保存
                        // 将网络logo 保存成本地的logo
                        FileTools tools = new FileTools();

                        String headIcon = userInfor.getHeadIcon();
                        String qrcodeImg = userInfor.getQrCodeImg();
                        if (!StringUtils.isBlank(headIcon)) {

                            String fileName = headIcon.substring(headIcon.lastIndexOf("/") + 1);
                            try {
                                boolean result = tools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName,
                                        tools.doGet(userInfor.getHeadIcon()));
                                if (result) {
                                    userInfor.setHeadIconLocalPath(DeviceConfig.getSysPath(context)
                                            + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (!StringUtils.isBlank(qrcodeImg)) {

                            String fileName = qrcodeImg.substring(qrcodeImg.lastIndexOf("/") + 1);
                            try {
                                boolean result = tools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName,
                                        tools.doGet(userInfor.getQrCodeImg()));
                                if (result) {
                                    userInfor.setQrCodeImgLocalPath(DeviceConfig.getSysPath(context)
                                            + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //个人角色特殊处理
                        if (userInfor.getType() == UserInfor.PERSON_TYPE) {
                            userInfor.setAddr(userInfor.getAddress());
                        }
                        userInfor.setDeviceId(Installation.getDriverId(context));
                        userInfor.setAccount(PreferencesUtils.getString(context, Constants.ACCOUNT_KEY));//设置登录账号
                        userInfor.setPassword(PreferencesUtils.getString(context, Constants.PASSWORD_KEY));//设置用户密码
                        userInfor.setType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));//设置用户类型
                        userInforDao.save(userInfor);
                    } else {//有内容更新的时候调用

                        // 更新
                        userInfor.setId(localUserInfor.getId());
                        FileTools tools = new FileTools();

                        String headIcon = userInfor.getHeadIcon();
                        String qrcodeImg = userInfor.getQrCodeImg();
                        if (!StringUtils.isBlank(headIcon) && headIcon != localUserInfor.getHeadIcon()) {

                            String fileName = headIcon.substring(headIcon.lastIndexOf("/") + 1);
                            try {
                                boolean result = tools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName, tools.doGet(headIcon));
                                if (result) {
                                    userInfor.setHeadIconLocalPath(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                } else {

                                    userInfor.setHeadIconLocalPath(localUserInfor.getHeadIconLocalPath());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                userInfor.setHeadIconLocalPath(localUserInfor.getHeadIconLocalPath());
                            }
                        }

                        if (!StringUtils.isBlank(qrcodeImg)) {

                            String fileName = qrcodeImg.substring(qrcodeImg.lastIndexOf("/") + 1);
                            try {
                                boolean result = tools.saveDownFile(DeviceConfig.getSysPath(context) + Constants.MY_ICON_SAVE_DIR + uid + "/", fileName,
                                        tools.doGet(userInfor.getQrCodeImg()));
                                if (result) {
                                    userInfor.setQrCodeImgLocalPath(DeviceConfig.getSysPath(context)
                                            + Constants.MY_ICON_SAVE_DIR + uid + "/" + fileName);
                                } else {

                                    userInfor.setQrCodeImgLocalPath(localUserInfor.getQrCodeImgLocalPath());
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                userInfor.setQrCodeImgLocalPath(localUserInfor.getQrCodeImgLocalPath() + userInfor.getCompType());
                            }
                        }

                        //个人角色特殊处理
                        if (userInfor.getType() == UserInfor.PERSON_TYPE) {
                            userInfor.setAddr(userInfor.getAddress());
                        }

                        userInfor.setDeviceId(Installation.getDriverId(context));
                        userInfor.setAccount(PreferencesUtils.getString(context, Constants.ACCOUNT_KEY));//设置登录账号
                        userInfor.setPassword(PreferencesUtils.getString(context, Constants.PASSWORD_KEY));//设置用户密码
                        userInfor.setType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));//设置用户类型
                        userInforDao.saveOrUpdate(userInfor);
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ToastUtils.show(context, "网络连接异常...");
                    }
                });
            }
        } else {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ToastUtils.show(context, "网络连接异常...");
                }
            });

        }

        userInfor = userInforDao.getById(uid);
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (userInfor != null) {

            String infor = userInfor.getType() == UserInfor.EXPERT_TYPE ? userInfor.getSpecialty() : userInfor.getType() == UserInfor.COMPANY_TYPE ? userInfor.getService() : userInfor.getWork();
            String js = JsMethod.createJs("javascript:Page.initPage(${icon}, ${name}, ${username}, ${area}, ${areaIds}, " +
                            "${addr}, ${tel}, ${qrCode}, ${linkMan}, ${type}, ${auth}, ${professionalTitle}," +
                            "${infor}, ${compType});",
                    userInfor.getHeadIconLocalPath(), userInfor.getName(),
                    userInfor.getAccount(),
                    userInfor.getArea(), userInfor.getAreaIds(), userInfor.getAddr(),
                    userInfor.getTel(), userInfor.getQrCodeImgLocalPath(), userInfor.getLinkMan(),
                    userInfor.getType(), userInfor.isAuth(), userInfor.getProfessionalTitle(), infor, userInfor.getCompType());
            System.out.println(js);
            webView.loadUrl(js);
        }
        this.closeWaitDialog();
    }
}
