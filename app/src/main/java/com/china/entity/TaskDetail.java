package com.china.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/15.
 */
public class TaskDetail implements Serializable {
    public String distance; //距离(米)
    public String vehicleCode; //车牌号码
    public String driverName; //司机姓名
    public String startTime; //开始时间
    public String endTime; //结束时间
    public String timeCost; //总共用时
    public String warehouseName; //仓库信息
}
