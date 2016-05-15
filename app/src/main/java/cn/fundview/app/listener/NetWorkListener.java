package cn.fundview.app.listener;

/**
 * Created by lict on 2016/5/14.
 *
 * 根据不同的网络状态,在加载网络资源的时候 执行不同的操作
 *
 * wifi 网络下的操作
 * 2g/3g/4g 下的操作
 * 没有网络下的操作
 */
public interface NetWorkListener {

    /**
     * 当前网络是 wifi 的时候的操作
     */
    void wifiHandler();

    /**
     * 当前网络是 2g/3g/4g的时候执行的操作
     */
    void mobileHandler();

    /**
     * 没有网络的时候 执行的操作
     */
    void disConnectedHandler();
}
