package cn.fundview.app.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.lang.reflect.Method;

import cn.fundview.R;

public class PopUpWindow {

    private View view;
    private Context context;
    private View popUpView;
    private PopupWindow mPopupWindow;

    public PopUpWindow(Context context, View popUpView, View targetView) {

        this.context = context;
        this.popUpView = popUpView;
        this.view = targetView;
        init();
    }

    private void init() {


        backgroundAlpha(0.15f);
        mPopupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        setPopupWindowTouchModal(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(R.style.AnimationUpPopup);
        popUpView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAsDropDown(view);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        mPopupWindow.update();
    }

    public void setTouchable(boolean flag) {

        mPopupWindow.setTouchable(flag);
    }

    public void dismiss() {

        if (mPopupWindow.isShowing()) {

            mPopupWindow.dismiss();
        }

    }

    public void toggle() {

        if (mPopupWindow.isShowing()) {

            mPopupWindow.dismiss();
        } else {
            backgroundAlpha(0.15f);
            popUpView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.showAsDropDown(view);
        }

    }

    private void setPopupWindowTouchModal(boolean touchModal) {
        if (null == mPopupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(mPopupWindow, touchModal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }
}
