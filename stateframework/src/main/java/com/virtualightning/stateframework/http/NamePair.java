package com.virtualightning.stateframework.http;

import java.io.DataOutputStream;
import java.net.URLEncoder;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.7 允许属性类型为Object。当值为 null 时此属性作废<br>
 * Description:<br>
 * HTTP键值对
 */
@SuppressWarnings("unused")
public final class NamePair extends Request.FormData {
    private final String value;

    public NamePair(String key, Object value) {
        super(key);
        this.value = value != null ? String.valueOf(value) : null;
    }

    @Override
    void writeToStream(DataOutputStream dataOutputStream) throws Exception {
        if(value == null)
            return;

        if(requestBody.isMultipart) {
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"");
            dataOutputStream.writeBytes(URLEncoder.encode(key,requestBody.charset.Value));
            dataOutputStream.writeBytes("\"\n\r\n");
            dataOutputStream.writeBytes(URLEncoder.encode(value,requestBody.charset.Value));
            dataOutputStream.writeBytes("\r\n");
        } else {
            dataOutputStream.writeBytes(URLEncoder.encode(key,requestBody.charset.Value));
            dataOutputStream.writeBytes("=");
            dataOutputStream.writeBytes(URLEncoder.encode(value,requestBody.charset.Value));
        }
    }
}
