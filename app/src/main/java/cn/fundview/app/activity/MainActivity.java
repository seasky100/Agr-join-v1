package cn.fundview.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;

import com.lidroid.xutils.ViewUtils;

import cn.fundview.R;
import cn.fundview.app.action.global.CheckVersionAction;
import cn.fundview.app.action.my.LoginAction;
import cn.fundview.app.activity.my.MyAttentActivity;
import cn.fundview.app.activity.my.ProfileActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.msg.NotificationController;
import cn.fundview.app.msg.observer.NewFundviewInforObserverMrg;
import cn.fundview.app.service.UpdateService;
import cn.fundview.app.service.UpdateService.UpdataAppBinder;
import cn.fundview.app.tool.AppUtils;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.MenuBar;
import cn.fundview.app.view.MenuBar.MenuBarListener;
import cn.fundview.app.view.msg.MsgListView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends ABaseActivity implements MenuBarListener {

    public static final int REQUEST_CODE_MY = 1212;// 从我的页面进行登录的请求码
    public static final int REQUEST_CODE_EXPERT = 1213;// 从关注的专家进行登录的请求码
    public static final int REQUEST_CODE_COMP = 1214;// 从专注的企业进行登录的请求码
    public static final int REQUEST_CODE_AUTO = 12312;//打开应用的时候 自动登录

    private ABaseWebView activeView;

    private MenuBar menuBar = null;
    private int currIndex = 1; // 标识当前在那个页面 1 项目 2 消息 3 我

    private UpdateService updateService;

    private AppUpdateConn conn = new AppUpdateConn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //设置访问信息
        PreferencesUtils.putInt(this, Constants.FIRST_OPEN_TAG, 1);
        // 开启app的更新服务
        Intent updateService = new Intent(this, UpdateService.class);
        this.bindService(updateService, conn, BIND_AUTO_CREATE);

        menuBar = (MenuBar) this.findViewById(R.id.tabs);
        ViewUtils.inject(this);
        menuBar.setListener(this);
        currIndex = 1;
        showHomePage();

        //自动登录
        String username = PreferencesUtils.getString(this, Constants.ACCOUNT_KEY);
        String password = PreferencesUtils.getString(this, Constants.PASSWORD_KEY);
        LoginAction loginAction = new LoginAction(this, activeView);
        loginAction.execute(username, password, REQUEST_CODE_AUTO);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currIndex == 2) {

            //从消息列表页过来
            NewFundviewInforObserverMrg.getInstance().addObserver((MsgListView) activeView);
        }

        //查询未读的资讯
        int count = DaoFactory.getInstance(this).getFundviewInforDao().countUnReadFundviewInfor();

        menuBar.msgNotice(count);

        // 激活震动器
