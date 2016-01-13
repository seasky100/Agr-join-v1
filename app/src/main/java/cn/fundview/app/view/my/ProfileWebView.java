package cn.fundview.app.view.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.R;
import cn.fundview.app.ProfileLinkManActivity;
import cn.fundview.app.action.my.InitProfileAction;
import cn.fundview.app.action.my.SaveProfileCompTypeAction;
import cn.fundview.app.activity.my.ProfileAddrActivity;
import cn.fundview.app.activity.my.ProfileAreaActivity;
import cn.fundview.app.activity.my.ProfileIconActivity;
import cn.fundview.app.activity.my.ProfileInforActivity;
import cn.fundview.app.activity.my.ProfileNameActivity;
import cn.fundview.app.activity.my.ProfileProfessionalTitleActivity;
import cn.fundview.app.activity.my.ProfileQrcodeActivity;
import cn.fundview.app.activity.my.ProfileTelActivity;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.PopUpWindow;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.tool.adapter.ListViewAdapter;
import cn.fundview.app.view.ABaseWebView;

public class ProfileWebView extends ABaseWebView {

    //请求吗
    public static final int AREAREQUESTCODE = 111;

    private PopUpWindow popupWindow;
    final List<Map<String, String>> dataSource = new ArrayList<>();  //企业类型
    private int selectedIndex = 0;//当前选中的下标索引
    private ListViewAdapter adapter;

    public ProfileWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);


        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "加工企业");
        map1.put("key", "1");

        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "农资企业");
        map2.put("key", "2");

        Map<String, String> map3 = new HashMap<>();
        map3.put("name", "生产企业");
        map3.put("key", "3");

        Map<String, String> map4 = new HashMap<>();
        map4.put("name", "流通企业");
        map4.put("key", "4");

        Map<String, String> map5 = new HashMap<>();
        map5.put("name", "其他企业");
        map5.put("key", "5");

        Map<String, String> map7 = new HashMap<>();
        map7.put("name", "批发市场");
        map7.put("key", "7");

        Map<String, String> map6 = new HashMap<>();
        map6.put("name", "农村中介组织");
        map6.put("key", "6");

        Map<String, String> map8 = new HashMap<>();
        map8.put("name", "家庭农场");
        map8.put("key", "8");

        dataSource.add(map1);
        dataSource.add(map2);
        dataSource.add(map3);
        dataSource.add(map4);
        dataSource.add(map7);
        dataSource.add(map6);
        dataSource.add(map8);
        dataSource.add(map5);
        this.loadUrl("file:///android_asset/page/my/profile.html");
    }

    @Override
    public void init() {

        InitProfileAction action = new InitProfileAction(context, this);
        action.execute();
    }

    /**
     * 打开新页面
     **/
    @JavascriptInterface
    public void openPage(String page, boolean editable) {

        if ("profile-head".equals(page)) {
            Intent intent = new Intent(context, ProfileIconActivity.class);
            intent.putExtra("uid", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));
            context.startActivity(intent);
        } else if ("profile-name".equals(page)) {

            Intent intent = new Intent(context, ProfileNameActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        } else if ("profile-addr".equals(page)) {

            Intent intent = new Intent(context, ProfileAddrActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        } else if ("profile-tel".equals(page)) {

            Intent intent = new Intent(context, ProfileTelActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        } else if ("profile-infor".equals(page)) {

            Intent intent = new Intent(context, ProfileInforActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        } else if ("profile-qrcode".equals(page)) {//

            Intent intent = new Intent(context, ProfileQrcodeActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        } else if ("profile-professional-title".equals(page)) {

            //专家的职称
            Intent intent = new Intent(context, ProfileProfessionalTitleActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        } else if ("profile-linkMan".equals(page)) {//企业联系人

            //专家的所属单位
            Intent intent = new Intent(context, ProfileLinkManActivity.class);
            intent.putExtra("editable", editable);
            context.startActivity(intent);
        }
    }

    @JavascriptInterface
    public void showCompType(final int compType) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.home_search_condition, null);
//            final String[] dataSource = new String[]{"专 家", "成 果", "企 业", "需 求","机 构"};
                adapter = new ListViewAdapter(context, dataSource);
                GridView gridview = (GridView) popupView.findViewById(R.id.home_condition_panel);
                gridview.setSelector(getResources().getDrawable(R.drawable.search_condition_item));
                gridview.setFocusable(true);
                adapter.setSelectedIndex(compType);
                gridview.setAdapter(adapter);
                popupWindow = new PopUpWindow(context, popupView, searchTitleBarView);

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                        // TODO Auto-generated method stub
                        String value = dataSource.get(position).get("name");
                        int key = Integer.parseInt(dataSource.get(position).get("key"));
                        popupWindow.dismiss();

                        SaveProfileCompTypeAction action = new SaveProfileCompTypeAction(context, ProfileWebView.this);
                        action.execute(key, "Page.setCompType", PreferencesUtils.getInt(context, Constants.ACCOUNT_ID));
                    }
                });

            }
        });
    }


    /**
     * 打开地区列表
     *
     * @param provinceId
     */
    @JavascriptInterface
    public void openAreaList(int provinceId, int cityId, int countyId) {

        Bundle bundle = new Bundle();
        bundle.putInt("provinceId", provinceId);        //省id
        bundle.putInt("cityId", cityId);                //市id
        bundle.putInt("countyId", countyId);            //县id
        Intent intent = new Intent(context, ProfileAreaActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    /*
     * 重新回到该页面
     **/
    public void onRestart() {

        InitProfileAction action = new InitProfileAction(context, this);
        action.execute();
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {

    }
}
