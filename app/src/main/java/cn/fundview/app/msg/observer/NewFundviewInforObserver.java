package cn.fundview.app.msg.observer;

import cn.fundview.app.domain.model.FundviewInfor;

/**
 * 丰景资讯 观察者
 */
public interface NewFundviewInforObserver {

    /**
     * 收到新消息了
     **/
    void onReceive(FundviewInfor item);
}