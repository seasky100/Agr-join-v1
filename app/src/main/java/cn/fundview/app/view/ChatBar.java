package cn.fundview.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.fundview.R;

/**
 * 聊天工具栏
 *
 * @author ouda
 */
public class ChatBar extends LinearLayout {

    private ChatBarListener listener;

    private Context context;

    private View takePhotoBtn = null;
    private View chatSendBtn = null;
    private EditText chatInput = null;


    public ChatBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.chat_bar, this, true);

        // 打开相机按钮
        takePhotoBtn = findViewById(R.id.takePhotoBtn);
        takePhotoBtn.setOnClickListener(takePhotoBtnListener);

        // 聊天内容输入框
        chatInput = (EditText) findViewById(R.id.chatInput);
        // chatInput.on
        chatInput.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    // 滚动到webview底部
                    // webView.scrollTo(0, webView.getContentHeight()/* +100
                    // */);
                }

            }
        });

        // 发送聊天内容按钮
        chatSendBtn = findViewById(R.id.chatSendBtn);
        chatSendBtn.setOnClickListener(chatSendBtnListener);
    }

    /**
     * 注册监听
     **/
    public void registerListener(ChatBarListener listener) {
        this.listener = listener;
    }

    private OnClickListener takePhotoBtnListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
/*
            Intent intent = new Intent(context, TakePhotoActivity.class);

			context.startActivity(intent);
*/

            listener.sendPhotoMsg();
        }
    };

    private OnClickListener chatSendBtnListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            String txt = chatInput.getText().toString()
                    .replaceAll("'", "\\\\'");
            txt = txt.replaceAll("%", "％");
            // txt = txt.replaceAll("\n", " ");
            // String time = String.valueOf(new Date().getTime());
            // webView.loadUrl("javascript:" + txtMsgCallback + "('" + time +
            // "', '" + txt + "');");

            if (null == txt || "".equals(txt.trim())) {

                Toast.makeText(context, "输入的内容不能为空!", Toast.LENGTH_SHORT)
                        .show();
            } else {

                chatInput.setText("");
                // webView.invalidate();
                if (null != listener)
                    listener.sendMsg(txt);
            }
        }
    };

}
