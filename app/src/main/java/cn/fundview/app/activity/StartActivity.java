package cn.fundview.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.fundview.R;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.adapter.GuideViewAdapter;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * 开始acticity ,首先检查是否是安装后第一次使用,如果是,先打开引导页,不是的话跳过
 * 判断是不是第一次使用使用的是 PreferencesUtils.getInt(this, Constants.FIRST_OPEN_TAG,0) == 1
 */
public class StartActivity extends FragmentActivity {

    private List<Integer> drawables;
    private Handler handler;
//    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏显示状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        drawables = new ArrayList<>();
        drawables.add(R.drawable.guide_1);
        drawables.add(R.drawable.guide_2);
        drawables.add(R.drawable.guide_3);
        handler = new Handler();

        //判断是否是第一次安装
        if (PreferencesUtils.getBoolean(this, Constants.FIRST_INSTALL_TAG, true)) {

            //发送网络请求,统计安装
//            RService.doAsync(cn.fundview.app.domain.webservice.util.Constants.INSTALL_COUNT_URL + "?deviceId=" + Installation.getDriverId(this) + "&type=1");
            PreferencesUtils.putBoolean(this, Constants.FIRST_INSTALL_TAG, false);
//            ToastUtils.show(this, "应用第一次安装完成");

            //删除apk
            File file1 = new File(DeviceConfig.getSysPath(this) + Constants.APK_PATH);
            if (file1.exists()) {

                file1.delete();
            }
        }

        if (PreferencesUtils.getInt(this, Constants.FIRST_OPEN_TAG, 0) > 0) {

//            getWindow().setBackgroundDrawableResource(R.mipmap.start_bg);

            setContentView(R.layout.activity_appstart);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    StartActivity.this.finish();
                }
            }, 1000);
        } else {
//            getWindow().setBackgroundDrawableResource(R.mipmap.qd1_bj);
            setContentView(R.layout.first_guide);
            GuideViewAdapter viewPagerAdapter = new GuideViewAdapter(getSupportFragmentManager(), drawables);
            final ViewPager viewPager = (ViewPager) this.findViewById(R.id.pager);
            final LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.indicator_panel);

            for (int i = 0; i < drawables.size(); i++) {

                ImageView dot = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
                params.setMargins(4, 0, 4, 0);
                dot.setLayoutParams(params);

                if (i == 0) {
                    dot.setImageResource(R.drawable.dot_curr);
                } else {

                    dot.setImageResource(R.drawable.dot);
                }
                linearLayout.addView(dot);
            }


            viewPager.setAdapter(viewPagerAdapter);
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//
//                    viewPager.setCurrentItem(1);
//                }
//            };
            //handler.postDelayed(runnable, 20000);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                }

                @Override
                public void onPageSelected(final int position) {

                    TextView textView = (TextView) StartActivity.this.findViewById(R.id.enter);
//                    if (runnable != null) {
//
//                        handler.removeCallbacks(runnable);
//                    }
//                    runnable = null;
                    for (int i = 0; i < drawables.size(); i++) {

                        ImageView dot = (ImageView) linearLayout.getChildAt(i);
                        if (i == position) {
                            dot.setImageResource(R.drawable.dot_curr);
                        } else {
                            dot.setImageResource(R.drawable.dot);
                        }

                    }
                    if (position == drawables.size() - 1) {

                        //最后一步
                        textView.setVisibility(View.VISIBLE);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(StartActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                StartActivity.this.finish();
                            }
                        });
                    } else {
                        textView.setVisibility(View.GONE);
//                        runnable = new Runnable() {
//                            @Override
//                            public void run() {
//
//                                viewPager.setCurrentItem(position + 1);
//                            }
//                        };
                        //handler.postDelayed(runnable, 2000);

                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        if (JPushInterface.isPushStopped(this)) {

            JPushInterface.resumePush(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        JPushInterface.onPause(this);
    }
}
