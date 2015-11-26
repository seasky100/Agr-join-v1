package cn.fundview.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Map;

import cn.fundview.R;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.msg.observer.NewFundviewInforObserver;
import cn.fundview.app.msg.observer.NewFundviewInforObserverMrg;

/**
 * 选项栏
 * <p/>
 * 观察 丰景资讯推送(数量大于1 显示红点)
 */
public class MenuBar extends LinearLayout implements NewFundviewInforObserver {

    private MenuBarListener listener;

    private Context context;

    private MenuItem currItem;
    private MenuItem projItem;
    private MenuItem msgItem;
    private MenuItem myItem;

    public MenuBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.menu_bar, this, true);

        projItem = (MenuItem) this.findViewById(R.id.proj_item);

        this.context = context;

        msgItem = (MenuItem) this.findViewById(R.id.msg_item);

        myItem = (MenuItem) this.findViewById(R.id.my_item);

        currItem = projItem;
        projItem.setClickable(true);
        msgItem.setClickable(true);
        myItem.setClickable(true);
        projItem.setOnClickListener(itemClicklistener);
        msgItem.setOnClickListener(itemClicklistener);
        myItem.setOnClickListener(itemClicklistener);
        NewFundviewInforObserverMrg.getInstance().addObserver(this);//添加消息数量观察者
    }

    public void setListener(MenuBarListener listener) {

        this.listener = listener;
    }

    private OnClickListener itemClicklistener = new OnClickListener() {

        @Override
        public void onClick(View view) {

            currItem.setOnUpBg();
            currItem = ((MenuItem) view);
            currItem.setOnClickBg();
            int flag = Integer.valueOf((String) view.getTag());
            listener.onMenuItemClick(flag);
        }
    };

    public void setCuurItem(int flag) {

        currItem.setOnUpBg();
        setCuur(flag);
        currItem.setOnClickBg();
    }

    /**
     * 设置是否用未读消息提醒
     *
     * @param count
     */
    public void msgNotice(int count) {

        if (count > 0) {

            myItem.showMsgNotice();
        } else {

            msgItem.hideMsgNotice();
        }
    }

    public void versionUpdate(boolean flag) {

        if (flag) {

            myItem.showMsgNotice();
        } else {

            myItem.hideMsgNotice();
        }
    }
    private void setCuur(int flag) {

        switch (flag) {
            case 1:
                currItem = projItem;
                break;
            case 2:
                currItem = msgItem;
                break;
            case 3:
                currItem = myItem;
                break;
        }
    }

    @Override
    public void onReceive(FundviewInfor item) {

        if (item != null) {

            msgItem.showMsgNotice();
        }
    }


    public static interface MenuBarListener {

        void onMenuItemClick(int flag);
    }

}
