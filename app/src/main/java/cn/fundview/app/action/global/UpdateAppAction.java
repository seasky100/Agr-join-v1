package cn.fundview.app.action.global;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.Installation;

/**
 * 项目名称：Agr-join-v1
 * 类描述： 更新app
 * 创建人：lict
 * 创建时间：2015/11/24 0024 上午 11:43
 * 修改人：lict
 * 修改时间：2015/11/24 0024 上午 11:43
 * 修改备注：
 */
public class UpdateAppAction{

    private Context context;
    private int versionCode;
    private static final String VERISON_URL = Constants.DOWN_SERVICE;
    private static final String APK_URL = Constants.APK_DOWN_PATH;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (msg.what == 1) {

                // 显示下载文件
                showUpdatePage(context);
            } else if (msg.what == 2) {

                // 显示安装
                showInstallApp(context);
            } else if (msg.what == 3) {

                // 显示更新失败
                Toast.makeText(context, "更新失败,请检查你的网络连接", Toast.LENGTH_LONG)
                        .show();
                // 删除apk文件
                File file = new File(DeviceConfig.getSysPath(context) + Constants.VERSION_PATH);
                if (file.exists()) {

                    file.delete();
                }

                File file1 = new File(DeviceConfig.getSysPath(context) + Constants.APK_PATH);
                if (file1.exists()) {

                    file1.delete();
                }

            }else if (msg.what == 4) {

                // 当前已是最新版本
                Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_LONG)
                        .show();
            }
        }
    };

    public UpdateAppAction(Context context) {

        this.context = context;
    }

    public void execute() {

        versionCode = Installation.getVersionCode(context);

        down();       //异步处理
    }

    private void down() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                startUpdateApp(context);
            }
        }).start();
    }

    private void startUpdateApp(Context context) {


        if (downFile(VERISON_URL, DeviceConfig.getSysPath(context)
                + Constants.VERSION_PATH)) {
            handleVersionXmlData(context);
        }
    }

    private boolean isHavingFile(String filePath) {

        File temp = new File(filePath);
        if (temp.exists()) {

            Log.d(Constants.TAG, "文件存在");
            return true;
        } else {

            Log.d(Constants.TAG, "文件不存在");
            return false;
        }
    }

    // 安装app
    private boolean installApp(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {

                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            System.out.println(file.getAbsolutePath());
            System.out.println("uri == null " + (uri == null));
            System.out.println(uri);
            intent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    private boolean downFile(String urlStr, String filePath) {

        boolean result = false;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);
            System.out.println("download Url = " + urlStr);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            System.out
                    .println("responseCode = " + connection.getResponseCode());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK
                    && connection.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {

                if (inputStream != null) {

                    inputStream.close();
                    connection.disconnect();
                }
                return false;
            } else {

                if (inputStream != null) {

                    System.out.println("连接成功,获得输入流,大小是 : "
                            + inputStream.available());
                    File file = write2FileFromInput(filePath, inputStream);

                    if (file == null) {

                        return false;
                    } else {

                        return true;
                    }
                } else {

                    return false;
                }
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = false;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = false;
        } finally {

            if (inputStream != null) {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // 处理version.xml 文件中的数据
    private void handleVersionXmlData(Context context) {

        File file = new File(DeviceConfig.getSysPath(context)
                + Constants.VERSION_PATH);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Map<String, String> versionData = parseVersionXml(inputStream);
            if (versionData != null) {

                String version = versionData.get("version");
                if (version != null) {

                    int versionCode = Integer.parseInt(version);
                    if (versionCode > Installation.getVersionCode(context)) {

                        // 需要执行更新
                        if (isHavingFile(DeviceConfig.getSysPath(context) + Constants.APK_PATH)) {

                            // 如果apk 文件存在的直接安装
                            // 直接安装
                            handler.sendEmptyMessage(2);
                        } else {

                            handler.sendEmptyMessage(1);
                        }
                    }else{

                        handler.sendEmptyMessage(4);        //当前意识最新版本
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (inputStream != null) {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<String, String> parseVersionXml(InputStream inputStream)
            throws Exception {

        Map<String, String> map = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "utf-8");
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:
                    if ("data".equals(parser.getName())) {
                        map = new HashMap<>();
                    } else if ("version".equals(parser.getName())) {

                        String version = parser.nextText();
                        map.put("version", version);
                    } else if ("down".equals(parser.getName())) {

                        String downPath = parser.nextText();
                        map.put("downPath", downPath);
                    }
                    break;
            }
            event = parser.next();
        }

        inputStream.close();
        return map;
    }

    private File write2FileFromInput(String filePath, InputStream inputStream) {

        File file = null;
        OutputStream outputStream = null;
        try {
            if (inputStream != null) {

                file = new File(filePath);
                if (!file.getParentFile().exists()) {

                    file.getParentFile().mkdirs();
                }

                outputStream = new FileOutputStream(file);
                byte[] bs = new byte[1024 * 4];
                int length = 0;
                while ((length = inputStream.read(bs)) != -1) {

                    System.out.println("length = " + length);
                    outputStream.write(bs, 0, length);
                }
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(Constants.TAG, e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(Constants.TAG, e.getMessage());
        } finally {

            if (outputStream != null) {

                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    // 显示下载
    private void showUpdatePage(final Context context) {

        new AlertDialog.Builder(context).setTitle("更新提示")
                .setMessage("最新的\"科企对接应用\"更新了, 快去更新吧")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        updateApp(context);
                    }
                }).setNegativeButton("取消", null).show();
    }

    // 显示安装
    private void showInstallApp(final Context context) {

        new AlertDialog.Builder(context).setTitle("安装提示")
                .setMessage("最新的应用已经下载完成,请安装")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        installApp(DeviceConfig.getSysPath(context) + Constants.APK_PATH);
                    }
                }).setNegativeButton("取消", null).show();
    }


    private boolean delFile(String filePath) {

        File file = new File(filePath);
        if (file.exists()) {

            file.delete();
            return true;
        }
        return false;
    }

    private void updateApp(final Context context) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("正在下载");
        dialog.setMessage("请稍后...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 下载apk
                boolean success = downFile(APK_URL,
                        DeviceConfig.getSysPath(context) + Constants.APK_PATH);
                if (success) {

                    dialog.dismiss();
                    installApp(DeviceConfig.getSysPath(context)  + Constants.APK_PATH);
                } else {

                    dialog.cancel();
                    handler.sendEmptyMessage(3);// 显示更新失败
                }
            }
        }).start();
    }
}
