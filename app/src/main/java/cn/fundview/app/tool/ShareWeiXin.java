package cn.fundview.app.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import cn.fundview.app.tool.file.FileTools;

public class ShareWeiXin {

    private static final String APP_ID = "wx08a93267d1140996";
    private static final int THUMB_SIZE = 150;
    private IWXAPI api;

    // private int type;// 1为发送到朋友圈，2为和朋友分享

    public ShareWeiXin(Context context) {

        this.api = WXAPIFactory.createWXAPI(context, APP_ID);
        // this.api = WXAPIFactory.createWXAPI(context, APP_ID, false);
        this.api.registerApp(APP_ID);
    }

    public void shareArticle(String title, String desc, String url, String picPath) {

        if (FileTools.isFileExist(picPath)) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;

            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = desc;

            Bitmap bmp = BitmapFactory.decodeFile(picPath);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            // 发送类型：WXSceneTimeline为分享在朋友圈 WXSceneSession为发送给某人
            req.scene = SendMessageToWX.Req.WXSceneSession;// SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        }
    }

    private String buildTransaction(final String type) {

        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
