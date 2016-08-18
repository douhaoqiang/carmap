package com.china.net;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.china.GlobalConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	private  Context context;
	public UDPClient(Context context){
		this.context=context;

	}


	public void sendLocationMsg(final Location location , final String deviceId){
		new Thread(){
			@Override
			public void run() {
				sendMsg(location,deviceId);
			}
		}.start();

	}

	private void sendMsg(Location location ,String deviceId) {
		DatagramSocket client = null;
		try {
			client = new DatagramSocket();
			String gpsInfo = new StringBuilder().append(location.getLongitude()).append("|")
					.append(location.getLatitude()).append("|")
					.append(location.getTime()).append("|")
					.append(deviceId).append("|")
					.append(location.getAltitude()).toString();
			//数据格式
//			经度|纬度|定位时间 getTime|设备id|海拔高度
//			116.123456|36.123456|1461555366104|123456789|123.1234564
			byte[] sendBuf = gpsInfo.getBytes();

			InetAddress addr = InetAddress.getByName(GlobalConstants.getUdpRootUrl());
			int port = GlobalConstants.getUdpPort();
//			InetAddress addr = InetAddress.getByName("192.168.203.100");
//			int port = 60000;
			// int port = 20160;
			DatagramPacket sendPacket = new DatagramPacket(sendBuf,
                    sendBuf.length, addr, port);
			client.send(sendPacket);

			byte[] recvBuf = new byte[100];
			DatagramPacket recvPacket = new DatagramPacket(recvBuf,
                    recvBuf.length);
			client.receive(recvPacket);
			String recvStr = new String(recvPacket.getData(), 0,
                    recvPacket.getLength());
			System.out.println("收到:" + recvStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(client!=null){
                    client.close();
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
