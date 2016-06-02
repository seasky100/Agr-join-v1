package cn.fundview.app.view.expert.guest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.fundview.R;

/**
 * Created by lict on 2016/6/2.
 */
public class GuestExpertItemDecoration extends RecyclerView.ItemDecoration {

    private ColorDrawable mDivider;
    private int height;

    public GuestExpertItemDecoration(Context ctx, int height) {
        mDivider = new ColorDrawable(ctx.getResources().getColor(R.color.gray));
        this.height = height;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingLeft();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            int bottom = top + height;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.set(0, 0,0, height);
    }
}
