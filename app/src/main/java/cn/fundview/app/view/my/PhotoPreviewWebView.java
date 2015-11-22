package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import cn.fundview.app.action.my.LoadMyIconAction;
import cn.fundview.app.action.my.SaveIconAction;
import cn.fundview.app.activity.my.ProfileIconActivity;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.UploadListener;

public class PhotoPreviewWebView extends ABaseWebView implements UploadListener {

    private String fileSavePath;
    private String fileName;
    private String tempPath;

    public PhotoPreviewWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/my/profile-icon-preview.html");
    }

    @Override
    public void init() {

        LoadMyIconAction action = new LoadMyIconAction(context, this);
        action.execute("Page.picShow");
    }

    /**
     * 保存头像
     **/
    @JavascriptInterface
    public void savePic(String width, String height) {

        SaveIconAction action = new SaveIconAction(context, this);
        action.execute(fileSavePath, fileName, tempPath, width, height);
    }

    public void showPic(String path) {

        tempPath = path;
        this.loadUrl("javascript:Page.picShow(\"" + path + "\");");
    }

    public void setPath(String fileSavePath, String fileName) {
        this.fileSavePath = fileSavePath;
        this.fileName = fileName;
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {

    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {

        loadUrl("javascript:Page.hideLoading()");
        Intent intent = new Intent(context, ProfileIconActivity.class);
        String passString = "saveSuccess";
        intent.putExtra("takePic", passString);
        ((Activity) context).setResult(Activity.RESULT_OK,
                intent);
        ((Activity) context).finish();
    }

    @Override
    public void onFailure(HttpException error, String msg) {

    }
}
