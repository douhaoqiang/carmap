package com.china.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.china.R;


/**
 * Created by renzhan on 15/6/3.
 */
public class WeiXinLoadingDialog extends Dialog {
	private TextView tv;
	String msg = "加载中...";

	public WeiXinLoadingDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}

	private WeiXinLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weixin_dialog_loading);
		tv = (TextView) this.findViewById(R.id.tv);
		tv.setText(msg);
		LinearLayout linearLayout = (LinearLayout) this
				.findViewById(R.id.LinearLayout);
		linearLayout.getBackground().setAlpha(210);
	}

	public void setShowMsg(String msg) {
		this.msg = msg;
	}

	public static WeiXinLoadingDialog weixinDialog;

	public static WeiXinLoadingDialog show(Context context) {
		return show(context, null);
	}

	public static WeiXinLoadingDialog show(Context context, String msg) {
		return show(context, msg, false);
	}

	public static WeiXinLoadingDialog show(Context context, int msgId) {
		return show(context, msgId, false);
	}

	public static WeiXinLoadingDialog show(Context context, int msgId,
			boolean isCancelable) {
		String msg = context.getResources().getString(msgId);
		return show(context, msg, isCancelable);
	}

	public static WeiXinLoadingDialog show(Context context, String msg,
			boolean isCancelable) {
		weixinDialog = new WeiXinLoadingDialog(context);
		if (msg != null)
			weixinDialog.setShowMsg(msg);
		weixinDialog.setCanceledOnTouchOutside(false);
		// 设置ProgressDialog 是否可以按退回键取消
		weixinDialog.setCancelable(isCancelable);

		weixinDialog.show();
		return weixinDialog;
	}

}
