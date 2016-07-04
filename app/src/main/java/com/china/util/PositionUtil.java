package com.china.util;

import com.china.entity.GpsInfoVo;

import java.math.BigDecimal;


/**
 * 坐标工具类  各种坐标转换 
 * @author Yang Yang
 * 2016年3月31日
 */
public class PositionUtil {
	private static final String BAIDU_LBS_TYPE = "bd09ll";  
    
    private static double pi = 3.1415926535897932384626;  
    private static double a = 6378245.0;  
    private static double ee = 0.00669342162296594323;  
  
    /** 
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System 
     *  (浩强，注意了，我们使用这个就可以了)
     * @param lat 
     * @param lon 
     * @return 
     */  
    public static GpsInfoVo gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {  
            return null;  
        }  
        double dLat = transformLat(lon - 105.0, lat - 35.0);  
        double dLon = transformLon(lon - 105.0, lat - 35.0);  
        double radLat = lat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        double mgLat = lat + dLat;  
        double mgLon = lon + dLon; 
        
        double formatLat = new   BigDecimal(mgLat).setScale(8,   BigDecimal.ROUND_HALF_UP).doubleValue();
    	double formatLon = new   BigDecimal(mgLon).setScale(8,   BigDecimal.ROUND_HALF_UP).doubleValue();
        
        return new GpsInfoVo(formatLon,formatLat);  
    }  
  
    /** 
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return 
     * */  
    public static GpsInfoVo gcj_To_Gps84(double lat, double lon) {  
    	GpsInfoVo gps = transform(lat, lon);  
        double lontitude = lon * 2 - gps.getLongitude();  
        double latitude = lat * 2 - gps.getLatitude();  
        return new GpsInfoVo(latitude, lontitude);  
    }  
  
    /** 
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标 
     *  
     * @param gg_lat 
     * @param gg_lon 
     */  
    private static GpsInfoVo gcj02_To_Bd09(double gg_lat, double gg_lon) {  
        double x = gg_lon, y = gg_lat;  
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);  
        double bd_lon = z * Math.cos(theta) + 0.0065;  
        double bd_lat = z * Math.sin(theta) + 0.006;  
        return new GpsInfoVo(bd_lat, bd_lon);  
    }  
  
    /** 
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * 
     * @param bd_lat
     * @param bd_lon 
     * @return 
     */  
    private static GpsInfoVo bd09_To_Gcj02(double bd_lat, double bd_lon) {  
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;  
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);  
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);  
        double gg_lon = z * Math.cos(theta);  
        double gg_lat = z * Math.sin(theta);  
        return new GpsInfoVo(gg_lat, gg_lon);  
    }  
  
    /** 
     * (BD-09)-->84 
     * @param bd_lat 
     * @param bd_lon 
     * @return 
     */  
    private static GpsInfoVo bd09_To_Gps84(double bd_lat, double bd_lon) {  
  
    	GpsInfoVo gcj02 = PositionUtil.bd09_To_Gcj02(bd_lat, bd_lon);  
    	GpsInfoVo map84 = PositionUtil.gcj_To_Gps84(gcj02.getLatitude(),  
                gcj02.getLongitude());  
        return map84;  
  
    }  
  
    private static boolean outOfChina(double lat, double lon) {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }  
  
    private static GpsInfoVo transform(double lat, double lon) {  
        if (outOfChina(lat, lon)) {  
            return new GpsInfoVo(lat, lon);  
        }  
        double dLat = transformLat(lon - 105.0, lat - 35.0);  
        double dLon = transformLon(lon - 105.0, lat - 35.0);  
        double radLat = lat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        double mgLat = lat + dLat;  
        double mgLon = lon + dLon;  
        return new GpsInfoVo(mgLat, mgLon);  
    }  
  
    private static double transformLat(double x, double y) {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y  
                + 0.2 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }  
  
    private static double transformLon(double x, double y) {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1  
                * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0  
                * pi)) * 2.0 / 3.0;  
        return ret;  
    }  
  
    /**
     * 84 to 火星坐标系 (GCJ-02)  火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 
     * @param lat
     * @param lon
     * @return
     */
    private static GpsInfoVo gps84_To_Bd09(double lat, double lon) {  
    	GpsInfoVo gcj02 = gps84_To_Gcj02(lat, lon);  
    	return gcj02_To_Bd09(gcj02.getLatitude(), gcj02.getLongitude());
    }
    
    public static void main(String[] args) {  
        // 北斗芯片获取的经纬度为WGS84地理坐标 31.426896,119.496145  
        //Gps gps = new Gps(39.915580, 116.481073);  
        //System.out.println("gps :" + gps); 
        //gps 转 火星
        //Gps gcj = gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());  
        //System.out.println("gcj :" + gcj);  
        //Gps star = gcj_To_Gps84(gcj.getWgLat(), gcj.getWgLon());  
       // System.out.println("star:" + star); 
        //火星 转 百度
        //Gps bd = gcj02_To_Bd09(gcj.getWgLat(), gcj.getWgLon());  
        //System.out.println("bd  :" + bd);  
    	//Gps gcj2 = bd09_To_Gcj02(bd.getWgLat(), bd.getWgLon());  
    	//System.out.println("gcj :" + gcj2);	
    	//bd
    	GpsInfoVo gps = new GpsInfoVo(39.92286987381178, 116.49364201546106); 
    	GpsInfoVo gcj2 = bd09_To_Gcj02(gps.getLatitude(), gps.getLongitude());
    	
    	/*
    	double dd = 39.91668340845874;
    	BigDecimal   b   =   new   BigDecimal(dd);  
    	double   f1   =   b.setScale(8,   BigDecimal.ROUND_HALF_UP).doubleValue();  
    	System.out.println(f1);*/
    }  
}
