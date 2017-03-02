package com.virtualightning.stateframeworkdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.core.Analyzer;
import com.virtualightning.stateframework.core.StateRecord;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
public class MainActivity extends Activity {


    @BindView(R.id.m)
    LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final StateRecord stateRecord = new StateRecord();
        setContentView(R.layout.main);

        Analyzer.analyzeState(stateRecord,this);
        Analyzer.analyzeView(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateRecord.notifyState("3", 1, 0.2);
            }
        });
        view.setBackgroundColor(Color.BLUE);
    }

    @BindObserver(
            stateId = "3",
            isVarParameters = false
    )
    public void fun(Integer i, Double d) {
        Toast.makeText(this, String.valueOf(i + d), Toast.LENGTH_SHORT).show();
    }
}
