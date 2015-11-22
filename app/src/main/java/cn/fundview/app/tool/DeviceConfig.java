package cn.fundview.app.tool;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class DeviceConfig {

    /**
     * SD卡是否存在
     *
     * @return boolean
     */
    public static boolean isExistExtendStorage() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 得到SD卡路径
     *
     * @return path
     */
    public static String getExtendStoragePath(Context context) {

        String path = "";
        if (isExistExtendStorage()) {

            File sdDir = Environment.getExternalStorageDirectory();
            path = sdDir.toString();
        }
        return path + "/";
    }

    /**
     * 获取手机存储的路径
     *
     * @param context 上下文
     * @return path
     */
    public static String getLocalStoragePath(Context context) {

        String path = context.getFilesDir().toString();
        return path + "/";
    }

    /**
     * 获取手机可以使用的存储路径  sd卡优先
     *
     * @param context 上下文
     * @return drivePath
     */
    public static String getSysPath(Context context) {
        String drivePath;
        if (isExistExtendStorage()) {
            drivePath = getExtendStoragePath(context);
        } else {
            drivePath = getLocalStoragePath(context);
        }
        return drivePath;
    }

    /**
     * 如果手机又自带的内存卡,同时还有自己安装的内存卡,返回自己安装的内存卡的跟路径(cdcard1),否则返回自带内存卡的路径(sdcard0)
     *
     * @param context
     * @return
     */
    public static String getExtendStorage(Context context) {

        //针对有的手机又内置的内存卡,并且又安装了自己的内存卡,此时,将优先选择自己的内存卡
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        String path = "";
        try {
            String[] volumePaths = (String[]) sm.getClass().getMethod("getVolumePaths", String.class).invoke(sm, String[].class);

            if (volumePaths != null && volumePaths.length > 0) {

                path = volumePaths[volumePaths.length - 1];
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return path + "/";
    }
}
