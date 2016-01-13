package cn.fundview.app.view.expert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

import cn.fundview.app.action.expert.DetailAction;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.action.my.AttentUserAction;
import cn.fundview.app.activity.achv.DetailActivity;
import cn.fundview.app.activity.expert.DetailInfoActivity;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 专家详细view
 */
public class DetailView extends ABaseWebView{

    private Integer expertId;// 企业的帐号id

    public DetailView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/expert/detail.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        expertId = intent.getIntExtra("expertId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间
        DetailAction detailAction = new DetailAction(context, this);
        detailAction.execute(expertId, lastModify);

    }


    public void reloadPage() {
        this.loadUrl("file:///android_asset/page/expert/detail.html");
        Intent intent = ((Activity) context).getIntent();
        expertId = intent.getIntExtra("expertId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间
        DetailAction detailAction = new DetailAction(context, this);
        detailAction.execute(expertId, lastModify);
    }

    /**
     * 拨打电话
     **/
    @JavascriptInterface
    public void call(String tel) {

        CallAction callAction = new CallAction(context, this);
        callAction.parseParams(tel);
        callAction.execute();
    }


    /**
     * 跳转到专家成果详细页
     **/
    @JavascriptInterface
    public void openAchvDetail(int achvId, String achvName, String lastModify) {

        Bundle bundle = new Bundle();
        bundle.putInt("achvId", achvId);
        bundle.putString("achvName", achvName);
        bundle.putString("lastModify", lastModify);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 关注专家
     **/
    @JavascriptInterface
    public void attent(int expertId, int attentStatus) {


        AttentUserAction action = new AttentUserAction(context, this);
        action.execute(2, expertId, attentStatus);
    }

    /**
     * 分享到微信
     **/
    @JavascriptInterface
    public void shareWX() {
        //
        // ShareProjToWXAction action = new ShareProjToWXAction(context, this);
        // action.execute(projId);
    }

    /**
     * 设置标题栏
     **/
    @JavascriptInterface
    public void setTitle(final String title) {

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                searchTitleBarView.setCommonTitlebarRightBtn(title, null, false);
            }
        });
    }

    /**
     * 分享到微信
     **/
    @JavascriptInterface
    public void openChatPage(String expertId, String expertName) {
        //
        // ShareProjToWXAction action = new ShareProjToWXAction(context, this);
        // action.execute(projId);
    }

    @JavascriptInterface
    public void loadheadIcon(String logo) {

        FileTools.downFile(logo, DeviceConfig.getSysPath(context) + "/fundView/data/images/expert/logo/"  + logo.substring(logo.lastIndexOf("/") + 1), this);
    }

    /**
     * 显示专家的研究范围/介绍的详细内容
     **/
    @JavascriptInterface
    public void showMore(String attr, int expertId, String expertName, String updateDate) {


        Intent intent = new Intent(context, DetailInfoActivity.class);

        intent.putExtra("expertId", expertId);
        intent.putExtra("attr", attr);
        intent.putExtra("updateDate", updateDate);
        intent.putExtra("expertName", expertName);
        context.startActivity(intent);

    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {

        String logo = responseInfo.result.getPath();
        this.loadUrl("javascript:Page.loadhead('" + logo + "');");
    }

    @Override
    public void onClickLeftBtn() {

        ((Activity) context).setResult(Activity.RESULT_OK, ((Activity) context).getIntent());
        super.onClickLeftBtn();
    }

    @Override
    public void onKeyBack() {
        ((Activity) context).setResult(Activity.RESULT_OK, ((Activity) context).getIntent());
        super.onKeyBack();
    }
}
