package cn.fundview.app.action.global;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 获取图片路径和下载图片
 *
 * @author ouda
 */
public class FileLoadAction extends ABaseAction {

    /**
     * 参数
     **/
    private String downPath;
    private String saveLocalPath;
    private String fileName;
    private String oldName;


    /**
     * 结果
     **/
    private boolean result;

    public FileLoadAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String downPath, String saveLocalPath, String fileName, String oldFileName) {

        this.downPath = downPath;
        this.saveLocalPath = saveLocalPath;
        this.fileName = fileName;
        this.oldName = oldFileName;
        handle(true);

    }

    /**
     * 异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        FileTools tools = new FileTools();
        try {

            tools.saveDownFile(saveLocalPath, fileName, tools.doGet(downPath));
            //删除老图片
            if (oldName != null && oldName.trim() != "")
                FileTools.delFile(saveLocalPath + oldName);
            result = true;

        } catch (Exception e) {
            result = false;
        }

    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

//        if (result) {
//
//            webView.loadUrl("javascript:Page.onDownloadError();");
//        } else {

        webView.loadUrl("javascript:Page.onFinishDownload();");

//        }
    }
}
