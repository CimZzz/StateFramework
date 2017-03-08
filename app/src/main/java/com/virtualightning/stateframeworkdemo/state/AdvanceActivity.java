package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindResources;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnClick;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;

import java.util.Random;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class AdvanceActivity extends Activity {
    static final String STATE_1 = "S_1";

    @BindView(R.id.state3_tv)
    TextView tv;

    @BindResources(R.array.state1)
    String[] strArray;

    StateRecord stateRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_3);

        stateRecord = new StateRecord();

        Analyzer.analyzeAll(this,stateRecord);
    }

    @OnClick(R.id.state3_btn)
    void onSendRequestClick() {
        //启用线程模拟发送异步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //线程睡眠5秒模拟耗时操作
                    Thread.sleep(5000);
                    //操作完成发送响应至状态观察者
                    Random random = new Random();
                    stateRecord.notifyState(STATE_1,strArray[random.nextInt(strArray.length)]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @BindObserver(
            stateId = STATE_1,
            isVarParameters = false
    )
    void onReceiverResponse(String response) {
        tv.setText(response);
        Toast.makeText(this,"接收到响应报文",Toast.LENGTH_SHORT).show();
    }
}
