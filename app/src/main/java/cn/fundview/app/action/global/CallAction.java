package cn.fundview.app.action.global;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebView;

/**
 * 打电话
 *
 * @author ouda
 */
public class CallAction {

    public static final String RRGIST_NAME = "call";
    public static final int CallRequestCode = 123;

    // 参数
    private String phoneNum;
    private Context context;

    //	/private WebView webView;
    public CallAction(Context context, WebView webView) {

        this.context = context;
        //this.webView = webView;
    }

    public Object execute() {

        //跳转到拨号界面
        // Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum.replaceAll("-", "")));
        // context.startActivity(intent);
        //直接拨打号码
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.replaceAll("-", "")));

        //检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                String[] permissions = {Manifest.permission.CALL_PHONE};

//                boolean flag = ((Activity) context).shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE);

//                if (flag) {

                ((Activity) context).getIntent().putExtra("tel", phoneNum);
                ((Activity) context).requestPermissions(permissions, CallRequestCode);

//                }

            } else {
                context.startActivity(intent);
            }
        } else {

            context.startActivity(intent);
        }

        return "";
    }

    public void parseParams(String... args) {

        phoneNum = args[0];
    }

    public String pkgResult(Object result) {

        return "ok";
    }

}
