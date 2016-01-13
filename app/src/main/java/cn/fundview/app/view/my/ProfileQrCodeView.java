package cn.fundview.app.view.my;

import android.content.Context;
import android.util.AttributeSet;

import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

/**
 * 我的二维码view
 */
public class ProfileQrCodeView extends ABaseWebView {

    public ProfileQrCodeView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/my/profile-qrcode.html");
    }

    @Override
    public void init() {

        UserInfor userInfor = DaoFactory.getInstance(context).getUserInforDao().getById(PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));
        String js = JsMethod.createJs("javascript:Page.initPage(${qrCodeImg},${logo},${username},${name});", userInfor.getQrCodeImgLocalPath(), userInfor.getHeadIconLocalPath(), userInfor.getAccount(),userInfor.getName());
        loadUrl(js);

    }


}
