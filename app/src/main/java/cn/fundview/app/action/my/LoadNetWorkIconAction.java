package cn.fundview.app.action.my;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;

public class LoadNetWorkIconAction {

    private Context context;
    private WebView webView;

    public LoadNetWorkIconAction(Context context, WebView view) {
        this.context = context;
        this.webView = view;
    }

    public void execute(final String savePath, final String downUrl, final String defaultPath, final String callback) {

        String fileName = "";

        if (null != downUrl && !"".equals(downUrl)) {
            fileName = downUrl.substring(downUrl.lastIndexOf("/") + 1, downUrl.length());
        }

        if ("".equals(fileName)) {

            handleResult(defaultPath, callback);
        } else {

            String drivePath = DeviceConfig.getSysPath(context);
            final String filePath = drivePath + fileName;
            final String name = fileName;

            if (FileTools.isFileExist(filePath)) {

                handleResult(filePath, callback);
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // 下载文件
                            FileTools tools = new FileTools();
                            boolean downResult = tools.saveDownFile(savePath, name, tools.doGet(downUrl));
                            if (downResult) {
                                handleResult(filePath, callback);
                            } else {
                                handleResult(defaultPath, callback);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }

    }

    private void handleResult(final String path, final String callback) {

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + callback + "(\"" + path + "\")");
            }
        });
    }

}
