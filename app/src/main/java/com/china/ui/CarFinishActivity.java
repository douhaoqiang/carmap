package com.china.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.china.GlobalConstants;
import com.china.R;
import com.china.entity.TaskDetail;
import com.china.entity.response.TaskDetailResponse;
import com.china.net.BaseProtocol;
import com.china.net.DispatchApi;
import com.china.util.HeaderUtils;
import com.china.util.ToastUtil;
import com.china.view.WeiXinLoadingDialog;

/**
 * Created by Administrator on 2016/5/7.
 */
public class CarFinishActivity extends Activity {

    private AQuery aq;
    private WeiXinLoadingDialog loadingDialog;
    private TaskDetail mTaskDetail;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_finish_lay);
        aq=new AQuery(this);
        taskId=getIntent().getStringExtra("taskId");
        initView();
        getData();

    }

    /**
     * 初始化数据
     */
    private void initView(){
        loadingDialog=new WeiXinLoadingDialog(this);
        new HeaderUtils(aq,R.string.car_path_msg) ;
        aq.id(R.id.back_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回到选择车辆页面
                Intent intent=new Intent(CarFinishActivity.this,SelectCarActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取车辆 行驶数据
     */
    private void getData(){
        loadingDialog.show();
        BaseProtocol<TaskDetailResponse> request = DispatchApi.finishCarTask(taskId, GlobalConstants.deviceId);
        request.callback(new AjaxCallback<TaskDetailResponse>() {
            @Override
            public void callback(String url, TaskDetailResponse result, AjaxStatus status) {
                loadingDialog.cancel();
                if(status.getCode()!=200){
                    ToastUtil.show(CarFinishActivity.this,"网络请求失败！");
                    return;
                }
                if (result != null) {
                    if ("0000".equals(result.code)) {//表示获取成功数据成功
                        mTaskDetail =result.body;
                        showTaskDetail();
                    }else{
                        ToastUtil.show(CarFinishActivity.this,result.message);
                    }
                }else{
                    ToastUtil.show(CarFinishActivity.this,"获取数据失败！");
                }
            }
        });
        request.execute(aq, -1);
    }


    /**
     * 展示任务详情信息
     */
    private void showTaskDetail(){
        if(mTaskDetail==null){
            return;
        }
        aq.id(R.id.finish_car_code_tv).text(mTaskDetail.vehicleCode);
        aq.id(R.id.finish_driver_name_tv).text(mTaskDetail.driverName);
        aq.id(R.id.finish_wearroom_name_tv).text(mTaskDetail.warehouseName);
        aq.id(R.id.finish_begin_time_tv).text(mTaskDetail.startTime);
        aq.id(R.id.finish_end_time_tv).text(mTaskDetail.endTime);
        aq.id(R.id.finish_time_count_tv).text(mTaskDetail.timeCost);
        aq.id(R.id.finish_distance_tv).text(mTaskDetail.distance);
    }


}
