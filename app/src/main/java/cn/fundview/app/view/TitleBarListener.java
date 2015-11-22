package cn.fundview.app.view;

import android.view.View;

/**
 * 项目名称：agr-join-v2.0.0
 * 类名称 TitleBarListener
 * 类描述：标题栏 监听器
 * 分别坚挺左 中 右三个区域
 * 创建人：lict
 * 创建时间：2015年6月8日 上午10:28:18
 * 修改人：lict
 * 修改时间：2015年6月8日 上午10:28:18
 * 修改备注：
 */
public interface TitleBarListener {

    /**
     * 点击左边区域 主要是返回和首页的搜索条件选择
     */
    void onClickLeftBtn();

    /**
     * 点击标题栏中间区域 触发,主要是搜索框
     */
    void onClickMiddle();

    /**
     * 点击右边的功能按钮 主要有扫描,搜索和保存,分享等
     * void
     */
    void onClickRight();
}