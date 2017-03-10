package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.virtualightning.stateframework.anno.bind.BindView;
import com.virtualightning.stateframework.anno.event.OnClick;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;

/**
 * Created by CimZzz on ${Date}.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.3<br>
 * Description:<br>
 * Description
 */
public class StaticObserverActivity2 extends Activity {
    @BindView(R.id.state4_et)
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_4_2);

        Analyzer.analyzeAll(this,null);
    }

    /**
     * 点击按钮将唤醒全局状态,EditText 中的文本作为唤醒代价
     */
    @OnClick(R.id.state4_btn2)
    void onNotifyClick() {
        /*唤醒全局状态不需要状态记录实例*/
        StateRecord.notifyWholeState(StaticObserverActivity.GLOBAL_STATE_0,et.getText().toString());
    }
}
