package com.china.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
//import android.content.pm.PackageParser;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.androidquery.util.AQUtility;

import java.io.File;

public class PackageUtil {

    private Context mContext;

    public PackageUtil(Context context){
        this.mContext=context;
    }

    //*****************************************************************************************
    // 获取已经安装的应用程序的相关信息
    //*****************************************************************************************

    /**
     * 获取已安装应用程序对应的PackageInfo。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身对应的数据。
     * @return null - 未安装对应的应用程序
     */
    public PackageInfo getAppPackageInfo(String... packageName) {
        try {
            String name = packageName.length > 0 ? packageName[0] : null;
            if (name == null || name.length() == 0) name = AQUtility.getContext().getPackageName();
            PackageManager manager = mContext.getPackageManager();
            return manager.getPackageInfo(name, 0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取已安装应用程序对应的ApplicationInfo。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身对应的数据。
     * @return null - 未安装对应的应用程序
     */
    static public ApplicationInfo getAppApplicationInfo(String... packageName) {
        try {
            String name = packageName.length > 0 ? packageName[0] : null;
            if (name == null || name.length() == 0) name = AQUtility.getContext().getPackageName();
            return AQUtility.getContext().getPackageManager().getApplicationInfo(name, 0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前应用程序对应的PackageName
     *
     * @return
     */
    static public String getAppPackageName() {
        return AQUtility.getContext().getPackageName();
    }

    /**
     * 获取已安装应用程序Main Activity对应的Intent。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身对应的数据。
     * @return null - 未安装对应的应用程序或不存在Main Activity
     */
    static public Intent getAppMainActivity(String... packageName) {
        String name = packageName.length > 0 ? packageName[0] : null;
        if (name == null || name.length() == 0) name = AQUtility.getContext().getPackageName();
        return AQUtility.getContext().getPackageManager().getLaunchIntentForPackage(name);
    }

    /**
     * 获取已安装应用程序的显示名称。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身对应的数据。
     * @return null - 未安装对应的应用程序
     */
    static public String getAppLabel(String... packageName) {
        ApplicationInfo ai = getAppApplicationInfo(packageName);
        if (ai == null) return null;
        CharSequence label = AQUtility.getContext().getPackageManager().getApplicationLabel(ai);
        if (label == null) return null;
        return label.toString();
    }

    /**
     * 获取已安装应用程序的小图标。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身对应的数据。
     * @return null - 未安装对应的应用程序
     */
    static public Drawable getAppIcon(String... packageName) {
        ApplicationInfo ai = getAppApplicationInfo(packageName);
        if (ai == null) return null;
        return AQUtility.getContext().getPackageManager().getApplicationIcon(ai);
    }

    /**
     * 获取已安装应用程序的大图标。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身对应的数据。
     * @return null - 未安装对应的应用程序
     */
    @SuppressLint("NewApi")
    static public Drawable getAppLogo(String... packageName) {
        ApplicationInfo ai = getAppApplicationInfo(packageName);
        if (ai == null) return null;
        return AQUtility.getContext().getPackageManager().getApplicationLogo(ai);
    }

    /**
     * 获取已安装应用程序的VersionName。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身的VersionName。
     * @return null - 未安装对应的应用程序
     */
    public String getAppVersionName(String... packageName) {
        PackageInfo pi = getAppPackageInfo(packageName);
        if (pi == null) return null;
        return pi.versionName;
    }

    /**
     * 获取已安装应用程序的VersionCode。
     *
     * @param packageName （可选）应用程序的PackageName。如果为空，则返回程序本身的VersionCode。
     * @return -1 - 未安装对应的应用程序
     */
    public int getAppVersionCode(String... packageName) {
        PackageInfo pi = getAppPackageInfo(packageName);
        if (pi == null) return -1;
        return pi.versionCode;
    }

    //*****************************************************************************************
    // 获取未安装的APK文件的相关信息
    //*****************************************************************************************

    /**
     * 获取APK文件对应的PackageInfo。
     *
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
    static public PackageInfo getApkPackageInfo(String apkFileName) {
        try {
            if (apkFileName == null) return null;
            PackageManager manager = AQUtility.getContext().getPackageManager();
            return manager.getPackageArchiveInfo(apkFileName, 0);
        } catch (Exception e) {
            Log.i("AppInstallFunc", e.getMessage());
            return null;
        }
    }

    /**
     * 获取APK文件对应的PackageInfo。
     *
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
    static public ApplicationInfo getApkApplicationInfo(String apkFileName) {
        try {
            if (apkFileName == null) return null;
            PackageManager manager = AQUtility.getContext().getPackageManager();
            PackageInfo pi = manager.getPackageArchiveInfo(apkFileName, 0);
            return pi != null ? pi.applicationInfo : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取APK文件对应的Resource。
     * @param apkFileName APK文件的全路径文件名。
     * @return null - 错误
     */
//	static public Resources getApkResource(String apkFileName) {
//		try {
//			if (apkFileName == null) return null;
//			// 使用Android隐藏API，创建AssetManager并addAssetPath
//			AssetManager assetManager = new AssetManager();
//			assetManager.addAssetPath(apkFileName);
//			// create new Resource with AssetManager
//			Resources oldRes = AQUtility.getContext().getResources();
//			Resources newRes = new Resources(assetManager, oldRes.getDisplayMetrics(), oldRes.getConfiguration());
//			return newRes;
//		} catch (Exception e) {
//			return null;
//		}
//	}

    /**
     * 获取APK文件的PackageName。
     *
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
    static public String getApkPackageName(String apkFileName) {
        PackageInfo pi = getApkPackageInfo(apkFileName);
        if (pi == null) return null;
        return pi.packageName;
    }

    /**
     * 获取APK文件的Main Activity。
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
//	static public String getApkMainActivity(String apkFileName) {
//		if (apkFileName == null) return null;
//		// 使用Android隐藏API，分析APK文件
//		PackageParser packageParser = new PackageParser(null);
//		PackageParser.Package info = packageParser.parsePackage(new File(apkFileName), null, new DisplayMetrics(), 0);
//		if (info == null || info.activities == null) return null;
//		for (int i = 0; i < info.activities.size(); i++) {
//			PackageParser.Activity activity = info.activities.get(i);
//			ArrayList<? extends IntentFilter> intents = activity.intents;
//			if (intents == null || intents.size() == 0) continue;
//			IntentFilter intent = intents.get(0);
//			if (intent.hasAction(Intent.ACTION_MAIN) && (intent.hasCategory(Intent.CATEGORY_INFO) || intent.hasCategory(Intent.CATEGORY_LAUNCHER)) || intent.hasCategory(Intent.CATEGORY_DEFAULT) ) {
//				return activity.getComponentName().getClassName();
//			}
//		}
//		return null;
//	}

    /**
     * 获取APK文件的显示名称。
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
//	static public String getApkLabel(String apkFileName) {
//		ApplicationInfo ai = getApkApplicationInfo(apkFileName);
//		if (ai == null) return null;
//		Resources res = getApkResource(apkFileName);
//		if (res == null) return null;
//		CharSequence label = ai.labelRes != 0 ? res.getText(ai.labelRes) : null;
//		if (label == null) label = ai.nonLocalizedLabel;
//		if (label == null) label = ai.packageName;
//		return label.toString();
//	}

    /**
     * 获取APK文件的小图标。
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
//	static public Drawable getApkIcon(String apkFileName) {
//		ApplicationInfo ai = getApkApplicationInfo(apkFileName);
//		if (ai == null || ai.icon == 0) return null;
//		Resources res = getApkResource(apkFileName);
//		if (res == null) return null;
//		return res.getDrawable(ai.icon);
//	}

    /**
     * 获取APK文件的大图标。
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
//	@SuppressLint("NewApi")
//	static public Drawable getApkLogo(String apkFileName) {
//		ApplicationInfo ai = getApkApplicationInfo(apkFileName);
//		if (ai == null || ai.logo == 0) return null;
//		Resources res = getApkResource(apkFileName);
//		if (res == null) return null;
//		return res.getDrawable(ai.logo);
//	}

    /**
     * 获取APK文件的VersionName。
     *
     * @param apkFileName APK文件的全路径文件名。
     * @return null - APK文件无法解析
     */
    static public String getApkVersionName(String apkFileName) {
        PackageInfo pi = getApkPackageInfo(apkFileName);
        if (pi == null) return null;
        return pi.versionName;
    }

    /**
     * 获取APK文件的VersionCode。
     *
     * @param apkFileName APK文件的全路径文件名。
     * @return -1 - APK文件无法解析
     */
    static public int getApkVersionCode(String apkFileName) {
        PackageInfo pi = getApkPackageInfo(apkFileName);
        if (pi == null) return -1;
        return pi.versionCode;
    }

    /**
     * 安装指定路径下的APK。
     *
     * @param apkFile 应用程序的APK文件
     */
    @TargetApi(9)
    public static void installPackage(File apkFile) {
        if (apkFile == null || !apkFile.exists()) return;
        // 必须设置文件的属性，否则安装失败
        apkFile.setReadable(true, false);
        apkFile.setExecutable(true, false);
        // 启动系统应用安装界面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        AQUtility.getContext().startActivity(intent);
    }

    /**
     * 卸载指定的应用程序。
     *
     * @param packageName 应用程序的PackageName
     */
    @TargetApi(9)
    public static void uninstallPackage(String packageName) {
        if (packageName == null || packageName.length() == 0) return;
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse(packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AQUtility.getContext().startActivity(intent);
    }
}
