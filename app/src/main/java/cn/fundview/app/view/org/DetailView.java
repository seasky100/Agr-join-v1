package cn.fundview.app.view.org;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.action.org.DetailAction;
import cn.fundview.app.activity.achv.DetailActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.activity.org.DetailInfoActivity;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 参展机构详细view
 */
public class DetailView extends ABaseWebView {

    private Integer orgId;// 机构帐号id

    public DetailView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/organ/detail.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        orgId = intent.getIntExtra("orgId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间
        DetailAction detailAction = new DetailAction(context, this);
        detailAction.execute(orgId, lastModify);

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
     * 关注项目
     **/
    @JavascriptInterface
    public void attentProj(int projId, String logo, String title, int comps,
                           int attent, String lastModify) {

        // AttentProjAction action = new AttentProjAction(context, this);
        // action.execute(projId, logo, title, comps, attent, lastModify);
    }

    /**
     * 异步下载企业logo
     *
     * @param logo 企业 logo url
     */
    @JavascriptInterface
    public void loadLogo(String logo) {

        FileTools.downFile(logo, DeviceConfig.getSysPath(context) + "/fundView/data/images/org/logo/" + logo.substring(logo.lastIndexOf("/") + 1), this);
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {

        String logo = responseInfo.result.getPath();
        this.loadUrl("javascript:Page.loadhead('" + logo + "');");
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
     * 跳转到专家成果详细页
     **/
    @JavascriptInterface
    public void openExpertDetail(int expertId, String expertName, String lastModify) {

        Bundle bundle = new Bundle();
        bundle.putInt("expertId", expertId);
        bundle.putString("expertName", expertName);
        bundle.putString("lastModify", lastModify);
        Intent intent = new Intent(context, cn.fundview.app.activity.expert.DetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示机构的介绍的详细内容
     **/
    @JavascriptInterface
    public void showMore(int orgId, String name, String updateDate) {


        Intent intent = new Intent(context, DetailInfoActivity.class);
        intent.putExtra("orgId", orgId);
        intent.putExtra("updateDate", updateDate);
        intent.putExtra("orgName", name);
        context.startActivity(intent);

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


    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

        Intent intent = new Intent(context, SearchHistoryActivity.class);
        context.startActivity(intent);
    }
}
