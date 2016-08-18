package com.china;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/5/12.
 */
public class GlobalConstants {

    public static final String PRE_NAME ="carmap";//偏好的设置名称
    private static String ROOT_URL ="http://1.119.1.2";//接口地址
    private static int DISPATCH_PORT=40004;//任务的端口号
    private static int UDP_PORT=40004;//Udp请求端口号
    public static String token;
    public static String SystemVersion;
    public static String ClientVersionName;
    public static String deviceId;

    public static int ScreenWidth;
    public static int DesignScreenWidth=750;



    public static String getRootUrl() {
        return ROOT_URL;
    }

    public static void setRootUrl(String rootUrl) {
        ROOT_URL = rootUrl;
    }

    public static int getDispatchPort() {
        return DISPATCH_PORT;
    }

    public static void setDispatchPort(int dispatchPort) {
        DISPATCH_PORT = dispatchPort;
    }

    public static void setUdpPort(int udpPort) {
        UDP_PORT = udpPort;
    }

    /**
     * 获取派遣root url
     * @return
     */
    public static String getDispatchRootUrl(){
        return ROOT_URL +":"+DISPATCH_PORT;
    }

    /**
     * 获取上传地址坐标的root url
     * @return
     */
    public static String getUdpRootUrl(){
        return ROOT_URL;
    }

    /**
     * 获取上传地址坐标的root url
     * @return
     */
    public static int getUdpPort(){
        return UDP_PORT;
    }

}
