package cn.fundview.app.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.fundview.R;
import cn.fundview.app.tool.StringUtils;

/**
 * 搜索标题栏 (下拉框 搜索框)
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：SearchTitleBar 类描述： 创建人：lict 创建时间：2015年6月8日
 * 上午10:00:28 修改人：lict 修改时间：2015年6月8日 上午10:00:28 修改备注：
 */
public class SearchTitleBar extends LinearLayout {

    //home titleBar
    private ImageView scanBtn;
    private TextView searchType;
    private EditText searchEditText;

    //common TitleBar
    private TextView titleView;
    private ImageView backBtn;
    private LinearLayout rightBtn;
    private TextView rightBtnText;
    private ImageView searchBtn;

    //search titleBar
    private ImageView cleanBtn;

    private TitleBarListener listener;
    private Context context;

    //param
    private String key;//搜索关键词
    private int type;//搜索类型 专家,企业,成果,需求

    public SearchTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.title_bar);
        /*这里从集合里取出相对应的属性值,第二参数是如果使用者没用配置该属性时所用的默认值*/
        int titleBarType = typeArray.getInt(R.styleable.title_bar_type, 1);//读取在xml 中设置的标题栏的类型
        switch (titleBarType) {

            case 1:
                //home titleBar
                setHomeTitleBar();
                break;
            case 2:
                //search titleBar
                setSearchTitleBar();
                break;
            case 3:
                //common titleBar
                boolean backBtn = typeArray.getBoolean(R.styleable.title_bar_back_btn, false);
                String rightBtn = typeArray.getString(R.styleable.title_bar_right_btn);
                setCommonTitleBar(backBtn, rightBtn);
                break;
        }
        typeArray.recycle();
    }

    /**
     * 注册监听
     **/
    public void registerListener(TitleBarListener listener) {
        this.listener = listener;
    }

    /**
     * 设置首页标题栏
     * void
     */
    private void setHomeTitleBar() {

        LayoutInflater.from(context).inflate(R.layout.home_title_bar, this, true);
        scanBtn = (ImageView) this.findViewById(R.id.scanBtn);
        searchType = (TextView) this.findViewById(R.id.searchType);
        searchEditText = (EditText) this.findViewById(R.id.editText);
        titleView = (TextView) this.findViewById(R.id.titleText);
        attachHomeEvents();

    }

    private void attachHomeEvents() {

        // 搜索条件
        searchType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.onClickLeftBtn();
            }
        });

        // 搜索框
        searchEditText.setKeyListener(null);
        searchEditText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                listener.onClickMiddle();
            }
        });
        scanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickRight();
            }
        });
    }

    /**
     * search title bar
     */
    private void setSearchTitleBar() {

        LayoutInflater.from(context).inflate(R.layout.search_title_bar, this, true);
        backBtn = (ImageView) this.findViewById(R.id.backBtn);//返回按钮
        searchEditText = (EditText) this.findViewById(R.id.search);//搜索框
        cleanBtn = (ImageView) this.findViewById(R.id.cleanBtn);
        rightBtn = (LinearLayout) this.findViewById(R.id.rightBtn);
        rightBtnText = (TextView) this.findViewById(R.id.rightText);
        attachSearchEvents();


    }

    /**
     * 绑定事件
     */
    private void attachSearchEvents() {

        // 返回按钮
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.onClickLeftBtn();
            }
        });

        // 搜索框
        searchEditText.setCursorVisible(true);
        searchEditText.setFocusable(true);
        searchEditText.setFocusableInTouchMode(true);

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                key = s.toString();

                if (key.equals("")) {

                    rightBtnText.setText("取消");
                    cleanBtn.setVisibility(View.GONE);

                } else {

                    rightBtnText.setText("搜索");
                    cleanBtn.setVisibility(View.VISIBLE);
                }

                key = key.replaceAll("'", "");
                listener.onClickMiddle();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        // 搜索按钮
        rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String key = searchEditText.getText().toString();
                if (StringUtils.isBlank(key)) {

                    ((Activity) context).finish();//点击取消后是关闭当前窗口
                } else {


                    listener.onClickRight();
                }
            }
        });

        // 清除按钮
        cleanBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                searchEditText.setText("");
            }
        });
    }

