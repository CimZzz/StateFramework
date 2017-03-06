package com.virtualightning.stateframework.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public final class MultiFile extends Request.FormData {
    final String fileName;
    final String contentType;
    File file;

    public MultiFile(String key, String fileName, String contentType, File file) {
        super(key);
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;

        if(file == null)
            throw new RuntimeException("MultipartFile文件不能为空,文件名:" + fileName);
    }

    @Override
    void writeToStream(DataOutputStream dataOutputStream) throws Exception {

        String buffer = "Content-Disposition: form-data; name=\"" +
                URLEncoder.encode(key,requestBody.charset.Value) +
                "\"; filename=\"" +
                URLEncoder.encode(fileName,requestBody.charset.Value) +
                "\"\n" +
                "Content-Type: " +
                contentType +
                "\n\r\n";
        dataOutputStream.writeBytes(buffer);

        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[256];

            int size;

            while ((size = fileInputStream.read(bytes)) != -1) {
                dataOutputStream.write(bytes,0,size);
                dataOutputStream.flush();
            }

            dataOutputStream.writeBytes("\r\n");
        } catch (Exception e) {
            throw new RuntimeException("HTTP发送MultipartFile时出错,文件名:" + fileName + ",键名:" + key ,e);
        } finally {
            file = null;
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (Exception e) {}
        }
    }
}