//		Shaker.getInstance().enable();

        //检查版本更新
        CheckVersionAction checkVersionAction = new CheckVersionAction(this);
        checkVersionAction.execute();
        if(PreferencesUtils.getInt(this, Constants.NEW_VERSION) == 1) {

            //有新的版本
            menuBar.versionUpdate(true);
        }else {

            menuBar.versionUpdate(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (currIndex == 2) {

            //从消息列表页过来
            NewFundviewInforObserverMrg.getInstance().removeMsgObserver((MsgListView) activeView);
        }
        // 使震动器无效
//		Shaker.getInstance().unenable();
    }

    @Override
    public void onMenuItemClick(int flag) {

        activeView.hide();
        showFlagPage(flag);
        if(PreferencesUtils.getInt(this, Constants.NEW_VERSION) == 1) {

            //有新的版本
            menuBar.versionUpdate(true);
        }else {

            menuBar.versionUpdate(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (currIndex == 1) {

                exitSys();
            } else {

                showHomePage();
            }
            return true;
        } else {


            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 首页
     **/
    private void showHomePage() {

        if (activeView != null)
            activeView.hide();

        if (currIndex == 2) {

            //从消息列表页过来
            NewFundviewInforObserverMrg.getInstance().removeMsgObserver((MsgListView) activeView);
        }
        activeView = (ABaseWebView) this.findViewById(R.id.homewebView);


        //1cae42
        (activeView).show();
        this.setHomeTitleBar(R.id.homewebView, null);
        activeView.getTitleBarView().findViewById(R.id.title_container).setBackgroundColor(getResources().getColor(R.color.title_bar_bg_color_1));
        currIndex = 1;

        //清空新消息观察者
//		NewFundviewInforObserverMrg.getInstance().removeMsgObserver();
    }

    private void showMsgPage() {

//		Shaker.getInstance().stop();
        if (activeView != null)
            activeView.hide();
        activeView = (ABaseWebView) this.findViewById(R.id.msgWebview);
        this.setHomeTitleBar(R.id.msgWebview, "资讯");
        activeView.getTitleBarView().findViewById(R.id.title_container).setBackgroundColor(getResources().getColor(R.color.title_bar_bg_color_1));
        // 注册optionMenu 监听
        currIndex = 2;

        //添加新消息观察者
        NewFundviewInforObserverMrg.getInstance().addObserver((MsgListView) activeView);

        // 清空消息提醒
        NotificationController.getInstance().closeNotification();
        activeView.show();

    }

    private void showMyPage() {

//		Shaker.getInstance().stop();
        if (activeView != null)
            activeView.hide();

        if (currIndex == 2) {

            //从消息列表页过来
            NewFundviewInforObserverMrg.getInstance().removeMsgObserver((MsgListView) activeView);
        }
        activeView = (ABaseWebView) this.findViewById(R.id.myWebview);
        this.setHomeTitleBar(R.id.myWebview, "个人中心");
        activeView.getTitleBarView().findViewById(R.id.title_container).setBackgroundColor(getResources().getColor(R.color.title_bar_bg_color_1));
        // 注册optionMenu 监听
        currIndex = 3;
        activeView.show();

        //清空新消息观察者
//		NewFundviewInforObserverMrg.getInstance().clearObserver();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Integer flag = intent.getIntExtra("flag", -1);

        if (-1 != flag) {
            activeView.hide();
            showFlagPage(flag);
            menuBar.setCuurItem(flag);
        } else {
            // 打开消息页面
            activeView.hide();
            showFlagPage(2);
            currIndex = 2;
            menuBar.setCuurItem(2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // 从我的页面进行登录
        if (requestCode == REQUEST_CODE_MY
                && resultCode == Activity.RESULT_CANCELED) {

            // 登录失败
            // String error = data.getExtras().getString("error");
            // System.out.println("error = " + error);
        } else if (requestCode == REQUEST_CODE_MY && resultCode == RESULT_OK) {

            // 跳转到我的信息
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (requestCode == REQUEST_CODE_EXPERT
                && resultCode == RESULT_OK) {

            // 跳转到关注的专家
            startActivity(new Intent(this, MyAttentActivity.class));
        } else if (requestCode == REQUEST_CODE_COMP && resultCode == RESULT_OK) {

            // 跳转到关注的企业
            startActivity(new Intent(this, MyAttentActivity.class));
        }
    }

    private void showFlagPage(int flag) {

        switch (flag) {
            case 1:

                showHomePage();
                break;
            case 2:

                showMsgPage();
                break;
            case 3:

                showMyPage();
                break;
        }
    }

    private void exitSys() {
        new AlertDialog.Builder(MainActivity.this).setTitle("退出")
                .setMessage("你确定要退出吗?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        NewFundviewInforObserverMrg.getInstance().clearObserver();

                        // // 停止服务\

//
                        //清空登录状态
//						PreferencesUtils.putInt(MainActivity.this, Constants.LOGIN_STATUS_KEY, Constants.LOGIN_OUT_STATUS);

                        JPushInterface.stopPush(MainActivity.this);
                        MainActivity.this.finish();

                        // 在进程中移除自己（用于程序自己重新启动）
                        AppUtils.killProcess();

                    }
                }).setNegativeButton("否", null).show();
    }

    private class AppUpdateConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            updateService = ((UpdataAppBinder) service).getService();
            updateService.down(MainActivity.this);
            System.out.println("后台检测app 更新....");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            // 关闭服务

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateService = null;
        unbindService(conn);
    }

    //获取menuBae
    public MenuBar getMenuBar() {

        return menuBar;
    }
}
