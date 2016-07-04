package com.china.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.china.GlobalConstants;
import com.china.R;
import com.china.entity.response.InitResponse;
import com.china.net.BaseProtocol;
import com.china.net.DispatchApi;
import com.china.util.ToastUtil;

/**
 * Created by Administrator on 2016/5/12.
 */
public class SplashActivity extends Activity {

    private AQuery aq;
    private boolean bTimeout=false;//表示是否等待了固定的时间
    private boolean isHaveTask=false;//表示是否有没有完成的任务
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_lay);
        aq=new AQuery(this);
        initData();
        AQUtility.postDelayed(new Runnable() {

            @Override
            public void run() {
                bTimeout = true;
                tryStartActivity();
            }
        }, 3000);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        BaseProtocol<InitResponse> request = DispatchApi.init(GlobalConstants.deviceId);
        request.callback(new AjaxCallback<InitResponse>() {
            @Override
            public void callback(String url, InitResponse result, AjaxStatus status) {
                if(status.getCode()!=200){
                    ToastUtil.show(SplashActivity.this,"网络请求失败！");
                    return;
                }
                if (result != null) {
                    if ("0000".equals(result.code)) {//表示获取成功数据成功

                    } else if("9004".equals(result.code)) {
                        isHaveTask=true;
                        taskId=result.body.id;
                    }
                    tryStartActivity();
                }else{
                    ToastUtil.show(SplashActivity.this,"获取数据失败！");
                }
            }
        });
        request.execute(aq, -1);

    }


    private void tryStartActivity(){
        if(!bTimeout){
            return;
        }

        if(isHaveTask){
            Intent intent = new Intent(SplashActivity.this, CarPathActivity.class);
            intent.putExtra("taskId",taskId);
            intent.putExtra("canBack",false);
            startActivity(intent);
            this.finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, SelectCarActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

}
