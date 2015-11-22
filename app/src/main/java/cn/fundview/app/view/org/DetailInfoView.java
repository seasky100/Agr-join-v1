package cn.fundview.app.view.org;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import cn.fundview.app.action.org.DetailInfoAction;
import cn.fundview.app.view.ABaseWebView;

/**
 * 机构介绍详细
 */
public class DetailInfoView extends ABaseWebView {

    public DetailInfoView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/company/detail-info.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        int orgId = intent.getIntExtra("orgId", 0);
        String lastModify = intent.getStringExtra("updateDate");// 最后修改时间

        DetailInfoAction detailInfoAction = new DetailInfoAction(context, this);
        detailInfoAction.execute(orgId, lastModify);

    }

}
