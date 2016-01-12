package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import cn.fundview.app.action.my.InitMyPageAction;
import cn.fundview.app.action.my.LoginAction;
import cn.fundview.app.activity.MainActivity;
import cn.fundview.app.activity.my.AboutActivity;
import cn.fundview.app.activity.my.FavoritesActivity;
import cn.fundview.app.activity.my.HotActivity;
import cn.fundview.app.activity.my.LoginActivity;
import cn.fundview.app.activity.my.MyAttentActivity;
import cn.fundview.app.activity.my.ProfileActivity;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.AsyncTaskCompleteListener;

public class MyView extends ABaseWebView implements AsyncTaskCompleteListener {


    public MyView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        if (this.getUrl() == null)
            this.loadUrl("file:///android_asset/page/my/my.html");
    }

    @Override
    public void init() {

        InitMyPageAction action = new InitMyPageAction(context, this);
        action.execute();
    }

    @Override
    public void active() {
        super.active();
        InitMyPageAction action = new InitMyPageAction(context, this);
        action.execute();

    }


    /**
     * 打开新页面
     **/
    @JavascriptInterface
    public void openPage(String id) {

        Intent intent = null;
        String username = PreferencesUtils.getString(context, Constants.ACCOUNT_KEY);
        String password = PreferencesUtils.getString(context, Constants.PASSWORD_KEY);
        int status = PreferencesUtils.getInt(context, Constants.LOGIN_STATUS_KEY, Constants.LOGIN_OUT_STATUS);

        if ("personal".equals(id)) {


            intent = new Intent(context, ProfileActivity.class);

            if (StringUtils.isBlank(username)) {

                //没有登录, 跳转到登录页面
                intent = new Intent(context, LoginActivity.class);
            } else if (status == Constants.LOGIN_OUT_STATUS) {

                //还没有登录,自动登录
                LoginAction loginAction = new LoginAction(context, this);
                loginAction.execute(username, password, 1);
                loginAction.regListener(this);
            }

        } else if ("about".equals(id)) {

            intent = new Intent(context, AboutActivity.class);
        } else if ("attent-expert".equals(id)) {
            //我关注的专家
            if (StringUtils.isBlank(username)) {

                //没有登录, 跳转到登录页面
                intent = new Intent(context, LoginActivity.class);
            } else if (status == Constants.LOGIN_OUT_STATUS) {

                //还没有登录,自动登录
                LoginAction loginAction = new LoginAction(context, this);
                loginAction.execute(username, password, 2);
                loginAction.regListener(this);
                return;
            } else {
                intent = new Intent(context, MyAttentActivity.class);
                intent.putExtra("attent-type", 2);
            }
        } else if ("attent-company".equals(id)) {
            //我关注的企业
            if (StringUtils.isBlank(username)) {

                //没有登录, 跳转到登录页面
                intent = new Intent(context, LoginActivity.class);
            } else if (status == Constants.LOGIN_OUT_STATUS) {

                //还没有登录,自动登录
                LoginAction loginAction = new LoginAction(context, this);
                loginAction.execute(username, password, 3);
                loginAction.regListener(this);

            } else {

                intent = new Intent(context, MyAttentActivity.class);
                intent.putExtra("attent-type", 1);
            }

        } else if ("favorites-achv".equals(id)) {

            //我的收藏成果
            intent = new Intent(context, FavoritesActivity.class);
            intent.putExtra("favorite-type", 1);
        } else if ("favorites-requ".equals(id)) {

            //我的收藏需求
            intent = new Intent(context, FavoritesActivity.class);
            intent.putExtra("favorite-type", 2);
        } else if ("msg".equals(id)) {

            //我的消息
            intent = new Intent(context, MyAttentActivity.class);
        } else if ("hot".equals(id)) {

            //客服热线
            intent = new Intent(context, HotActivity.class);
        } else if ("about".equals(id)) {

            //关于丰景
            intent = new Intent(context, AboutActivity.class);
        }

        if (intent != null) {

            context.startActivity(intent);
        }
    }


    /**
     * 打开的登录页面
     **/
    @JavascriptInterface
    public void openLoginPage(String page) {

        Intent intent = new Intent(context, LoginActivity.class);
        if ("profile".equals(page)) {

            //登录成功后跳转到我的信息页面
            ((Activity) context).startActivityForResult(intent, MainActivity.REQUEST_CODE_MY);
        } else if ("attent-comp".equals(page)) {

            //登录成功后跳转到关注的企业
            ((Activity) context).startActivityForResult(intent, MainActivity.REQUEST_CODE_COMP);
        } else if ("attent-expert".equals(page)) {

            //登录成功后跳转到关注的专家
            ((Activity) context).startActivityForResult(intent, MainActivity.REQUEST_CODE_EXPERT);
        }
    }

    @Override
    public void complete(int requestCode, int responseCode, Object msg) {

        if (requestCode == 1) {

            //点击用户名后跳转的
            if (responseCode == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {
//				
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            } else if (responseCode == 0) {

                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        }

        if (requestCode == 2) {

            //跳转到我关注的专家
            if (responseCode == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {
//
                Intent intent = new Intent(context, MyAttentActivity.class);
                intent.putExtra("attent-type", 2);
                context.startActivity(intent);
            } else if (responseCode == 0) {

                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        }

        if (requestCode == 3) {

            //跳转到我关注的企业
            if (responseCode == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {
//
                Intent intent = new Intent(context, MyAttentActivity.class);
                intent.putExtra("attent-type", 1);
                context.startActivity(intent);
            } else if (responseCode == 0) {

                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        }

        if (requestCode == 4) {

            //检查版本更新,提示有新的版本
            if(responseCode == 2) {

                this.loadUrl("javascript:Page.setNewVersion(1);");
            }else{

                this.loadUrl("javascript:Page.setNewVersion(0);");
            }
        }
    }
}
