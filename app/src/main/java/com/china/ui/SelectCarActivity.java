package com.china.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.china.GlobalConstants;
import com.china.InitActivity;
import com.china.MySpinner.MySpinerPop;
import com.china.MySpinner.MySpinnerAdapter;
import com.china.MySpinner.ViewHolder;
import com.china.R;
import com.china.entity.Vehicle;
import com.china.entity.Driver;
import com.china.entity.Wareroom;
import com.china.entity.response.CarAndDriverResponse;
import com.china.entity.response.SaveCarMsgResponse;
import com.china.entity.response.WareroomResponse;
import com.china.net.BaseProtocol;
import com.china.net.DispatchApi;
import com.china.util.HeaderUtils;
import com.china.util.ToastUtil;
import com.china.view.WeiXinLoadingDialog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/6.
 */
public class SelectCarActivity extends Activity implements View.OnClickListener {

    private AQuery aq;
    private ArrayList<Wareroom> warerooms;
    private ArrayList<Vehicle> cars;
    private ArrayList<Driver> drivers;
    private View wareRoomLay, carLay, driverLay;
    private Button start_btn;
    private Wareroom selectWareroom;
    private Vehicle selectCar;
    private Driver selectDriver;
    private WeiXinLoadingDialog loadingDialog;
    private long firstTime;
    private int indexNumber;
    private SharedPreferences sp;
    private TextView headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_car_lay);
        sp = getSharedPreferences(GlobalConstants.PRE_NAME, Context.MODE_PRIVATE);
        aq = new AQuery(this);
        loadingDialog = new WeiXinLoadingDialog(this);
        initView();
        getWareroomData();
    }


    /**
     * c初始化界面
     */
    private void initView() {
        new HeaderUtils(aq, R.string.car_select_car);
        wareRoomLay = findViewById(R.id.select_wearroom_lay);
        carLay = findViewById(R.id.select_car_lay);
        driverLay = findViewById(R.id.select_driver_lay);
        start_btn = aq.id(R.id.select_start_btn).getButton();
        headerTitle = aq.id(R.id.header_title).getTextView();
        wareRoomLay.setOnClickListener(this);
        carLay.setOnClickListener(this);
        driverLay.setOnClickListener(this);
        start_btn.setOnClickListener(this);
        headerTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_wearroom_lay:
                showWareroomList(view);
                break;
            case R.id.select_car_lay:
                showCarList(view);
                break;
            case R.id.select_driver_lay:
                showDriverList(view);
                break;
            case R.id.select_start_btn:
                saveSelectMsg();
                break;
            case R.id.header_title:
                updateIpAddress();
                break;

        }
    }

    //显示空余车场
    public void showWareroomList(View view) {

        if (warerooms == null || warerooms.size() <= 0) {
            //没有可用车辆
            ToastUtil.show(this, "没有可用仓库！");
            return;
        }

        MySpinnerAdapter<Wareroom> adapter = new MySpinnerAdapter(this, warerooms, R.layout.popup_list_item, new MySpinnerAdapter.SpinnerCallBack<Wareroom>() {
            @Override
            public void drawChild(ViewHolder holder, Wareroom child, int position) {
                holder.setTextViewText(R.id.pop_list_item_name, child.name);
            }
        });

        MySpinerPop<Wareroom> spaceSpiner = new MySpinerPop<>(this, view, adapter, new MySpinerPop.ItemOnClickCallback<Wareroom>() {
            @Override
            public void onItemClick(Wareroom child, int position) {
                //选择停车场
                setWareroom(child);
            }
        });
        spaceSpiner.showAsDropDown(view);

    }

    //显示下拉车辆
    public void showCarList(View view) {

        if (selectWareroom == null) {
            ToastUtil.show(this, "请先选择仓库！");
            return;
        }

        if (cars == null || cars.size() <= 0) {
            //没有可用车辆
            ToastUtil.show(this, "没有可用车辆！");
            return;
        }

        MySpinnerAdapter<Vehicle> adapter = new MySpinnerAdapter(this, cars, R.layout.popup_list_item, new MySpinnerAdapter.SpinnerCallBack<Vehicle>() {
            @Override
            public void drawChild(ViewHolder holder, Vehicle child, int position) {
                holder.setTextViewText(R.id.pop_list_item_name, child.code);
            }
        });

        MySpinerPop<Vehicle> spaceSpiner = new MySpinerPop<>(this, adapter, new MySpinerPop.ItemOnClickCallback<Vehicle>() {
            @Override
            public void onItemClick(Vehicle child, int position) {
                //选择停车场
                setCar(child);
            }
        });
        spaceSpiner.showAsDropDown(view);

    }


    //显示下拉司机
    public void showDriverList(View view) {

        if (selectWareroom == null) {
            ToastUtil.show(this, "请先选择仓库！");
            return;
        }

        if (drivers == null || drivers.size() <= 0) {
            //没有可用车辆
            ToastUtil.show(this, "没有可用司机！");
            return;
        }

        MySpinnerAdapter<Driver> adapter = new MySpinnerAdapter(this, drivers, R.layout.popup_list_item, new MySpinnerAdapter.SpinnerCallBack<Driver>() {
            @Override
            public void drawChild(ViewHolder holder, Driver child, int position) {
                holder.setTextViewText(R.id.pop_list_item_name, child.name);
            }
        });

        MySpinerPop<Driver> spaceSpiner = new MySpinerPop<>(this, adapter, new MySpinerPop.ItemOnClickCallback<Driver>() {
            @Override
            public void onItemClick(Driver child, int position) {
                //选择停车场
                setDriver(child);
            }
        });
        spaceSpiner.showAsDropDown(view);

    }

    /**
     * 设置选择的仓库
     *
     * @param wareroom
     */
    private void setWareroom(Wareroom wareroom) {
        selectWareroom = wareroom;
        aq.id(R.id.select_wearroom_tv).text(wareroom.name);
        aq.id(R.id.select_city_tv).text(wareroom.city);
        getCarsAndDriverData();
    }

    /**
     * 设置选择的车辆
     *
     * @param car
     */
    private void setCar(Vehicle car) {
        selectCar = car;
        aq.id(R.id.select_car_tv).text(car.code);
    }

    /**
     * 设置选择的司机
     *
     * @param driver
     */
    private void setDriver(Driver driver) {
        selectDriver = driver;
        aq.id(R.id.select_driver_tv).text(driver.name);
    }


    /**
     * 打开车辆轨迹界面
     */
    private void showCarPath(String taskId) {

        Intent intent = new Intent(this, CarPathActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("canBack", true);
        startActivity(intent);

    }

    /**
     * 获取仓库列表数据
     */
    private void getWareroomData() {
        loadingDialog.show();
        BaseProtocol<WareroomResponse> request = DispatchApi.getWarerooms();
        request.callback(new AjaxCallback<WareroomResponse>() {
            @Override
            public void callback(String url, WareroomResponse result, AjaxStatus status) {
                loadingDialog.cancel();
                if (status.getCode() != 200) {
                    ToastUtil.show(SelectCarActivity.this, "网络请求失败！");
                    return;
                }
                if (result != null) {
                    if ("0000".equals(result.code)) {//表示获取成功数据成功
                        warerooms = result.body.warehouses;
                    } else {
                        ToastUtil.show(SelectCarActivity.this, result.message);
                    }
                } else {
                    ToastUtil.show(SelectCarActivity.this, "获取数据失败！");
                }
            }
        });
        request.execute(aq, -1);

    }


    /**
     * 获取车辆和司机列表
     */
    private void getCarsAndDriverData() {
        loadingDialog.show();
        BaseProtocol<CarAndDriverResponse> request = DispatchApi.getCarsAndDrivers(selectWareroom.wid);
        request.callback(new AjaxCallback<CarAndDriverResponse>() {
            @Override
            public void callback(String url, CarAndDriverResponse result, AjaxStatus status) {
                loadingDialog.cancel();
                if (status.getCode() != 200) {
                    ToastUtil.show(SelectCarActivity.this, "网络请求失败！");
                    return;
                }
                if (result != null) {
                    if ("0000".equals(result.code)) {//表示获取成功数据成功
                        cars = result.body.vehicles;
                        drivers = result.body.drivers;
                    } else {
                        ToastUtil.show(SelectCarActivity.this, result.message);
                    }
                } else {
                    ToastUtil.show(SelectCarActivity.this, "获取数据失败！");
                }
            }
        });
        request.execute(aq, -1);
    }

    /**
     * 获取车辆和司机列表
     */
    private void saveSelectMsg() {

        if (selectWareroom == null || selectDriver == null || selectCar == null) {
            ToastUtil.show(this, "请先完善信息！");
            return;
        }

        loadingDialog.show();
        BaseProtocol<SaveCarMsgResponse> request = DispatchApi.saveCarMsg(
                selectWareroom.wid, selectCar.vid, selectDriver.did, selectWareroom.city, GlobalConstants.deviceId);
        request.callback(new AjaxCallback<SaveCarMsgResponse>() {
            @Override
            public void callback(String url, SaveCarMsgResponse result, AjaxStatus status) {
                loadingDialog.cancel();
                if (status.getCode() != 200) {
                    ToastUtil.show(SelectCarActivity.this, "网络请求失败！");
                    return;
                }
                if (result != null) {
                    if ("0000".equals(result.code)) {//表示获取成功数据成功
                        showCarPath(result.body.id);
                    } else if ("9001".equals(result.code)) {
                        //表示有未完成的订单id，接着做任务
                        showCarPath(result.body.id);
                    } else if ("9999".equals(result.code)) {
                        ToastUtil.show(SelectCarActivity.this, result.message);
                    }
                } else {
                    ToastUtil.show(SelectCarActivity.this, "获取数据失败！");
                }
            }
        });
        request.execute(aq, -1);
    }


    private void updateIpAddress() {
        long sencondTime = System.currentTimeMillis();
        if (sencondTime - firstTime < 2000) {
            indexNumber += 1;
        } else {
            indexNumber = 0;
        }
        if (indexNumber == 3) {
            indexNumber = 0;
            LinearLayout linear = new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);
            final TextView tv = new TextView(this);
            tv.setText("ip地址：");
            linear.addView(tv);
            final EditText et = new EditText(this);
            et.setText(GlobalConstants.getRootUrl());
            linear.addView(et);
            final TextView tv2 = new TextView(this);
            tv2.setText("任务接口端口：");
            linear.addView(tv2);
            final EditText et2 = new EditText(this);
            et2.setText(GlobalConstants.getDispatchPort()+"");
            linear.addView(et2);
            final TextView tv3 = new TextView(this);
            tv3.setText("UDP接口端口：");
            linear.addView(tv3);
            final EditText et3 = new EditText(this);
            et3.setText(GlobalConstants.getUdpPort()+"");
            linear.addView(et3);
            new AlertDialog.Builder(this)
                    .setTitle("请输入ip")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(linear)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    SharedPreferences.Editor ets = sp.edit();
                                    if(!TextUtils.isEmpty(et.getText().toString())){
                                        GlobalConstants.setRootUrl(et.getText().toString());
                                        ets.putString("ip", et.getText().toString());
                                    }
                                    if(!TextUtils.isEmpty(et2.getText().toString())){
                                        GlobalConstants.setDispatchPort(Integer.valueOf(et2.getText().toString()));
                                        ets.putString("port1", et2.getText().toString());
                                    }
                                    if(!TextUtils.isEmpty(et3.getText().toString())){
                                        GlobalConstants.setDispatchPort(Integer.valueOf(et3.getText().toString()));
                                        ets.putString("port2", et3.getText().toString());
                                    }

                                    ets.commit();
                                    InitActivity.exit(SelectCarActivity.this);
                                }
                            }).setNegativeButton("取消", null).show();
        }
        firstTime = sencondTime;
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
}
