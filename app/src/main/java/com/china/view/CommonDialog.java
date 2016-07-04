package com.china.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.china.R;


/**
 * Created by douhaoqiang on 15/10/8
 */
public class CommonDialog extends Dialog {
    private Context context;
    private int mResId = 0;
    private DialogDrawView mDialogDrawView;

    public CommonDialog(Context context, int resId, DialogDrawView dialogDrawView) {
        super(context, R.style.CustomDialog);
        setOwnerActivity((Activity) context);

        this.context = context;
        this.mResId = resId;
        this.mDialogDrawView=dialogDrawView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(mResId, null);
        setContentView(view);
        drawView(this,view);
    }

    public void isCanCancle(boolean isCanCancle){
        setCancelable(isCanCancle);
    }


    /**
     * 设置Window的背景颜色
     */
    public final void setWindowBackgroundColor(int bgColor) {
        getWindow().setBackgroundDrawable(new ColorDrawable(bgColor));
    }

    private void drawView(Dialog dialog, View view){
        mDialogDrawView.drawView(dialog,view);
    }


    public interface DialogDrawView{
        public void drawView(Dialog dialog, View view);
    }


}
