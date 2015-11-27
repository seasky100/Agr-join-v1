package cn.fundview.app.action.global;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * 项目名称：Agr-join-v1
 * 类描述： 更新app
 * 创建人：lict
 * 创建时间：2015/11/24 0024 上午 11:43
 * 修改人：lict
 * 修改时间：2015/11/24 0024 上午 11:43
 * 修改备注：
 */
public class CheckVersionAction {

    private Context context;
    private int versionCode;
    private AsyncTaskCompleteListener listener;
    private static final String VERISON_URL = Constants.DOWN_SERVICE;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (msg.what == 1) {

                // 显示下载文件
                listener.complete(4,2,null);    //
            }
        }
    };

    public CheckVersionAction(Context context, AsyncTaskCompleteListener listener) {

        this.context = context;
        this.listener = listener;
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
                if (downFile(VERISON_URL, DeviceConfig.getSysPath(context)
                        + Constants.VERSION_PATH)) {
                    handleVersionXmlData(context);
                }
            }
        }).start();
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

                        handler.sendEmptyMessage(1);        //有新的版本
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
}
