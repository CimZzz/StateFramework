package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualightning.stateframework.anno.BindResources;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnItemClick;
import com.virtualightning.stateframework.constant.ResType;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class StateMainActivity extends Activity {
    @BindResources(resId = R.array.stateItem,type = ResType.STRING_ARRAY)
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
                startActivity(new Intent(this,BindViewEventResActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,BindStateObserverActivity.class));
                break;
            case 2:
                startActivity(new Intent(this,AdvanceActivity.class));
                break;
            case 3:
                startActivity(new Intent(this,A1.class));
                break;
        }
    }
}
