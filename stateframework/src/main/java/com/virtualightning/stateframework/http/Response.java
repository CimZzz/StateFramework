package com.virtualightning.stateframework.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 *
 */
@SuppressWarnings("unused")
public class Response {
    final HttpURLConnection connection;
    boolean isBodyUsed;
    String charSet;

    Response(HttpURLConnection connection) {
        this.connection = connection;
        this.isBodyUsed = false;
        this.charSet = "UTF-8";
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    public String getResponseHeader(String key) throws IOException {
        for(Map.Entry<String,List<String>> entry : connection.getHeaderFields().entrySet()) {
            Log.e("DESS","key : " + entry.getKey() + ", value : " + Arrays.toString(entry.getValue().toArray()));
        }
        return "";//connection.getHeaderField(key);
    }

    public InputStream getResponseBodyStream() throws IOException{
        if(isBodyUsed)
            throw new RuntimeException("不能重复获取响应体");

        isBodyUsed = true;

        return connection.getInputStream();
    }

    public String getResponseBodyString() throws IOException {
        if(isBodyUsed)
            throw new RuntimeException("不能重复获取响应体");

        isBodyUsed = true;

        InputStream inputStream = connection.getInputStream();

        ByteArrayOutputStream byteWriter = new ByteArrayOutputStream(inputStream.available());

        byte[] buffer = new byte[256];
        int size;
        while ((size = inputStream.read(buffer)) != -1)
            byteWriter.write(buffer,0,size);

        return new String(byteWriter.toByteArray(),charSet);
    }
}
