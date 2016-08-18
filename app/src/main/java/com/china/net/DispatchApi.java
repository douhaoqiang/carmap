package com.china.net;

import android.text.TextUtils;
import android.util.Log;

import com.androidquery.callback.AjaxStatus;
import com.china.GlobalConstants;
import com.china.entity.response.CarAndDriverResponse;
import com.china.entity.response.InitResponse;
import com.china.entity.response.SaveCarMsgResponse;
import com.china.entity.response.TaskDetailResponse;
import com.china.entity.response.WareroomResponse;
import com.google.gson.GsonBuilder;

//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;

import java.lang.ref.WeakReference;

/**
 * DESC 调度任务接口
 *
 * @auther 窦浩强 827809500@qq.com  2015/8/21 0021.
 */
public class DispatchApi<T> extends BaseProtocol<T> {
	private static final String TAG = DispatchApi.class.getSimpleName();

	public DispatchApi(Class<T> type, int... option) {
		super(type, option);

		// 协议固定参数
		if (!TextUtils.isEmpty(GlobalConstants.token)){
			headers("token", GlobalConstants.token);
		}

		this.headers("osName", "android")
				.headers("osVersion", GlobalConstants.SystemVersion)
				.headers("appVersion", GlobalConstants.ClientVersionName);
	}

	@Override
	protected String getRootUrl() {
		return GlobalConstants.DISPATCH_URL;
	}

	@Override
	public BaseProtocol<T> url(String url) {
		return super.url(url);
	}

	private WeakReference<TransformCallback> transformCallback;

	public static interface TransformCallback {
		public void transformCallBack(Object o);
	}

	public void setTransformCallback(TransformCallback callback) {
		transformCallback = new WeakReference<TransformCallback>(callback);
	}

	private static GsonBuilder gsonBuilder = new GsonBuilder()
			.setDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("unchecked")
	@Override
	public <T> T transform(String url, Class<T> type, String encoding,
			byte[] data, AjaxStatus status) {

		TransformCallback callbacks = transformCallback != null ? transformCallback
				.get() : null;

		Log.d(TAG, "url=" + url + " status=" + status.getCode());
		Log.i(TAG, "[prot " + type + "] return: " + new String(data));

		Object o = super.transform(url, type, encoding, data, status);
//		if(o instanceof Response){
//			if(((Response) o).code.equals("1001")) {
//				if( o instanceof LoginResponse || o instanceof PutLocResponse){
//
//				}else {
//					EventBus.getDefault().postSticky(new NeedReauthEvent());
//				}
//			}
//		}
		return (T) o;

	}

	/**
	 * init初始化接口
	 * @return
	 */
	public static BaseProtocol<InitResponse> init(String deviceId) {
		BaseProtocol<InitResponse> protocol = new DispatchApi(
				InitResponse.class).method(METHOD_POST).url(
				"/config/init").postJsonMap("deviceId",deviceId);

		return protocol;
	}


	/**
	 * 获取仓库列表
	 * @return
	 */
	public static BaseProtocol<WareroomResponse> getWarerooms() {
		BaseProtocol<WareroomResponse> protocol = new DispatchApi(
				WareroomResponse.class).method(METHOD_POST).url(
				"/config/warehouses");

		return protocol;
	}

	/**
	 * 获取车辆和司机列表
	 * @param wareroomWid
	 * @return
     */
	public static BaseProtocol<CarAndDriverResponse> getCarsAndDrivers(String wareroomWid) {
		BaseProtocol<CarAndDriverResponse> protocol = new DispatchApi(
				CarAndDriverResponse.class).method(METHOD_POST).url(
				"/config/getDriversAndVehicles").postJsonMap("id",wareroomWid);

		return protocol;
	}


	/**
	 * 保存配置信息并开始记录gps
	 * @param wareroomWid
	 * @return
     */
	public static BaseProtocol<SaveCarMsgResponse> saveCarMsg(String wareroomWid, String vehicleId, String driverId, String city, String deviceId) {

		BaseProtocol<SaveCarMsgResponse> protocol = new DispatchApi(
				SaveCarMsgResponse.class).method(METHOD_POST).url(
				"/saveDeliveryTask/create").postJsonMap(
				"warehouseId",wareroomWid,
				"vehicleId",vehicleId,
				"driverId",driverId,
				"city",city,
				"deviceId",deviceId);

		return protocol;
	}


	/**
	 * 到达目的地结束任务
	 * @param taskId 任务Id
	 * @param deviceId 设备Id
     * @return
     */
	public static BaseProtocol<TaskDetailResponse> finishCarTask(String taskId, String deviceId) {

		BaseProtocol<TaskDetailResponse> protocol = new DispatchApi(
				TaskDetailResponse.class).method(METHOD_POST).url(
				"/saveDeliveryTask/end").postJsonMap(
				"id",taskId,
				"deviceId",deviceId);

		return protocol;
	}


}
