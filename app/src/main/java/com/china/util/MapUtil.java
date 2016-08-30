package com.china.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.china.BuildConfig;
import com.china.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/6.
 */
public class MapUtil {

    private Context context;
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public MapCallBack mCallback;
    private boolean isFirst = true;
    private Marker locationMarker;

    public MapUtil(Context context) {
        this.context = context;
    }

    /**
     * 设置要处理的地图引用
     *
     * @param mapView
     */
    public void setMap(MapView mapView) {
        this.mapView = mapView;
        this.aMap = this.mapView.getMap();
        setUpMap();
        initLocationClient();
    }

    public void setMapCallback(MapCallBack callback) {
        this.mCallback = callback;
    }

    public AMap getAMap() {
        return aMap;
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.point4));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);//是否显示缩放图标
//        aMap.setMyLocationStyle(myLocationStyle);
        mUiSettings.setMyLocationButtonEnabled(false);// 是否显示定位按钮
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }


    /**
     * 初始化定位信息
     */
    private void initLocationClient() {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    /**
     * 开启定位
     */
    public void startLocate() {
        startPhoneLocation();
        startMapLocation();
    }


    /**
     * 开启手机定位
     */
    private void startPhoneLocation(){

    }

    /**
     * 开启高德定位
     */
    private void startMapLocation(){
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10 * 1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }



    /**
     * 绘制图标
     */
//    private void drawMarker(LatLng poi) {
//        //绘制marker
//        Marker marker = aMap.addMarker(new MarkerOptions()
//                .position(new LatLng(39.986919, 116.353369))
//                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.poi_marker_1)))
//                .draggable(true));
//    }

    /**
     * 绘制两点间曲线图
     */
    private void drawLine(LatLng startPoi, LatLng endPoi) {
        // 绘制曲线
        aMap.addPolyline((new PolylineOptions())
                .add(new LatLng(43.828, 87.621), new LatLng(45.808, 126.55))
                .geodesic(true).color(Color.RED));
    }

    /**
     * 绘制多个点的曲线
     *
     * @param pois
     */
    private void drawLine(ArrayList<LatLng> pois) {
        aMap.addPolyline((new PolylineOptions())
                .addAll(pois)
                .geodesic(true).color(Color.RED));
    }

    private void stopLocation() {
        if(mLocationClient!=null){
            mLocationClient.stopLocation();//停止定位
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }

    public void onDestroy() {
        stopLocation();
        mapView.onDestroy();

    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    printLocationMsg(amapLocation);
                    if (mCallback != null) {
                        mCallback.callback(amapLocation);
                    }
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 打印定位信息
     *
     * @param amapLocation
     */
    private void printLocationMsg(AMapLocation amapLocation) {

        if (!BuildConfig.DEBUG) {
            return;
        }

        //定位成功回调信息，设置相关消息
        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        amapLocation.getLatitude();//获取纬度
        amapLocation.getLongitude();//获取经度
        amapLocation.getAccuracy();//获取精度信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(amapLocation.getTime());
        df.format(date);//定位时间
        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        amapLocation.getCountry();//国家信息
        amapLocation.getProvince();//省信息
        amapLocation.getCity();//城市信息
        amapLocation.getDistrict();//城区信息
        amapLocation.getStreet();//街道信息
        amapLocation.getStreetNum();//街道门牌号信息
        amapLocation.getCityCode();//城市编码
        amapLocation.getAdCode();//地区编码
        Log.e("location信息：", amapLocation.toString());

    }


    /**
     * 将地图中心移动到某一个坐标上
     *
     * @param point
     */
    public void moveToMyLocation(LatLng point) {
        if (point != null) {
            CameraUpdate update = CameraUpdateFactory.changeLatLng(point);//将地图中心点移动到指定坐标
            aMap.moveCamera(update);
        }
    }


    /**
     * 绘制现在所在的位置
     * @param point
     */
    public void drawLocationMark(LatLng point){

        if (isFirst) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(point, 14f);//将地图中心点移动到指定坐标、以及指定地图放大倍数
            aMap.moveCamera(update);
            isFirst = false;
        }

        if(locationMarker!=null){
            locationMarker.setPosition(point);
            aMap.invalidate();// 刷新地图
        }else{
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(point);
            markerOption.draggable(false);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(getMarkBitmap()));
            locationMarker = aMap.addMarker(markerOption);
        }
    }

    /**
     * 在地图上绘制原点
     *
     * @param point
     */
    public void drawLineCircle(LatLng point) {
        if (isFirst) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(point, 17f);//将地图中心点移动到指定坐标、以及指定地图放大倍数
            aMap.moveCamera(update);
            isFirst = false;
        }
        drawCircle(point, 6, Color.parseColor("#aa4c90f9"), 0, 0);
    }

    /**
     * 绘制圆
     *
     * @param center      //圆中心点
     * @param radius      //圆半径
     * @param fillColor   //圆颜色
     * @param strokeColor //圆边框颜色
     * @param strokeWidth //圆边框宽度
     */
    private void drawCircle(LatLng center, double radius, int fillColor, int strokeColor, int strokeWidth) {
        aMap.addCircle(new CircleOptions()
                .center(center)//圆心
                .radius(radius)//半径
                .strokeColor(strokeColor)//边框的颜色
                .strokeWidth(strokeWidth)//边框宽度
                .fillColor(fillColor)//圆内的颜色
        );
    }

    public boolean isCanDrawCircle(LatLng nowLocation,LatLng lastLocation) {
        LatLng startLatlng = new LatLng(39.90403, 116.407525);
        LatLng endLatlng = new LatLng(39.983456, 116.3154950);
        // 计算量坐标点距离
        float distance = AMapUtils.calculateLineDistance(lastLocation, nowLocation);
        return distance > 10 ? true : false;
    }


    public Bitmap getMarkBitmap( ) {
        int width = (int) FontSizeUtils.getPx(100);
        int height = (int) FontSizeUtils.getPx(100);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        Bitmap bgbmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.location);
        // 将bmp1绘制在画布上
        Rect srcRect = new Rect(0, 0, width, height);// 截取bmp1中的矩形区域
        Rect dstRect = new Rect(0, 0, width,height);// bmp1在目标画布中的位置
        canvas.drawBitmap(bgbmp, srcRect, dstRect, paint);
        return bitmap;
    }

    public interface MapCallBack {
        public void callback(AMapLocation amapLocation);
    }

}
