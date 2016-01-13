package cn.fundview.app.tool.file;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

/**
 * 项目名称：Agr-join-v1
 * 类描述： 下载/上传 监听器
 * 创建人：lict
 * 创建时间：2016/1/13 0013 上午 11:21
 * 修改人：lict
 * 修改时间：2016/1/13 0013 上午 11:21
 * 修改备注：
 */
public interface DownLoadListener {

    void onStart();

    void onLoading(long total, long current, boolean isUploading);

    void onSuccess(ResponseInfo<File> responseInfo);

    void onFailure(HttpException error, String msg);
}
