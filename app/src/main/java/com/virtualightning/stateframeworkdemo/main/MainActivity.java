package com.virtualightning.stateframeworkdemo.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.VLHeader;
import com.virtualightning.stateframework.anno.VLUrlParams;
import com.virtualightning.stateframework.constant.HTTPMethodType;
import com.virtualightning.stateframework.http.HTTPClient;
import com.virtualightning.stateframework.http.IHTTPCallback;
import com.virtualightning.stateframework.http.MultiFile;
import com.virtualightning.stateframework.http.NamePair;
import com.virtualightning.stateframework.http.Request;
import com.virtualightning.stateframework.http.Response;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframework.state.StateRecord;
import com.virtualightning.stateframeworkdemo.R;
import com.virtualightning.stateframeworkdemo.request.TestRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by CimZzz on 2/28/17.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * Description
 */
@BindHTTPRequest(
        url = "http://localhost/{$}/{$}",
        method = HTTPMethodType.GET
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
        client.enqueue(testRequest, new IHTTPCallback() {
            @Override
            public void onSuccess(Response response) throws IOException {
                Log.e("EEEE",response.getResponseBodyString());
                response.getResponseHeader("");
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @BindObserver(
            stateId = "1",
            isVarParameters = false
    )
    public void fun(Integer i, Double d) {
        Toast.makeText(this, String.valueOf(i + d), Toast.LENGTH_SHORT).show();
    }
}
