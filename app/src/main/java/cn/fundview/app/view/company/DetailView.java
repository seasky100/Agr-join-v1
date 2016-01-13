package cn.fundview.app.view.company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

import cn.fundview.app.action.company.DetailAction;
import cn.fundview.app.action.global.CallAction;
import cn.fundview.app.action.my.AttentUserAction;
import cn.fundview.app.activity.company.CompanyProductDetailActivity;
import cn.fundview.app.activity.company.DetailInfoActivity;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.view.ABaseWebView;

public class DetailView extends ABaseWebView {

    private Integer compId;// 企业的帐号id

    public DetailView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/company/detail.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        compId = intent.getIntExtra("compId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间

        //titleBarView.setTitleBar(true, "更多", name);

        DetailAction detailAction = new DetailAction(context, this);
        detailAction.execute(compId, lastModify);

    }


    public void reloadPage() {

        this.loadUrl("file:///android_asset/page/company/detail.html");
        Intent intent = ((Activity) context).getIntent();
        compId = intent.getIntExtra("compId", 0);
        String lastModify = intent.getStringExtra("lastModify");// 最后修改时间

        DetailAction detailAction = new DetailAction(context, this);
        detailAction.execute(compId, lastModify);

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
     * 异步下载企业logo
     *
     * @param logo 企业 logo url
     */
    @JavascriptInterface
    public void loadLogo(String logo) {

        FileTools.downFile(logo, DeviceConfig.getSysPath(context) + "/fundView/data/images/comp/logo/" + logo.substring(logo.lastIndexOf("/") + 1), this);
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {

        String logo = responseInfo.result.getPath();
        this.loadUrl("javascript:Page.loadhead('" + logo + "');");
    }

    /**
     * 打开需求详细
     **/
    @JavascriptInterface
    public void openRequDetail(int id, String title, String lastModify) {

        Bundle bundle = new Bundle();
        bundle.putInt("requId", id);
        bundle.putString("requName", title);
        bundle.putString("lastModify", lastModify);
        Intent intent = new Intent(context, cn.fundview.app.activity.requ.DetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 关注
     **/
    @JavascriptInterface
    public void attent(int compId, int attentStatus) {


        AttentUserAction action = new AttentUserAction(context, this);
        action.execute(1, compId, attentStatus);
    }

    /**
     * 打开企业产品详细
     *
     * @param id          产品id
     * @param productName 产品名
     * @param lastModify  产品信息更新时间
     */
    @JavascriptInterface
    public void openProductDetail(int id, String productName, String lastModify) {

        Bundle bundle = new Bundle();
        bundle.putInt("productId", id);
        bundle.putString("productName", productName);
        bundle.putString("lastModify", lastModify);
        Intent intent = new Intent(context, CompanyProductDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 显示企业的经营范围/介绍的详细内容
     **/
    @JavascriptInterface
    public void showMore(String attr, int compId, String compName, String updateDate) {


        Intent intent = new Intent(context, DetailInfoActivity.class);

        intent.putExtra("compId", compId);
        intent.putExtra("attr", attr);
        intent.putExtra("updateDate", updateDate);
        intent.putExtra("compName", compName);
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


    /**
     * 分享到微信
     **/
    @JavascriptInterface
    public void shareWX() {
        //
        // ShareProjToWXAction action = new ShareProjToWXAction(context, this);
        // action.execute(projId);
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
