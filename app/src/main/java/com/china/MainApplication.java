package com.china;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.androidquery.util.AQUtility;
import com.china.util.PackageUtil;
import com.china.util.PhoneUtil;
import com.china.util.SystemInfoUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    private static MainApplication instance;
    private List<Activity> activityList = new ArrayList<Activity>();

    /**
     * @return true -
     */
    static boolean canStopApplication() {
        //
        // if (!DownloadManager.release(false)) return false;
        return true;
    }

    public static MainApplication getInstance() {
        if (instance == null) {
            instance = new MainApplication();
        }
        return instance;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            PhoneUtil phoneUtil=new PhoneUtil(this);
            PackageUtil packageUtil=new PackageUtil(this);

            /**
             * 获取手机系统版本
             */
            GlobalConstants.SystemVersion = phoneUtil.getVersion();

            /**
             * 获取手机设备号
             */
            GlobalConstants.deviceId=phoneUtil.getDeviceId();

            /**
             * 获取app版本号
             */
            GlobalConstants.ClientVersionName = packageUtil.getAppVersionName();


            DisplayMetrics dm = getResources().getDisplayMetrics();
            GlobalConstants.ScreenWidth=dm.widthPixels;
//            GlobalConstants.DesignScreenWidth=dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        initImageLoader();
        ApplicationInfo info = getApplicationInfo();
    }


    public static boolean existSDcard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else
            return false;
    }


    public void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        cacheDir = new File(cacheDir, getYidumiDataDir());

//	    Log.e("infe","图片缓存地址："+cacheDir.toString());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }


    public String getYidumiDataDir() {
        File file = this.getFilesDir();
        file = new File(file.getParent() + "/default");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }


}
