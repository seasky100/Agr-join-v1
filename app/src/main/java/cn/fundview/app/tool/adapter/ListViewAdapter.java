package cn.fundview.app.tool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.fundview.R;

public class ListViewAdapter extends BaseAdapter {


    private int selectedIndex;
    private List<Map<String, String>> dataSource;
    private Context context;
    private LayoutInflater inflater;

    public ListViewAdapter(Context context, List<Map<String, String>> dataSource) {

        this.context = context;
        this.dataSource = dataSource;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_condition_item, null);
        }

        TextView mTitle = (TextView) convertView.findViewById(R.id.text1);

        mTitle.setText(dataSource.get(position).get("name"));
        int key = Integer.parseInt(dataSource.get(position).get("key"));
        if (selectedIndex == key) {

            mTitle.setTextColor(context.getResources().getColor(R.color.title_bar_bg_color_1));
            convertView.setBackgroundResource(R.drawable.green_border);
        } else {

            convertView.setBackgroundResource(R.drawable.search_condition_item);
            mTitle.setTextColor(context.getResources().getColorStateList(R.color.search_condition_item_textview));
        }
        return convertView;
    }

    public List<Map<String, String>> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<Map<String, String>> dataSource) {
        this.dataSource = dataSource;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

}
