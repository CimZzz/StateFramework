package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualightning.stateframework.anno.state.BindObserver;
import com.virtualightning.stateframework.anno.bind.BindView;
import com.virtualightning.stateframework.anno.event.OnClick;
import com.virtualightning.stateframework.constant.RunType;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;


/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class BindStateObserverActivity extends Activity {
    static final String STATE_1 = "S_1";
    static final String STATE_2 = "S_2";
    static final String STATE_3 = "S_3";

    @BindView(R.id.state2_tv)
    TextView tv;

    StateRecord stateRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_2);
        stateRecord = StateRecord.newInstance(getClass());

        //绑定状态观察者至状态记录中
        //Analyzer.analyzeState(Object,StateRecord);

        Analyzer.analyzeAll(this,stateRecord);

        onChangeStateClick();
    }

    @OnClick(R.id.state2_btn1)
    void onSendState_1() {
        stateRecord.notifyState(STATE_1,"1");
    }

    @OnClick(R.id.state2_btn2)
    void onSendState_2() {
        stateRecord.notifyState(STATE_2,"2");
    }

    @OnClick(R.id.state2_btn3)
    void onSendState_3() {
        stateRecord.notifyState(STATE_3,"3");
    }

    @OnClick(R.id.state2_btn4)
    void onChangeStateClick() {
        if(stateRecord.isStop()) {
            tv.setText("运行中");
            stateRecord.setRecordState(true);
        } else {
            tv.setText("停止中");
            stateRecord.setRecordState(false);
        }
    }

    /**
     * 绑定状态观察者
     * <p>
     * 具体绑定参数参考 {@link BindObserver}
     */

    @BindObserver(
            stateId = STATE_1,//注册状态ID
            allowStop = true,//是否允许停止
            runType = RunType.MAIN_LOOP,//运行类型,当前为运行在主线程
            isVarParameters = true//是否使用自变长参数
    )
    void onNotifyState_1(Object... args) {
        Toast.makeText(this,(String)args[0],Toast.LENGTH_SHORT).show();
    }

    @BindObserver(
            stateId = STATE_2,//注册状态ID
            allowStop = false,//是否允许停止
            runType = RunType.MAIN_LOOP,//运行类型,当前为运行在主线程
            isVarParameters = true//是否使用自变长参数
    )
    void onNotifyState_2(Object... args) {
        Toast.makeText(this,(String)args[0],Toast.LENGTH_SHORT).show();
    }

    @BindObserver(
            stateId = STATE_3,//注册状态ID
            allowStop = false,//是否允许停止
            runType = RunType.MAIN_LOOP,//运行类型,当前为运行在主线程
            isVarParameters = false//是否使用自变长参数,当前禁止使用自变长参数,发送状态必须严格符合观察者参数类型与长度,否则会发生异常
    )
    void onNotifyState_3(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
