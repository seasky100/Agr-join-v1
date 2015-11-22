package cn.fundview.app.tool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.fundview.R;

public class GridViewAdapter extends BaseAdapter {

    private int[] mIconIDs;
    private String[] mTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    private Bitmap iconBitmap;

    public String[] getmTitles() {
        return mTitles;
    }

    public void setmTitles(String[] mTitles) {
        this.mTitles = mTitles;
    }

    public GridViewAdapter(Context context, String[] titles, int[] ids) {
        this.mContext = context;
        this.mIconIDs = ids;
        this.mTitles = titles;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mIconIDs.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        if(convertView==null){
//            convertView = mInflater.inflate(R.layout.search_condition_item_1, null);
//        }
//        ImageView mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
//        TextView mTitle=(TextView)convertView.findViewById(R.id.text_list_item);

//        mTitle.setText(mTitles[position]);
        Drawable d = mContext.getResources().getDrawable(mIconIDs[position]);
        iconBitmap = ((BitmapDrawable) d).getBitmap();
//        mImage.setImageBitmap(iconBitmap);

        return convertView;
    }

    private Bitmap getPropThumnail(int id) {
        Drawable d = mContext.getResources().getDrawable(id);
        Bitmap b = ((BitmapDrawable) d).getBitmap();
        int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
        int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);

        Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);

        return thumBitmap;
    }

}
