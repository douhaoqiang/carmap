package com.china.MySpinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.china.R;


/**
 * DESC 下拉列表
 *
 * @auther dhq 827809500@qq.com  2015/8/25 0025.
 */

public class MySpinerPop<T> extends PopupWindow {

    private Context mContext;
    private ListView mListView;
    private MySpinnerAdapter mAdapter;
    ItemOnClickCallback mOnClickCallback;
    private int mWidth = 0;

    public MySpinerPop(Context context,MySpinnerAdapter adapter,ItemOnClickCallback onClickCallback) {
        super(context);
        this.mContext = context;
        this.mAdapter=adapter;
        this.mOnClickCallback=onClickCallback;
        init();
    }

    public MySpinerPop(Context context, View view,MySpinnerAdapter adapter,ItemOnClickCallback onClickCallback) {
        super(context);
        this.mContext = context;
        mWidth = view.getWidth();
        this.mAdapter=adapter;
        this.mOnClickCallback=onClickCallback;
        init();
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        if(mWidth>0){
            setWidth(mWidth);
        }else{
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T child=(T)parent.getAdapter().getItem(position);
                mOnClickCallback.onItemClick(child,position);
                dismiss();
            }
        });
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor, 0, 0);
    }


    public interface ItemOnClickCallback<T>{
        public void onItemClick(T child, int position);
    }

}
