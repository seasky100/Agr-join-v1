package cn.fundview.app.view;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 文件上传 监听器
 */
public interface UploadListener {

    void onStart();

    void onLoading(long total, long current, boolean isUploading);

    void onSuccess(ResponseInfo<String> responseInfo);

    void onFailure(HttpException error, String msg);
}