package com.virtualightning.stateframeworkdemo.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
public class MainActivity extends Activity {
    private String strArray[];

    @BindView(R.id.list)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        Analyzer.analyzeView(this);
        list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,strArray));
    }
}
