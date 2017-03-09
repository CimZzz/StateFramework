package com.virtualightning.stateframeworkdemo.state;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.virtualightning.stateframework.anno.BindResources;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnClick;
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
public class BindViewEventResActivity extends Activity {
    /**
     * 根据视图ID绑定视图
     */

    @BindView(R.id.state1_tv)
    TextView tv;

    @BindView(R.id.state1_et)
    EditText et;

    @BindView(R.id.state1_list)
    ListView list;

    /**
     * 根据资源ID绑定资源
     */
    @BindResources(resId = R.array.state1,type = ResType.STRING_ARRAY)
    String[] strArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置布局ID*/
        setContentView(R.layout.state_1);

        //解析并绑定视图
        //Analyzer.analyzeView(Activity);
        //Analyzer.analyzeView(Object,View);View为根视图

        //解析并绑定资源
        //Analyzer.analyzeResources(Activity);
        //Analyzer.analyzeResources(Object,Context);
        //Analyzer.analyzeResources(Object,Resources);

        //解析并绑定事件
        //Analyzer.analyzeEvent(Activity);
        //Analyzer.analyzeEvent(Object,View);View为根视图

        //解析全部注解
        //Analyzer.analyzeAll(Activity,StateRecord);
        Analyzer.analyzeAll(this,null);


        //设置列表的适配器
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strArray));
    }

    /**
     * 注册点击事件,可选参数为 View
     */

    @OnClick(R.id.state1_btn)
    void onChangeBtnClick() {
        tv.setText(et.getText().toString());
    }


    @OnClick(R.id.state1_btn2)
    void onClearBtnClick(View view) {
        tv.setText("");
    }

   /**
    * 注册列表项点击事件,可选参数为 AdapterView , View , int , long
    * <p>
    * 参数参考 {@link android.widget.AdapterView.OnItemClickListener}
    */
    @OnItemClick(R.id.state1_list)
    void onListItemClick(AdapterView adapterView,int position) {
        tv.setText((String)adapterView.getItemAtPosition(position));
    }
}
