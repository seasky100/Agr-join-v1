package cn.fundview.app.action.my;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.StringUtils;

/**
 * 加载我的头像
 **/
public class LoadMyIconAction {

    private Context context;
    private WebView webView;

    public LoadMyIconAction(Context context, WebView view) {
        this.context = context;
        this.webView = view;
    }

    public void execute(String callback) {

        int uid = ((Activity) context).getIntent().getIntExtra("uid", 0);
        if (uid == 0) {

            return;
        }

        String sysPathString = DeviceConfig.getSysPath(context);
        UserInfor userInfor = DaoFactory.getInstance(context).getUserInforDao().getById(uid);
        String myIcon = "";
        if (userInfor != null) {

            myIcon = userInfor.getHeadIconLocalPath();//本地路径
            if (!StringUtils.isBlank(myIcon)) {

                if (FileTools.isFileExist(myIcon)) {

                    handleResult(myIcon, callback);
                } else {
                    handleResult(null, callback);
                }
            } else {

                handleResult(null, callback);

            }
        } else {

            return;
        }


    }

    private void handleResult(final String path, final String callback) {

        System.out.println("更新个人头像的保存路径:" + path);
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String js = JsMethod.createJs("javascript:" + callback
                        + "(${path});", path);
                webView.loadUrl(js);
            }
        });
    }

}