//	/**
//	 * searchPage高级搜索页
//	 *
//	 */
//	public void setSearchListPage(final int type) {
//
//		searchType.setVisibility(GONE);
//		searchTypePanel.setVisibility(VISIBLE);
//		titleView.setVisibility(GONE);
//		backBtn.setVisibility(VISIBLE);
//		funcPanel.setVisibility(GONE);
//
//		// 返回按钮
//		backBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				listener.onClickBack();
//			}
//		});
//
//		// 修改标题栏的提示信息
//		switch (type) {
//
//			case 0:
//				searchEditText.setHint("专家搜索");
//				break;
//			case 1:
//				searchEditText.setHint("成果搜索");
//				break;
//			case 2:
//				searchEditText.setHint("企业搜索");
//				break;
//			case 3:
//				searchEditText.setHint("需求搜索");
//				break;
//		}
//		// 搜索框
//		searchEditText.setCursorVisible(false);
//		searchEditText.setClickable(true);
//		searchEditText.setFocusable(false);
//		//searchEditText.
//		searchEditText.setFocusableInTouchMode(false);
//		searchEditText.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent(context, SearchHistoryActivity.class);
//				intent.putExtra("type",type);
//				context.startActivity(intent);
//			}
//		});
//	}

    /**
     * commonPage title bar
     *
     * @param backVisible true 显示返回按钮
     * @param funcBtnText 功能按钮标题
     */
    private void setCommonTitleBar(boolean backVisible, String funcBtnText) {

        LayoutInflater.from(context).inflate(R.layout.common_title_bar, this, true);
        backBtn = (ImageView) this.findViewById(R.id.backBtn);//返回按钮
        rightBtn = (LinearLayout) this.findViewById(R.id.rightBtn);
        rightBtnText = (TextView) this.findViewById(R.id.rightText);
        this.titleView = (TextView) this.findViewById(R.id.titleText);
        this.searchBtn = (ImageView) this.findViewById(R.id.searchBtn);
        attachCommonEvents(backVisible, funcBtnText);

    }

    private void attachCommonEvents(boolean backVisible, String funcBtnTex) {

        if (backVisible) {

            backBtn.setVisibility(VISIBLE);
            // 返回按钮
            backBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.onClickLeftBtn();//监听标题栏左边的按钮
                }
            });
        } else {

            backBtn.setVisibility(INVISIBLE);//隐藏但不释放位置,保证标题一直保持在中间
        }

        if (StringUtils.isBlank(funcBtnTex)) {

            this.rightBtn.setVisibility(INVISIBLE);//暂时隐藏,后面需要提供入口设置是否是搜索
        } else {

            this.rightBtn.setVisibility(VISIBLE);
            this.rightBtnText.setText(funcBtnTex);
            this.rightBtn.setEnabled(true);
            this.rightBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.onClickRight();
                }
            });
        }
    }

    /**
     * 设置 右边按钮是搜索按钮,并设置标题栏的内容,只用在commonTitleBar 中
     *
     * @param title        标题栏的內容
     * @param rightBtnText 标题栏右側按鈕的文字
     * @param isSearch     标题栏的右側是否是搜索按鈕
     */
    public void setCommonTitlebarRightBtn(String title, String rightBtnText, boolean isSearch) {

        this.titleView.setText(title);
        this.rightBtn.setVisibility(VISIBLE);
        this.rightBtnText.setVisibility(VISIBLE);
        this.searchBtn.setVisibility(GONE);
        if (StringUtils.isBlank(rightBtnText)) {

            this.rightBtnText.setText("");
            if (isSearch) {
                this.searchBtn.setImageResource(R.mipmap.search_icon_btn);
                this.rightBtnText.setVisibility(GONE);
                this.searchBtn.setVisibility(VISIBLE);
            }
        } else {

            this.rightBtnText.setText(rightBtnText);
            this.rightBtnText.setBackgroundResource(R.color.transparent);
            this.searchBtn.setVisibility(GONE);
        }

        this.rightBtn.setEnabled(true);
        this.rightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.onClickRight();
            }
        });
    }

    /**
     * 标题栏为空的时候 表示是有不可编辑的标题栏, 不为空的时候表示只有标题栏
     *
     * @param title
     */
    public void setHomeTitle(String title) {

        if (StringUtils.isBlank(title)) {

            this.scanBtn.setVisibility(View.VISIBLE);
            searchType.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.GONE);
            titleView.setText("");
        } else {

            //隐藏除了标题外的所有组件
            this.scanBtn.setVisibility(View.GONE);
            searchType.setVisibility(View.GONE);
            searchEditText.setVisibility(View.GONE);
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);

        }
    }

    public String getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public EditText getSearchEditText() {

        return this.searchEditText;
    }

    public TextView getSearchType() {

        return this.searchType;
    }
}
