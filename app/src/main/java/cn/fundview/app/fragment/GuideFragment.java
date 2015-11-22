package cn.fundview.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import cn.fundview.R;
import cn.fundview.app.tool.Constants;

/**
 * Created by Administrator on 2015/10/13 0013.
 * <p/>
 * 引导页面 Fragment
 */
public class GuideFragment extends Fragment {

    private int drawableId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        drawableId = bundle.getInt(Constants.GUIDE_FRAGMENT_IMG_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.guide_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        imageView.setImageResource(drawableId);
        return view;
    }
}
