package cn.fundview.app.action.global;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.fundview.app.activity.MainActivity;

/**
 * 回到主页面,params现在暂时为页面
 *
 * @author ouda
 */
public class OpenMainPageAction {

    public void execute(Context context, int flag) {

        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

}
