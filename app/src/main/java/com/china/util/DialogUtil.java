package com.china.util;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.china.R;
import com.china.view.CommonDialog;
import com.china.view.DialogCallBack;

/**
 * description 提示框
 * Created by dhq on 2015/11/10.
 */
public class DialogUtil {


    //获取数据失败
    public static void showFinishPath(Context context, final String str , final DialogCallBack callBack){
        CommonDialog commonDialog=new CommonDialog(context, R.layout.common_msg_dialog, new CommonDialog.DialogDrawView() {
            @Override
            public void drawView(final Dialog dialog, View view) {
                TextView title = (TextView)view.findViewById(R.id.common_dialog_line1_text);
                TextView okBtn = (TextView)view.findViewById(R.id.common_dialog_left_btn);
                TextView cancleBtn = (TextView)view.findViewById(R.id.common_dialog_right_btn);
                title.setText(str);
                okBtn.setText(R.string.ok);
                cancleBtn.setText(R.string.cancel);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        callBack.ok();
                    }
                });
                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        callBack.cancle();
                    }
                });
            }
        });
        commonDialog.show();
    }


    //查看地勤位置（没有权限）
    public static void noAuthority(Context context, final String str , final DialogCallBack callBack){
        CommonDialog commonDialog=new CommonDialog(context, R.layout.common_dialog_one_btn, new CommonDialog.DialogDrawView() {
            @Override
            public void drawView(final Dialog dialog, View view) {
                TextView title = (TextView)view.findViewById(R.id.common_dialog_title_tv);
                Button okBtn = (Button)view.findViewById(R.id.common_dialog_ok_btn);
                title.setText(str);
                okBtn.setText(R.string.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        callBack.ok();
                    }
                });
            }
        });
        commonDialog.show();
    }

    //获取数据失败
    public static void showDataFail(Context context, final String str , final DialogCallBack callBack){
        CommonDialog commonDialog=new CommonDialog(context, R.layout.common_msg_dialog, new CommonDialog.DialogDrawView() {
            @Override
            public void drawView(final Dialog dialog, View view) {
                TextView title = (TextView)view.findViewById(R.id.common_dialog_line1_text);
                TextView okBtn = (TextView)view.findViewById(R.id.common_dialog_left_btn);
                TextView cancleBtn = (TextView)view.findViewById(R.id.common_dialog_right_btn);
                if(TextUtils.isEmpty(str)){
                    title.setText("获取数据失败!");
                }else{
                    title.setText(str);
                }
                okBtn.setText(R.string.cycle_get);
                cancleBtn.setText(R.string.cancel);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        callBack.ok();
                    }
                });
                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        callBack.cancle();
                    }
                });
            }
        });
        commonDialog.show();
    }


    //检查车辆信息不完整
    public static void checkCarDialog(Context context, final String str){
        CommonDialog commonDialog=new CommonDialog(context, R.layout.common_dialog_one_btn, new CommonDialog.DialogDrawView() {
            @Override
            public void drawView(final Dialog dialog, View view) {
                TextView title = (TextView)view.findViewById(R.id.common_dialog_title_tv);
                Button okBtn = (Button)view.findViewById(R.id.common_dialog_ok_btn);
                title.setText(str);
                okBtn.setText(R.string.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        commonDialog.show();
    }

    //检查车辆信息不完整
    public static void showTaskFinishDialog(Context context, final String str, final DialogCallBack callBack){
        CommonDialog commonDialog=new CommonDialog(context, R.layout.common_dialog_one_btn, new CommonDialog.DialogDrawView() {
            @Override
            public void drawView(final Dialog dialog, View view) {
                TextView title = (TextView)view.findViewById(R.id.common_dialog_title_tv);
                Button okBtn = (Button)view.findViewById(R.id.common_dialog_ok_btn);
                title.setText(str);
                okBtn.setText(R.string.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.ok();
                        dialog.dismiss();
                    }
                });
            }
        });
        commonDialog.show();
    }

}
