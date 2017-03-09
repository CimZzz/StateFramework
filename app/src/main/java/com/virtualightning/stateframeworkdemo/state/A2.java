package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnClick;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class A2 extends Activity {
    @BindView(R.id.a)
    TextView a;

    StateRecord stateRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);
        stateRecord = StateRecord.newInstance(getClass());
        Analyzer.analyzeAll(this,stateRecord);
    }

    @BindObserver(
            stateId = "1",
            isVarParameters = false,
            isWholeObserver = true
    )
    void onTest(String s) {
        a.setText(s);
    }

    @OnClick(R.id.a)
    void onClick() {
        StateRecord.notifyWholeState("1","哈哈");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("HJSD","12312313123");
        stateRecord.unregisterObserver();
    }
}
