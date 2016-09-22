package com.china.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.china.IGpsInterface;
import com.china.entity.NoSwitch;
import com.china.util.GpsLoctionUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * DESC
 * Created by douhaoqiang on 2016/9/22.
 */

public class GpsService extends Service {

    private GpsLoctionUtil gpsLoctionUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBind();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    public void startLocate(){
        gpsLoctionUtil.startLocate();
    }

    private void init() {
        gpsLoctionUtil = new GpsLoctionUtil(this);
        gpsLoctionUtil.setLocationCallBack(new GpsLoctionUtil.GpsCallBack() {
            @Override
            public void callBack(Location location) {
                EventBus.getDefault().post(location);
            }

            @Override
            public void noSwitch() {
                EventBus.getDefault().post(new NoSwitch());
            }
        });
    }


    public class MyBind extends IGpsInterface.Stub {

        public GpsService getService(){
            return GpsService.this;
        }

        @Override
        public void callbackLocation(float latitude, float longitude) throws RemoteException {

        }

    }


}
