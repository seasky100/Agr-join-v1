package cn.fundview.app.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.fundview.R;

public class SearchBar extends LinearLayout {

    private SearchBarListener listener;

    private ImageView cleanBtn = null;
    private Button operationBtn = null;
    private EditText keyInput = null;

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.search_bar, this);

        // 清空关键字按钮
        cleanBtn = (ImageView) findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(keyCleanListener);

        // 操作按钮
        operationBtn = (Button) findViewById(R.id.operationBtn);
        operationBtn.setOnClickListener(searchCancelListener);

        // 关键字输入框
        keyInput = (EditText) findViewById(R.id.key);
        keyInput.addTextChangedListener(keyWatcher);
    }

    /**
     * 注册监听
     **/
    public void registerListener(SearchBarListener listener) {
        this.listener = listener;
    }

    private OnClickListener keyCleanListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            keyInput.setText("");
            operationBtn.setText("取消");
            cleanBtn.setVisibility(View.GONE);
        }
    };

    private OnClickListener searchCancelListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            String key = keyInput.getText().toString();

            if (null != key && !"".equals(key)) {
                key = key.replaceAll("'", "");
                listener.doSearch(key);
            } else {
                listener.closeView();
            }
        }
    };

    private TextWatcher keyWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String key = s.toString();

            if (key.equals("")) {

                operationBtn.setText("取消");
                cleanBtn.setVisibility(View.GONE);
            } else {

                operationBtn.setText("搜索");
                cleanBtn.setVisibility(View.VISIBLE);
            }

            key = key.replaceAll("'", "");
            listener.keyChange(key);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void setPlaceholder(String value) {

        keyInput.setHint(value);
    }
}
