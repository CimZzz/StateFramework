package com.virtualightning.stateframework.http;

import java.io.DataOutputStream;
import java.net.URLEncoder;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public final class NamePair extends Request.FormData {
    private final String value;

    public NamePair(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    void writeToStream(DataOutputStream dataOutputStream) throws Exception {
        if(requestBody.isMultipart) {
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"");
            dataOutputStream.writeBytes(URLEncoder.encode(key,requestBody.charset));
            dataOutputStream.writeBytes("\"\n\r\n");
            dataOutputStream.writeBytes(URLEncoder.encode(value,requestBody.charset));
            dataOutputStream.writeBytes("\r\n");
        } else {
            dataOutputStream.writeBytes(URLEncoder.encode(key,requestBody.charset));
            dataOutputStream.writeBytes("=");
            dataOutputStream.writeBytes(URLEncoder.encode(value,requestBody.charset));
        }
    }
}
