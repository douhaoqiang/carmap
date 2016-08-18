package com.china;

/**
 * Created by Administrator on 2016/5/12.
 */
public class GlobalConstants {
    public static final String DISPATCH_URL ="http://1.119.1.2";//接口地址
    public static final int PORT=40004;//端口号

    public static String token;
    public static String SystemVersion;
    public static String ClientVersionName;
    public static String deviceId;

    public static int ScreenWidth;
    public static int DesignScreenWidth=750;


    /**
     * 获取派遣root url
     * @return
     */
    public static String getDispatchRootUrl(){
        return DISPATCH_URL+":"+PORT;
    }

    /**
     * 获取上传地址坐标的root url
     * @return
     */
    public static String getUdpRootUrl(){
        return DISPATCH_URL;
    }

    /**
     * 获取上传地址坐标的root url
     * @return
     */
    public static int getUdpPort(){
        return PORT;
    }

}
