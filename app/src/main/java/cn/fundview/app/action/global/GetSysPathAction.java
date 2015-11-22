package cn.fundview.app.action.global;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.view.ABaseWebView;

/**
 * 获取系统的文件路径
 *
 * @author ouda
 */
public class GetSysPathAction extends ABaseAction {

    /**
     * 处理结果
     **/
    private String result;

    public GetSysPathAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public String execute() {

        if (DeviceConfig.isExistExtendStorage()) {

            result = DeviceConfig.getExtendStoragePath(context);
        } else {

            result = DeviceConfig.getLocalStoragePath(context);
        }

        return result;
    }
}
