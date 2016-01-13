package cn.fundview.app.action.global;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.view.ABaseWebView;

public class DownIconAction extends ABaseAction {

    /**
     * 参数
     **/
    private String callback;
    private String localSave;
    private String fileName;
    private String downPath;

    /**
     * 结果
     **/
    private boolean result = false;

    public DownIconAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String callback, String localSave, String fileName, String downPath) {

        this.callback = callback;
        this.localSave = localSave;
        this.fileName = fileName;
        this.downPath = downPath;

        handle(true);
    }

    /**
     * 异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        FileTools tools = new FileTools();
        try {
            result = tools.saveDownFile(localSave, fileName, tools.doGet(downPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行处理结果
     **/
    @Override
    protected void doHandleResult() {

        if (result)
            webView.loadUrl("javascript:" + callback + "();");
    }

}
