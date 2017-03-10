package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.virtualightning.stateframework.anno.state.BindObserver;
import com.virtualightning.stateframework.anno.bind.BindView;
import com.virtualightning.stateframework.anno.event.OnClick;
import com.virtualightning.stateframework.constant.RunType;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.MainActivity;
import com.virtualightning.stateframeworkdemo.R;

import java.lang.ref.WeakReference;

/**
 * Created by CimZzz(王彦雄) on 3/9/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.3<br>
 * Description:<br>
 */
public class StaticObserverActivity extends Activity {
    static final String GLOBAL_STATE_0 = "GS_0";

    @BindView(R.id.state4_tv)
    TextView tv;

    StateRecord stateRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_4);

        MainActivity.testAcWeakReference = new WeakReference<Activity>(this);
        stateRecord = StateRecord.newInstance(getClass());
        Analyzer.analyzeAll(this,stateRecord);
    }

    @OnClick(R.id.state4_btn1)
    void onBtnClick() {
        startActivity(new Intent(this,StaticObserverActivity2.class));
    }

    @BindObserver(
            stateId = StaticObserverActivity.GLOBAL_STATE_0,
            runType = RunType.MAIN_LOOP,
            isVarParameters = false,
            isWholeObserver = true//注册全局状态
    )
    void onWholeStateNotify(String string) {
        tv.setText(string);
    }

    /**
     * 销毁时注销状态记录释放内存
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*注意:如果注册了全局状态观察者,此处必须注销,否则可能会造成内存的泄露(不一定)*/
        /*建议:无论注册了何种状态观察者,都建议在不用时进行注销,这是一种良好的代码习惯,你会变得更加Professional!*/
        stateRecord.unregisterObserver();
    }
}
