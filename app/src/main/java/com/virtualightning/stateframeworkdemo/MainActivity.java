package com.virtualightning.stateframeworkdemo;

import android.app.Activity;
import android.os.Bundle;

import com.virtualightning.stateframework.anno.BindObserver;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
public class MainActivity extends Activity{

    @BindObserver(
            stateId = "2"
    )
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
