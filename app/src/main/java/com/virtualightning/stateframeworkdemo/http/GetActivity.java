package com.virtualightning.stateframeworkdemo.http;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.virtualightning.stateframework.anno.OnClick;
import com.virtualightning.stateframework.http.HTTPClient;
import com.virtualightning.stateframework.http.IHTTPCallback;
import com.virtualightning.stateframework.http.Response;
import com.virtualightning.stateframework.utils.Analyzer;
import com.virtualightning.stateframeworkdemo.R;
import com.virtualightning.stateframeworkdemo.request.Request1;

import java.io.IOException;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class GetActivity extends Activity {


    HTTPClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_1);
        Analyzer.analyzeAll(this,null);

        httpClient = new HTTPClient.Builder().build();
    }


    @OnClick(R.id.http3_btn)
    void onSendRequestClick() {
        //发送HTTP请求，这种写法有可能会导致内存泄露，仅供测试使用
        httpClient.enqueue(new Request1(), new IHTTPCallback() {
            @Override
            public void onSuccess(Response response) throws IOException {
                Log.i("HTTP",response.getResponseBodyString());
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("HTTP",e.getMessage());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //HTTPClient不再使用时需要关闭
        httpClient.close();
    }
}