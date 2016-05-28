package cn.fundview.app.action;

import android.content.Context;

import cn.fundview.app.listener.NetWorkListener;
import cn.fundview.app.tool.NetWorkUtils;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.view.AsyncTaskCompleteListener;

/**
 * Created by lict on 2016/5/14.
 * 具体的业务逻辑处理类 -- 基类
 *
 * usage:
 * 子类:实现 wifiHandler,mobileHandler,disConnectedHandler 单个方法,并添加具体的业务实现
 * 在activity/fragment 中 创建实例即可
 */
public abstract class BaseAction implements NetWorkListener {

    protected Context mContext;
    protected AsyncTaskCompleteListener mAsyncTaskCompleteListener;

    public BaseAction(Context context, AsyncTaskCompleteListener asyncTaskCompleteListener) {

        this.mContext = context;
        this.mAsyncTaskCompleteListener = asyncTaskCompleteListener;
    }
    public void execute(Object... params) {

        String networkName = NetWorkUtils.getNetworkTypeName(mContext);

        switch (networkName) {

            case NetWorkUtils.NETWORK_TYPE_DISCONNECT:
                disConnectedHandler();
                break;
            case NetWorkUtils.NETWORK_TYPE_WIFI:
                wifiHandler();
                break;
            default:
                mobileHandler();
                break;
        }

    }

    @Override
    public void wifiHandler() {


    }

    @Override
    public void mobileHandler() {
    }

    @Override
    public void disConnectedHandler() {
        ToastUtils.show(mContext,"网络连接失败");
    }
}
