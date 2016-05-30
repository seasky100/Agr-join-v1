package cn.fundview.app.tool;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.fundview.R;

/**
 * Created by lict on 2016/5/28.
 * dialog 工具类
 */
public class DialogUtils {

    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog_layout, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_container);// 加载布局
        // main.xml中的ImageView
        ImageView loadingImageView = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation loadingAnim = AnimationUtils.loadAnimation(
                context, R.anim.loading_dialog);
        // 使用ImageView显示动画
        loadingImageView.startAnimation(loadingAnim);
        tipTextView.setText(msg);// 设置加载信息

        final Dialog loadingDialog = new Dialog(context, R.style.loadingDialogStyle);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);//
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    loadingDialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }
}
