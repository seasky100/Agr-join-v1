package cn.fundview.app.tool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lict on 2016/5/14.
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<T> mDataSource;
    private Context mContext;
    private int mLayoutId;
    private MyRecyclerViewItemOnClickListener clickListener;
    public RecyclerViewAdapter(List<T> dataSource, Context context, int layoutId) {

        this.mContext = context;
        this.mDataSource = dataSource;
        this.mLayoutId = layoutId;
    }

    /**
     * 追加数据源
     *
     * @param datas
     */
    public void appendData(List<T> datas) {

        if (datas != null && datas.size() > 0) {

            int startPosition = mDataSource.size() - 1;
            mDataSource.addAll(datas);
            notifyItemRangeChanged(startPosition, datas.size());
        }
    }

    /**
     * 向前插入数据
     *
     * @param datas
     */
    public void insertData(List<T> datas) {

        if (datas != null && datas.size() > 0) {
            List<T> tempDatas = new ArrayList<>();

            tempDatas.addAll(datas);
            tempDatas.addAll(mDataSource);
            mDataSource = tempDatas;
            notifyItemRangeChanged(0, mDataSource.size());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        viewHolder.setClickListener(clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        bindViewHolder(holder, mDataSource.get(position));
    }

    /**
     * 真实的负责 将数据和view 绑定
     *
     * @param viewHolder
     * @param item
     */
    public abstract void bindViewHolder(MyViewHolder viewHolder, T item);

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public interface  MyRecyclerViewItemOnClickListener{

        void onClick(View view, int position);
    }

    public void setClickListener(MyRecyclerViewItemOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View convertView;
        private MyRecyclerViewItemOnClickListener clickListener;
        public MyViewHolder(View itemView) {
            super(itemView);
            convertView = itemView;
            convertView.setOnClickListener(this);
        }

        public <T extends View> T getView(int viewId) {

            return (T) convertView.findViewById(viewId);
        }

        public View getConvertView() {
            return convertView;
        }


        /**
         * 为TextView设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public MyViewHolder setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param drawableId
         * @return
         */
        public MyViewHolder setImageResource(int viewId, int drawableId) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableId);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param bm
         * @return
         */
        public MyViewHolder setImageBitmap(int viewId, Bitmap bm) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bm);
            return this;
        }

        /**
         * 为ImageView设置图片
         *
         * @param viewId
         * @param url
         * @return
         */
        public MyViewHolder setImageByUrl(int viewId, String url, int loadingImageId, int failImageId) {
            ImageView view = getView(viewId);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(loadingImageId)
                    .showImageOnFail(failImageId)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(url, view, options);
            return this;
        }

        @Override
        public void onClick(View v) {

            if(clickListener != null) {
                clickListener.onClick(v,getAdapterPosition());
            }
        }

        public void setClickListener(MyRecyclerViewItemOnClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }
}