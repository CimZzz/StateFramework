package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class A1 extends Activity {
    @BindView(R.id.a)
    TextView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);
        StateRecord stateRecord = StateRecord.newInstance(getClass());
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
        startActivity(new Intent(this,A2.class));
    }
}
