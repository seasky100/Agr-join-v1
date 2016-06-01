package cn.fundview.app.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lict on 2016/6/1.
 * 简单的itemDecoration 使用的是list 的divider的样式,只在list列表中使用
 */
public class SimpleLineLinearItemDecoration extends RecyclerView.ItemDecoration {

    public SimpleLineLinearItemDecoration(int colorId, int height) {
        super();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        System.out.println("onDraw");
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        System.out.println("onDrawOver");
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        System.out.println("getItemOffsets");
    }
}
