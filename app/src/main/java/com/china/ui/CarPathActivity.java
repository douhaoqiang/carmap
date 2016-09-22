package com.china.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.androidquery.AQuery;
import com.china.InitActivity;
import com.china.R;
import com.china.entity.GpsInfoVo;
import com.china.entity.NoSwitch;
import com.china.net.UDPClient;
import com.china.service.GpsService;
import com.china.util.DialogUtil;
import com.china.util.HeaderUtils;
import com.china.util.MapUtil;
import com.china.util.PhoneUtil;
import com.china.util.PositionUtil;
import com.china.view.DialogCallBack;
import com.china.view.WeiXinLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CarPathActivity extends Activity implements MapUtil.MapCallBack{

    private AQuery aq;
    private MapUtil mapUtil;
    private MapView mapView;
    private PhoneUtil phoneUtil;
    private UDPClient udpClient;
    private TextView finish_btn;//结束按钮
    private LatLng lastPosition;//记录的最后一个位置
    private String taskId;//任务id
    private WeiXinLoadingDialog loadingDialog;

    private MyConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        aq=new AQuery(this);
        taskId=getIntent().getStringExtra("taskId");
        loadingDialog=new WeiXinLoadingDialog(this);
        mapView = (MapView) aq.id(R.id.map).getView();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        initListener();
//        mapUtil.startLocate();//开启定位

        connection=new MyConnection();

        Intent intent = new Intent(this, GpsService.class);
        bindService(intent, connection, BIND_IMPORTANT);
    }



    /**
     * 初始化AMap对象
     */
    private void initView() {
        new HeaderUtils(aq,R.string.car_map_msg);
        phoneUtil=new PhoneUtil(this);
        udpClient=new UDPClient(this);
        mapUtil=new MapUtil(this);
        mapUtil.setMap(mapView);
        mapUtil.setMapCallback(this);


        finish_btn=(TextView) findViewById(R.id.finish_btn);


    }

    /**
     * 初始化事件监听器
     */
    private void initListener() {
        aq.id(R.id.move_mylocation_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapUtil.moveToMyLocation(lastPosition);
            }
        });
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIsFinish();
            }
        });

//        gpsLoctionUtil.setLocationCallBack(new GpsLoctionUtil.GpsCallBack() {
//            @Override
//            public void callBack(Location location) {
//                drawSendLocation(location);
//            }
//        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void showIsFinish(){
        DialogUtil.showFinishPath(this,"是否确定退出？",new DialogCallBack(){
            @Override
            public void ok() {
                finishTask();
            }
        });
    }


    /**
     * 完成任务事件
     */
    private void finishTask(){
        Intent intent=new Intent(this,CarFinishActivity.class);
        intent.putExtra("taskId",taskId);
        startActivity(intent);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapUtil.onDestroy();
        unbindService(connection);

    }

    private long backPressTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressTime > 2000) {
            Toast.makeText(this, "再点击一次退出程序！", Toast.LENGTH_SHORT).show();
            backPressTime = System.currentTimeMillis();
        } else {
            InitActivity.exit(this);
        }

    }

    class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接成功

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //连接异常断开

        }
    }

    @Override
    public void callback(AMapLocation amapLocation) {
        //高德地图定位回调
        LatLng nowPosition = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//        if (lastPosition==null || mapUtil.isCanDrawCircle(nowPosition,lastPosition)){
//            lastPosition =nowPosition;
//            mapUtil.drawLocationMark(lastPosition);
//            udpClient.sendLocationMsg(amapLocation,phoneUtil.getDeviceId());
//        }
        if (lastPosition==null || mapUtil.isCanDrawCircle(nowPosition,lastPosition)){
            lastPosition =nowPosition;
            mapUtil.drawLocationMark(nowPosition);
            udpClient.sendLocationMsg(amapLocation,phoneUtil.getDeviceId());
        }

    }

    /**
     * 询问是否进入Gps设置界面
     */
    private void openGpsSetting(){

        DialogUtil.showFinishPath(this,"请打开GPS确定自己的位置？",new DialogCallBack(){
            @Override
            public void ok() {
                // 转到手机设置界面，用户设置GPS
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
            }
        });

    }

    @Subscribe
    public void onLocationEvent(Location location){
        drawSendLocation(location);
    }

    @Subscribe
    public void onNoSwitchEvent(NoSwitch location){
        openGpsSetting();
    }

    /**
     * 绘制地图坐标  并且发送位置信息
     * @param location
     */
    private void drawSendLocation(Location location){
        GpsInfoVo gpsInfoVo = PositionUtil.gps84_To_Gcj02(location.getLatitude(), location.getLongitude());
        LatLng nowPosition = new LatLng(gpsInfoVo.getLatitude(), gpsInfoVo.getLongitude());
        Toast.makeText(this,"坐标："+gpsInfoVo.getLatitude()+"---"+gpsInfoVo.getLongitude(),Toast.LENGTH_SHORT).show();
        if (lastPosition==null || mapUtil.isCanDrawCircle(nowPosition,lastPosition)){
            lastPosition =nowPosition;
            mapUtil.moveToMyLocation(lastPosition);
            mapUtil.drawLocationMark(nowPosition);
            udpClient.sendLocationMsg(location,phoneUtil.getDeviceId());
        }
    }

}
