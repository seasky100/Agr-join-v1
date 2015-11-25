package cn.fundview.app.view.fundProject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.achv.OwnerAction;
import cn.fundview.app.action.fundProject.FundProjectDetailAction;
import cn.fundview.app.view.ABaseWebView;

/**
 * 项目名称：Agr-join-v1
 * 类描述：融资项目详细
 * 创建人：lict
 * 创建时间：2015/11/24 0024 下午 5:16
 * 修改人：lict
 * 修改时间：2015/11/24 0024 下午 5:16
 * 修改备注：
 */
public class DetailView extends ABaseWebView{

    private Integer id; //id
    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.loadUrl("file:///android_asset/page/fundProject/detail.html");
    }

    @Override
    public void init() {

        Intent intent = ((Activity) context).getIntent();
        id = intent.getIntExtra("id", 0);
        FundProjectDetailAction detailAction = new FundProjectDetailAction(context, this);
        detailAction.execute(id);
    }

    /**
     * 查找成果对应的专家
     *
     * @param ownerId   拥有者id
     * @param ownerType 拥有者类型 1企业  2 专家
     */
    @JavascriptInterface
    public void findOwner(int ownerId, int ownerType, String ownerName) {

        OwnerAction action = new OwnerAction(context, this);
        action.execute(ownerId, ownerType, ownerName);
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
}
