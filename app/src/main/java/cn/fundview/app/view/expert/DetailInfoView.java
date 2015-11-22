package cn.fundview.app.view.expert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import cn.fundview.app.action.expert.DetailInfoAction;
import cn.fundview.app.view.ABaseWebView;

/**
 * 企业介绍详细
 */
public class DetailInfoView extends ABaseWebView {

    public DetailInfoView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/company/detail-info.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        int expertId = intent.getIntExtra("expertId", 0);
        String lastModify = intent.getStringExtra("updateDate");// 最后修改时间
        String attr = intent.getStringExtra("attr");// 最后展示的属性

        DetailInfoAction detailInfoAction = new DetailInfoAction(context, this);
        detailInfoAction.execute(expertId, lastModify, attr);

    }

}
