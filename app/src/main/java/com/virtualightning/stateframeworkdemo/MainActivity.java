package com.virtualightning.stateframeworkdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.core.StateRecord;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
public class MainActivity extends Activity{

    @BindView(android.R.id.accessibilityActionContextClick)
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        final StateRecord stateRecord = new StateRecord();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateRecord.notifyState("3",1,0.2);
            }
        });
        view.setBackgroundColor(Color.BLUE);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(view);
        stateRecord.registerByAnnotation(this);
    }

    @BindObserver(
            stateId = "3",
            isVarParameters = false
    )
    public void fun(Integer i,Double d) {
        Toast.makeText(this,String.valueOf(i + d),Toast.LENGTH_SHORT).show();
    }
}
