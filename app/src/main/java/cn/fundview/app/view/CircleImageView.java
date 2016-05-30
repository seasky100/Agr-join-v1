package cn.fundview.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lict on 2016/5/31.
 */
public class CircleImageView extends ImageView {

    private Paint paint;

    public CircleImageView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(); paint.setColor(Color.BLUE);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(); paint.setColor(Color.BLUE);
    }

    @TargetApi(21)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = this.getX();
        float y = this.getY();
        int width = this.getWidth();
        int height = this.getHeight();
//        canvas.drawCircle(x + width/2,y + height/2,90,paint);
    }
}
