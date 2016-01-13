package cn.fundview.app.action.global;

import android.content.Context;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.DownLoadListener;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.file.VersionUtil;

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
    private static final String VERISON_URL = Constants.DOWN_SERVICE;

    public CheckVersionAction(Context context) {

        this.context = context;
    }

    public void execute() {

        //删除version.xml
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

                int flag = VersionUtil.handleVersionXmlData(context, responseInfo.result);
                if(flag == 1) {

                    //有新的版本号
                    PreferencesUtils.putInt(context, Constants.NEW_VERSION, 1);
                }else {

                    PreferencesUtils.putInt(context, Constants.NEW_VERSION, 0);
                }

                deleteFile();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println(msg);
                deleteFile();
            }
        });
    }

    private void deleteFile() {

        //删除version.xml
        File file1 = new File(DeviceConfig.getSysPath(context) + Constants.VERSION_PATH);
        if (file1.exists()) {

            file1.delete();
        }
    }
}
