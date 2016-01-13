package cn.fundview.app.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.DownLoadListener;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.file.VersionUtil;

/**
 * 后台服务,判断应用是否需要更新
 * 对安装包的删除不在使用广播
 */
public class UpdateService extends Service {

    public IBinder updateAppBinder;
    private Context context;
    private static final String VERISON_URL = Constants.DOWN_SERVICE;
    private static final String APK_URL = Constants.APK_DOWN_PATH;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (msg.what == 1) {

                // 有新版本
                if (FileTools.isHavingFile(DeviceConfig.getSysPath(context) + Constants.APK_PATH)) {

                    // 显示安装
                    showInstallApp(context);
                } else {
                    showUpdatePage(context);
                }
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

            } else {

                //最新版本
                // 删除apk文件
               deleteFile();

                File file1 = new File(DeviceConfig.getSysPath(context) + Constants.APK_PATH);
                if (file1.exists()) {

                    file1.delete();
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(Constants.TAG,
                "------------------UpdateService  onBind------------------------");

        if (updateAppBinder == null) {

            updateAppBinder = new UpdataAppBinder();
        }
        return updateAppBinder;
    }

    public class UpdataAppBinder extends Binder {

        public UpdateService getService() {

            return UpdateService.this;
        }
    }

    public void down(Context context1) {

        System.out.println("下载...");
        this.context = context1;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                startUpdateApp(context);
            }
        }).start();
    }

    /**
     * 下载 version.xml
     *
     * @param context
     */
    private void startUpdateApp(final Context context) {

        deleteFile();
        FileTools.downFile(VERISON_URL, DeviceConfig.getSysPath(context) + Constants.VERSION_PATH, new DownLoadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {

            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                handler.sendEmptyMessage(VersionUtil.handleVersionXmlData(context, responseInfo.result));
                deleteFile();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                System.out.println(msg);
                deleteFile();
            }
        });
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

    // 显示下载
    private void showUpdatePage(final Context context) {

        new AlertDialog.Builder(context).setTitle("更新提示")
                .setMessage("最新的\"科企对接应用\"更新了, 快去更新吧")
                .setPositiveButton("确定", new OnClickListener() {

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
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        installApp(DeviceConfig.getSysPath(context) + Constants.APK_PATH);
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void updateApp(final Context context) {

        FileTools.downFile(APK_URL, DeviceConfig.getSysPath(context) + Constants.APK_PATH, new DownLoadListener() {

            private ProgressDialog dialog;

            @Override
            public void onStart() {

                Log.d("测试线程", Thread.currentThread().getName());
                dialog = new ProgressDialog(context);
                dialog.setTitle("正在下载");
                dialog.setMessage("请稍后...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {

            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                dialog.dismiss();
                installApp(DeviceConfig.getSysPath(context) + Constants.APK_PATH);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                dialog.cancel();
                handler.sendEmptyMessage(3);// 显示更新失败
            }
        });
    }

    private void deleteFile() {

        //删除version.xml
        File file1 = new File(DeviceConfig.getSysPath(context) + Constants.VERSION_PATH );
        if (file1.exists()) {

            file1.delete();
        }
    }

}