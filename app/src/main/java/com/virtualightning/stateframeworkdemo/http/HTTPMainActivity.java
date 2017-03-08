package com.virtualightning.stateframeworkdemo.http;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualightning.stateframework.anno.BindResources;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnItemClick;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class HTTPMainActivity extends Activity {
    @BindResources(R.array.httpItem)
    String[] strArray;

    @BindView(R.id.list)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Analyzer.analyzeAll(this,null);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strArray));
    }

    @OnItemClick(R.id.list)
    void onItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this,GetActivity.class));
                break;
        }
    }
}
