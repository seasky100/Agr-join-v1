package cn.fundview.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.fundview.R;

/**
 * 选项卡Item
 *
 * @author ouda
 */
public class MenuItem extends LinearLayout {

    private int imgNormal;
    private int imgPressed;

    private Context context;

    private ImageView imgView;
    private ImageView msgNoticeImgView;
    private TextView txtView;


    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.menu_item, this, true);

        this.context = context;

        imgView = (ImageView) this.findViewById(R.id.menu_item_img);
        msgNoticeImgView = (ImageView) this.findViewById(R.id.msg_notice);
        txtView = (TextView) this.findViewById(R.id.menu_item_txt);
        String txt = "";
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.menuItem);
        txt = typedArray.getString(R.styleable.menuItem_itemTxt);
        imgNormal = typedArray.getResourceId(R.styleable.menuItem_itemImgBgNormal, 0);
        imgPressed = typedArray.getResourceId(R.styleable.menuItem_itemImgBgPressed, 0);
        txtView.setText(txt);
        imgView.setBackgroundResource(imgNormal);
        boolean selected = typedArray.getBoolean(R.styleable.menuItem_itemSelected, false);
        if (selected)
            setOnClickBg();
        else
            setOnUpBg();
        typedArray.recycle();
    }

    public void setOnClickBg() {
        imgView.setBackgroundResource(imgPressed);
        txtView.setTextColor(context.getResources().getColor(R.color.tag_txt_pressed));
    }

    public void setOnUpBg() {
        imgView.setBackgroundResource(imgNormal);
        txtView.setTextColor(context.getResources().getColor(R.color.tag_txt_normal));
    }

    public void showMsgNotice() {

        msgNoticeImgView.setVisibility(View.VISIBLE);
    }

    public void hideMsgNotice() {

        msgNoticeImgView.setVisibility(View.INVISIBLE);
    }


}
