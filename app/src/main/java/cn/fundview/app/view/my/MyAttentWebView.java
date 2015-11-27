package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.company.CompanyListAction;
import cn.fundview.app.action.my.AttentListAction;
import cn.fundview.app.activity.company.DetailActivity;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;

/**
 * 我的关注view
 */
public class MyAttentWebView extends ABaseWebView {

    private int page = 1;

    public MyAttentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        int type = ((Activity) context).getIntent().getIntExtra("attent-type", 1);//被关注者类型
        if (type == 1) {

            this.loadUrl("file:///android_asset/page/my/my-attent-company.html");
        } else if (type == 2) {

            this.loadUrl("file:///android_asset/page/my/my-attent-expert.html");
        }

    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        loadAttentionListByPage();
    }

    /**
     * 加载下一页
     **/
    @JavascriptInterface
    public void nextPage() {

        loadAttentionListByPage();
    }

    /**
     * 打开企业详细页面
     **/
    @JavascriptInterface
    public void openDetail(int compId, String compName, String lastModify, int type) {

        if (type == 1) {

            Intent intent = new Intent(context, DetailActivity.class);

            intent.putExtra("compName", compName);
            intent.putExtra("compId", compId);
            intent.putExtra("lastModify", lastModify);
            intent.putExtra("pageSize", DensityUtil.px2dip(context, this.getHeight()) / cn.fundview.app.domain.webservice.util.Constants.COMPANY_ITEM_HEIGHT + 1);
            intent.putExtra("page", page);
            ((Activity) context).startActivityForResult(intent, 1);
        } else {

            Intent intent = new Intent(context, cn.fundview.app.activity.expert.DetailActivity.class);
            intent.putExtra("expertName", compName);
            intent.putExtra("expertId", compId);
            intent.putExtra("lastModify", lastModify);
            intent.putExtra("pageSize", DensityUtil.px2dip(context, this.getHeight()) / cn.fundview.app.domain.webservice.util.Constants.EXPERT_ITEM_HEIGHT + 1);
            intent.putExtra("page", page);
            ((Activity) context).startActivityForResult(intent, 2);
        }

    }

    private void loadAttentionListByPage() {

        int type = ((Activity) context).getIntent().getIntExtra("attent-type", 1);//被关注者类型
        int pageSize = 0;

        if (type == 1) {

            pageSize = DensityUtil.px2dip(context, this.getHeight()) / cn.fundview.app.domain.webservice.util.Constants.COMPANY_ITEM_HEIGHT + 1;
        } else if (type == 2) {

            pageSize = DensityUtil.px2dip(context, this.getHeight()) / cn.fundview.app.domain.webservice.util.Constants.EXPERT_ITEM_HEIGHT + 1;
        }

        Integer uid = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);//关注者id
        AttentListAction attentAction = new AttentListAction(context, this);
        attentAction.execute(type, uid, page, pageSize, false);
        this.page = page + 1;
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }
}
