package cn.fundview.app.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.R;
import cn.fundview.app.CaptureActivity;
import cn.fundview.app.action.home.IndexAction;
import cn.fundview.app.activity.FundProject.FundProjectDetailActivity;
import cn.fundview.app.activity.FundProject.FundProjectListActivity;
import cn.fundview.app.activity.achv.AchvListActivity;
import cn.fundview.app.activity.company.CompListActivity;
import cn.fundview.app.activity.company.CompanyProductDetailActivity;
import cn.fundview.app.activity.expert.DetailActivity;
import cn.fundview.app.activity.expert.ExpertListActivity;
import cn.fundview.app.activity.history.SearchHistoryActivity;
import cn.fundview.app.activity.org.OrgListActivity;
import cn.fundview.app.activity.product.ProductListActivity;
import cn.fundview.app.activity.requ.RequListActivity;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PopUpWindow;
import cn.fundview.app.tool.adapter.ListViewAdapter;
import cn.fundview.app.view.ABaseWebView;

/**
 * 类名称：HomeView
 * 类描述：首页view  title bar 的观察者
 * 创建人：lict
 * 创建时间：2015年6月8日 下午3:03:34
 * 修改人：lict
 * 修改时间：2015年6月8日 下午3:03:34
 * 修改备注：
 */
@SuppressLint("InflateParams")
public class HomeView extends ABaseWebView {

    private PopUpWindow popupWindow;
    private int selectedIndex = 0;//当前选中的下标索引
    private ListViewAdapter adapter;

    final List<Map<String, String>> dataSource = new ArrayList<>();  //搜索条件 数据源

    public HomeView(final Context context, AttributeSet attrs) {
        super(context, attrs);


        Map<String, String> expertMap = new HashMap<>();
        expertMap.put("name", "专 家");
        expertMap.put("key", "0");

        Map<String, String> achvMap = new HashMap<>();
        achvMap.put("name", "成 果");
        achvMap.put("key", "1");

        Map<String, String> compMap = new HashMap<>();
        compMap.put("name", "企 业");
        compMap.put("key", "2");

        Map<String, String> requMap = new HashMap<>();
        requMap.put("name", "需 求");
        requMap.put("key", "3");

        Map<String, String> orgMap = new HashMap<>();
        orgMap.put("name", "院 所");
        orgMap.put("key", "4");

        Map<String, String> productMap = new HashMap<>();
        productMap.put("name", "产 品");
        productMap.put("key", "5");


        dataSource.add(compMap);
        dataSource.add(expertMap);
        dataSource.add(requMap);
        dataSource.add(achvMap);
        dataSource.add(productMap);

        //this.loadUrl("file:///android_asset/page/home/index.html");
    }

    @Override
    public void init() {
        IndexAction action = new IndexAction(context, this);
        action.execute(true);
    }

    @Override
    public void show() {
        super.show();

        if (this.getUrl() == null)
            this.loadUrl("file:///android_asset/page/home/index.html");

        if (pageLoaded) {

            IndexAction action = new IndexAction(context, this);
            action.execute(false);
        }
    }

    /**
     * 覆盖父类中的实现,父类中是关闭窗口
     */
    @Override
    public void onClickLeftBtn() {
        // TODO Auto-generated method stub
        //titleBar
        if (popupWindow != null) {

            if (adapter != null) {

                adapter.setSelectedIndex(selectedIndex);
            }
            popupWindow.toggle();

            return;
        } else {

            View popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.home_search_condition, null);
//            final String[] dataSource = new String[]{"专 家", "成 果", "企 业", "需 求","院 所"};

            adapter = new ListViewAdapter(context, dataSource);
            GridView gridview = (GridView) popupView.findViewById(R.id.home_condition_panel);
            gridview.setSelector(getResources().getDrawable(R.drawable.search_condition_item));
            gridview.setFocusable(true);
            adapter.setSelectedIndex(selectedIndex);
            gridview.setAdapter(adapter);
            final TextView anchorTextView = searchTitleBarView.getSearchType();
            final EditText editText = searchTitleBarView.getSearchEditText();
            popupWindow = new PopUpWindow(context, popupView, searchTitleBarView);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                    // TODO Auto-generated method stub
                    String value = dataSource.get(position).get("name");
                    int type = Integer.parseInt(dataSource.get(position).get("key"));
                    anchorTextView.setText(value);
//                    anchorTextView.setText(value);
                    switch (type) {

                        case 0:
                            editText.setHint("专家搜索");
                            break;
                        case 1:
                            editText.setHint("成果搜索");
                            break;
                        case 2:
                            editText.setHint("企业搜索");
                            break;
                        case 3:
                            editText.setHint("需求搜索");
                            break;
                        case 5:
                            editText.setHint("产品搜索");
                            break;
                    }

                    selectedIndex = position;
                    getTitleBarView().setType(type);
                    popupWindow.dismiss();
                }
            });
        }
    }

    @Override
    public void onClickMiddle() {
        // TODO Auto-generated method stub
        //super.onClickEdit();

        Intent intent = new Intent(context, SearchHistoryActivity.class);

        intent.putExtra("type", Integer.parseInt(dataSource.get(selectedIndex).get("key")));
        context.startActivity(intent);
    }

    /**
     * 打开企业详细页面
     **/
    @JavascriptInterface
    public void openCompDetail(int compId, String compName, String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.company.DetailActivity.class);
        intent.putExtra("compName", compName);
        intent.putExtra("compId", compId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开专家详细页面
     **/
    @JavascriptInterface
    public void openExpertDetail(int expertId, String expertName, String lastModify) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("expertName", expertName);
        intent.putExtra("expertId", expertId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openAchvDetail(int achvId, String name, String lastModify) {


//		context.startActivity(new Intent(context, CaptureActivity.class));
        Intent intent = new Intent(context, cn.fundview.app.activity.achv.DetailActivity.class);
        intent.putExtra("achvId", achvId);
        intent.putExtra("achvName", name);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开需求详细
     **/
    @JavascriptInterface
    public void openRequDetail(final int requId, final String requName, final String lastModify) {

        Intent intent = new Intent(context, cn.fundview.app.activity.requ.DetailActivity.class);
        intent.putExtra("requName", requName);
        intent.putExtra("requId", requId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }

    /**
     * 打开产品详细
     * @param productId     产品id
     * @param productName   产品名称
     * @param lastModify    更新时间
     */
    @JavascriptInterface
    public void openProductDetail(int productId, String productName, String lastModify) {

        Intent intent = new Intent(context, CompanyProductDetailActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("productId", productId);
        intent.putExtra("lastModify", lastModify);
        context.startActivity(intent);
    }



    /**
     * 加载更多项目
     **/
    @JavascriptInterface
    public void openDetail(int id, String name) {

        Intent intent = new Intent(context, FundProjectDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    //list
    @JavascriptInterface
    public void openAchvList() {

        Intent intent = new Intent(context, AchvListActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openExpertList() {
        Intent intent = new Intent(context, ExpertListActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openRequList() {
        Intent intent = new Intent(context, RequListActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openCompList() {
        Intent intent = new Intent(context, CompListActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openOrganList() {
        Intent intent = new Intent(context, OrgListActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openProductList() {
        Intent intent = new Intent(context, ProductListActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void openFundProjectList() {
        Intent intent = new Intent(context, FundProjectListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClickRight() {
        super.onClickRight();

        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivity(intent);
    }
}
