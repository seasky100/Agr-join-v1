package cn.fundview.app.view.common.slide;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.fundview.R;

/**
 * Created by lict on 2016/5/28.
 */
public class SlidePagerView extends FrameLayout {

    private String[] imageUrls;//需要展示的imageview 的路径
    private List<ImageView> imageViewsList;//需要显示的所有图片
    private List<View> dotViewsList;//页码指示条
    private Context context;
    private ViewPager viewPager;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //自动轮播启用开关
    private   boolean isAutoPlay = true;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //当前轮播页
    private int currentItem  = 0;
    private LinearLayout dotContainer;
    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };
    public SlidePagerView(Context context) {
        this(context,null);
    }

    public SlidePagerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidePagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if(isAutoPlay){
            startPlay();
        }
    }
    @TargetApi(21)
    public SlidePagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(){
        if(imageUrls == null || imageUrls.length == 0)
            return;

        viewPager = new ViewPager(context);
        ViewGroup.LayoutParams layoutParms = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        viewPager.setLayoutParams(layoutParms);

        addView(viewPager);
        dotContainer = new LinearLayout(context);
        addView(dotContainer);

        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view =  new ImageView(context);
            view.setTag(imageUrls[i]);
            if(i==0)//给一个默认图
                view.setBackgroundResource(R.mipmap.achv_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView =  new ImageView(context);
            dotViewsList.add(dotView);
            dotContainer.addView(dotView);
        }

        viewPager.setFocusable(true);

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        dotContainer.setGravity(GravityCompat.END);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) dotContainer.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.bottomMargin = 30;
        dotContainer.setLayoutParams(params);
        for(int i = 0;i<dotContainer.getChildCount();i++) {

            View view = dotContainer.getChildAt(i);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(30,30);
            param.leftMargin = 20;
            view.setLayoutParams(param);
        }
    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub

            if(isAutoPlay) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewsList.get(position);

            imageLoader.displayImage(imageUrls[position], imageView);

            container.addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            for(int i=0;i < dotViewsList.size();i++){
                if(i == position){
                    ((View)dotViewsList.get(position)).setBackgroundResource(R.drawable.dot_curr);
                }else {
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:// 手势滑动，空闲中
                    System.out.println("SCROLL_STATE_DRAGGING");
                    isAutoPlay = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:// 界面切换中
                    System.out.println("SCROLL_STATE_SETTLING");

                    isAutoPlay = true;
                    break;
                case ViewPager.SCROLL_STATE_IDLE:// 滑动结束，即切换完毕或者加载完毕
                    System.out.println("SCROLL_STATE_IDLE");
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    isAutoPlay = true;
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }
    }

    public void setImageUrls(String[] imageUrls) {

        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
        this.imageUrls = imageUrls;
        initUI();
    }
}
