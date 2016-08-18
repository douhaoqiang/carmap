package com.china;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.androidquery.util.AQUtility;
import com.china.ui.SplashActivity;

public class InitActivity extends Activity {

	private static boolean isExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onNewIntent(this.getIntent());
		WindowManager wm = this.getWindowManager();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent tIntent) {
		if (!isExit) {
			Intent intent = new Intent(this, SplashActivity.class);
			startActivity(intent);
		} else {
			isExit = false;
			finish();
			AQUtility.postDelayed(new Runnable() {
				@Override
				public void run() {
					// Kill process
					System.exit(0);
					// android.os.Process.killProcess(android.os.Process.myPid());
				}
			}, 500);
		}
	}

	//
	public static void exit(Context context) {
		if (MainApplication.canStopApplication()) {
			isExit = true;
			Intent intent = new Intent(context, InitActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			context.startActivity(intent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
