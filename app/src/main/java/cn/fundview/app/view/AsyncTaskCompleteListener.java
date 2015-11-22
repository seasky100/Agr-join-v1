package cn.fundview.app.view;

/**
 * 异步任务执行完成 监听器
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：AsyncTaskCompleteListener
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年7月23日 下午6:42:22
 * 修改人：lict
 * 修改时间：2015年7月23日 下午6:42:22
 * 修改备注：
 */
public interface AsyncTaskCompleteListener {

    /**
     * 异步任务执行完成后调用
     *
     * @param requestCode  请求码
     * @param responseCode 响应码
     * @param msg          返回的消息,可以携带异步任务额执行结果
     */
    void complete(int requestCode, int responseCode, Object msg);
}
