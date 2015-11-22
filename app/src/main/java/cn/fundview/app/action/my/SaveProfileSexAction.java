package cn.fundview.app.action.my;

import android.app.Activity;
import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.view.ABaseWebView;

public class SaveProfileSexAction extends ABaseAction {

    /** 参数 **/
    //private int sex;

    /**
     * 处理结果
     ***/
    private boolean result;

    public SaveProfileSexAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(int sex) {

        //this.sex = sex;
        handle(true);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doAsynchHandle() {
//
//		result = DaoFactory.getInstance(context).getProfileDao().saveProfile(Constants.MY_SEX_LABLE, String.valueOf(sex));
//		// 上传到网络
//		RService.getInstance().updateProfile(Util.getUserId(context), Constants.MY_SEX_LABLE, String.valueOf(sex));
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (result)
            ((Activity) context).finish();
        else
            webView.loadUrl("javascript:Page.hintFail();");
    }

}
