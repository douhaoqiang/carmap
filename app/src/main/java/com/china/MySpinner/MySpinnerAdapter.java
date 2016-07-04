package com.china.MySpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public class MySpinnerAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;
    public SpinnerCallBack mCallback;

    public MySpinnerAdapter(Context context, List<T> mDatas, int itemLayoutId, SpinnerCallBack spinnerCallBack) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        if(mDatas==null){
            this.mDatas = new ArrayList();
        }else{
            this.mDatas = mDatas;
        }
        this.mItemLayoutId = itemLayoutId;
        this.mCallback = spinnerCallBack;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, mItemLayoutId, convertView, parent);
        mCallback.drawChild(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    private ViewHolder getViewHolder(int position, int resId, View convertView, ViewGroup parent) {
        return ViewHolder.getNo(mContext, convertView, parent, resId, position);
    }

    public interface SpinnerCallBack<T> {
        public void drawChild(ViewHolder holder, T child, int position);
    }

}
