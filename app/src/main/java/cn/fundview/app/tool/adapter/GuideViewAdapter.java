package cn.fundview.app.tool.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.fundview.app.fragment.GuideFragment;
import cn.fundview.app.tool.Constants;

/**
 * Created by Administrator on 2015/10/13 0013.
 * 首次使用引导页面适配器
 */
public class GuideViewAdapter extends FragmentPagerAdapter {


    private List<Integer> drawables;//图片的集合

    public GuideViewAdapter(FragmentManager fm, List<Integer> drawables) {

        super(fm);
        this.drawables = drawables;
    }


    @Override
    public int getCount() {
        return drawables.size();
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.GUIDE_FRAGMENT_IMG_ID, drawables.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }
}

