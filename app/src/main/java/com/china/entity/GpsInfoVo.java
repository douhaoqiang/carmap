package com.china.entity;

import java.io.Serializable;


/**
 * gpsInfoVo 只包含经纬度信息
 * @author yangyang
 * 2016年4月27日
 */
public class GpsInfoVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1103995112434238914L;

	/**
	 * 经度(火星坐标系)
	 */
	
	private final Double longitude;

	/**
	 * 纬度(火星坐标系)
	 */
	private final Double latitude;

	public GpsInfoVo(Double longitude, Double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

}
