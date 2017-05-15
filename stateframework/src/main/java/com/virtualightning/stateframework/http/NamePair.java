package com.virtualightning.stateframework.http;

import java.io.DataOutputStream;
import java.net.URLEncoder;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.7 允许属性类型为Object。当值为 null 时此属性作废<br>
 * Modify : StateFrameWork_0.2.1 修复使用multipart时数据格式错误<br>
 * Modify : StateFrameWork_0.2.5 添加URL编码选项<br>
 * Description:<br>
 * HTTP键值对
 */
@SuppressWarnings("unused")
public final class NamePair extends Request.FormData {
    private final String value;

    public NamePair(String key, Object value) {
        super(key);
        this.value = String.valueOf(value);
        if(value == null)
            isEmpty = true;
    }

    @Override
    void writeToStream(DataOutputStream dataOutputStream) throws Exception {
        if(requestBody.isMultipart) {
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"");
            dataOutputStream.write(key.getBytes(requestBody.charset.Value));
            dataOutputStream.writeBytes("\"\r\n\r\n");
            if(requestBody.needEncode)
                dataOutputStream.writeBytes(URLEncoder.encode(value,requestBody.charset.Value));
            else dataOutputStream.write(value.getBytes(requestBody.charset.Value));
            dataOutputStream.writeBytes("\r\n");
        } else {
            dataOutputStream.write(key.getBytes(requestBody.charset.Value));
            dataOutputStream.writeBytes("=");
            if(requestBody.needEncode)
                dataOutputStream.writeBytes(URLEncoder.encode(value,requestBody.charset.Value));
            else dataOutputStream.write(value.getBytes(requestBody.charset.Value));
            dataOutputStream.write(value.getBytes(requestBody.charset.Value));
        }
    }
}
