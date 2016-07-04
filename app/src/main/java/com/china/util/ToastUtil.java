/**
 * 
 */
package com.china.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtil {

	private static Toast mToast = null;
	private static Context mContext = null;

	public static void timeOut(Context mContext) {
		Toast.makeText(mContext, "网络连接失败，请检查网络！", Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	public static void show(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	public static void init(Context context) {
		mContext = context;
	}




}
