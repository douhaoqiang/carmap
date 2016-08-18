package com.china;

/**
 * Created by Administrator on 2016/5/12.
 */
public class GlobalConstants {
    private static final String DISPATCH_URL ="http://1.119.1.2";//接口地址
    private static final int DISPATCH_PORT=40004;//任务的端口号
    private static final int UDP_PORT=40004;//Udp请求端口号

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
        return DISPATCH_URL+":"+DISPATCH_PORT;
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
        return UDP_PORT;
    }

}
