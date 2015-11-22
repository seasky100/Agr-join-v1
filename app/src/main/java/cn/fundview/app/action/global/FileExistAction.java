package cn.fundview.app.action.global;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.view.ABaseWebView;

public class FileExistAction extends ABaseAction {

    public FileExistAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public boolean execute(String filePath) {

        return FileTools.isFileExist(filePath);
    }
}
