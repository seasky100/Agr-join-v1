package cn.fundview.app.action.global;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.view.ABaseWebView;

public class ShareWXAction extends ABaseAction {

    public ShareWXAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    /**
     * 参数
     **/
    private int id;


    public void execute(int id) {

        this.id = id;
        this.showWaitDialog();
        handle(true);
    }

    /**
     * 异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        String drivePath = DeviceConfig.getSysPath(context);


//		String expertSavaPath = drivePath +DeviceConfig.EXPERT_SAVE_DIR;
//		String fileName = id + ".xml";
//
//		if (FileTools.isFileExist(expertSavaPath + fileName)) {
//
//			// 解析文件
//			ExpertDetail detail = parseExpertXml(expertSavaPath + fileName);
//
//			if (null != detail) {
//
//				ShareWeiXin weiXin = new ShareWeiXin(context);
//				String driver = DeviceConfig.getSysPath(context);
//				
//				String[] attr = detail.getLogo().split("/");
//				String logoName = attr[attr.length - 1];
//				String picPath = driver +"/fundview/data/images/expert/logo/" + logoName;
//				weiXin.shareArticle(detail.getName(), detail.getIntro(), "", picPath);
//			}
//		}
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

        this.closeWaitDialog();
    }

//	private ExpertDetail parseExpertXml(String filePath) {
//
//		ExpertDetail expertDetail = null;
//		try {
//			expertDetail = XMLParse.parseExpertDetail(new FileInputStream(filePath));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return expertDetail;
//	}
}
