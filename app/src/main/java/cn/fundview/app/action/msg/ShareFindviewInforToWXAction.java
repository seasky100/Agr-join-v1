package cn.fundview.app.action.msg;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.ShareWeiXin;
import cn.fundview.app.view.ABaseWebView;

public class ShareFindviewInforToWXAction extends ABaseAction {

    /**
     * 参数
     **/
    private int id;

    public ShareFindviewInforToWXAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(int id) {

        this.id = id;
        this.showWaitDialog();
        handle(true);
    }

    /**
     * 异步处理
     **/
    protected void doAsynchHandle() {

//		FundviewInfor infor = DaoFactory.getInstance(context).getFindviewInforDao().getInfor(id);
//		if (null != infor) {
//
//			ShareWeiXin weiXin = new ShareWeiXin(context);
//
//			String driver;
//			if (DeviceConfig.isExistExtendStorage()) {
//				driver = DeviceConfig.getExtendStoragePath();
//			} else {
//				driver = DeviceConfig.getLocalStoragePath(context);
//			}
//			String[] attr = infor.getLogo().split("/");
//			String fileName = attr[attr.length - 1];
//			String picPath = driver + Constants.FINDVIEW_INFOR_IMG_PATH + fileName;
//			weiXin.shareArticle(infor.getTitle(), infor.getIntroduction(), "http://m.findview.cn/msg/detail.action?id=" + id, picPath);
//		}
    }

    /**
     * 处理结果
     **/
    protected void doHandleResult() {

        this.closeWaitDialog();
    }

}
