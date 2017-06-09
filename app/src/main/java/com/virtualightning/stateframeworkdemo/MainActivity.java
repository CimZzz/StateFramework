package com.virtualightning.stateframeworkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualightning.stateframework.anno.bind.BindResources;
import com.virtualightning.stateframework.anno.bind.BindView;
import com.virtualightning.stateframework.anno.event.OnItemClick;
import com.virtualightning.stateframework.constant.ResType;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.http.HTTPMainActivity;
import com.virtualightning.stateframeworkdemo.state.StateMainActivity;

import java.lang.ref.WeakReference;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
public class MainActivity extends Activity {
    @BindResources(resId = R.array.mainItem,type = ResType.STRING_ARRAY)
    String[] strArray;

    @BindView(R.id.list)
    ListView list;

    public static WeakReference<Activity> testAcWeakReference = new WeakReference<>(null);

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
                startActivity(new Intent(this,StateMainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,HTTPMainActivity.class));
                break;
            case 2:
                Log.d("123123","" + testAcWeakReference.get());
                break;
        }
    }

    static class Demo {
        @BindResources(resId = R.array.mainItem,type = ResType.STRING_ARRAY)
        String[] strArray;
    }
}
