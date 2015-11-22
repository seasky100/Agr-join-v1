package cn.fundview.app.msg.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 新丰景资讯内容观察者管理器
 **/
public class NewFundviewInforObserverMrg {

    private static final NewFundviewInforObserverMrg instance = new NewFundviewInforObserverMrg();

    private List<NewFundviewInforObserver> observers;

    private NewFundviewInforObserverMrg() {
    }

    public static NewFundviewInforObserverMrg getInstance() {
        return instance;
    }

    public void addObserver(NewFundviewInforObserver observer) {

        System.out.println(this.getClass().getName() + "  添加新消息观察者...");
        if (null == observers)
            observers = new ArrayList<NewFundviewInforObserver>();

        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void removeMsgObserver(NewFundviewInforObserver observer) {

        if (null != observers && observers.size() > 0)
            observers.remove(observer);
    }

    public List<NewFundviewInforObserver> getObservers() {

        return observers;
    }

    //清空新消息观察者
    public void clearObserver() {

        System.out.println(this.getClass().getName() + "  清空新消息观察者...");
        if (observers != null && observers.size() > 0) {

            observers.clear();
        }
    }
}