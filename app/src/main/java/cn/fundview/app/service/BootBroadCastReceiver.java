package cn.fundview.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开启启动广播
 **/
public class BootBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//				Intent msgService = new Intent(context, MsgService.class);
//				context.startService(msgService);
            }


        }).start();
    }


}
