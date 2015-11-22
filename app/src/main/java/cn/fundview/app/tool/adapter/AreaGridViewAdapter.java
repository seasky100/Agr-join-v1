package cn.fundview.app.tool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.fundview.R;

public class AreaGridViewAdapter extends BaseAdapter {

    private int selectedIndex;
    private String[] dataSource;
    private Context context;
    private LayoutInflater inflater;

    public AreaGridViewAdapter(Context context, String[] dataSource) {

        this.context = context;
        this.dataSource = dataSource;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataSource.length;
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
            convertView = inflater.inflate(R.layout.area_search_condition_item, null);
        }
        TextView mTitle = (TextView) convertView.findViewById(R.id.text1);

        mTitle.setText(dataSource[position]);
        if (selectedIndex == position) {

            convertView.setBackgroundColor(context.getResources().getColor(R.color.search_condition_item_1));

        }

        return convertView;
    }

    public String[] getDataSource() {
        return dataSource;
    }

    public void setDataSource(String[] dataSource) {
        this.dataSource = dataSource;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

}
