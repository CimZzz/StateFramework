package com.virtualightning.stateframeworkdemo.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.constant.RequestMethod;
import com.virtualightning.stateframework.http.HTTPClient;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;
import com.virtualightning.stateframeworkdemo.request.TestRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
@BindHTTPRequest(
        url = "http://localhost/{$}/{$}",
        method = RequestMethod.GET
)
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
        File file = new File(Environment.getExternalStorageDirectory(),"test.txt");
        if(file.exists())
            file.delete();
        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("HelloWorld");
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TestRequest testRequest = new TestRequest();
        HTTPClient client = new HTTPClient.Builder().build();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n");

        int size = 10;
        for(Map.Entry<String,Charset> entry: Charset.availableCharsets().entrySet()) {
            if(size == 0) {
                Log.e("EEFFSS",stringBuilder.toString());
                stringBuilder = new StringBuilder();
                size = 10;
            }
            stringBuilder.append("/**\n").append(" * ").append(entry.getKey().toUpperCase()).append(" 编码\n").append(" */\n");
            stringBuilder.append(entry.getKey().toUpperCase().replace('-','_')).append("(\"").append(entry.getValue().displayName()).append("\"),\n\n\n");
            size--;
        }
        Log.e("EEFFSS",stringBuilder.toString());

    }

    @BindObserver(
            stateId = "1",
            isVarParameters = false
    )
    public void fun(Integer i, Double d) {
        Toast.makeText(this, String.valueOf(i + d), Toast.LENGTH_SHORT).show();
    }
}
