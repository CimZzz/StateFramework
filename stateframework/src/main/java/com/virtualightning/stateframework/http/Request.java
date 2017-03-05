package com.virtualightning.stateframework.http;

import com.virtualightning.stateframework.constant.HTTPMethodType;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public final class Request {
    private static char[] BOUNDARY_MAP = new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
            ,'T','U','V','W','X','Y','Z','a','b','c','d','e','f'
            ,'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
            ,'1','2','3','4','5','6','7','8','9'};

    String url;
    String method;
    String charset;
    boolean isMultipart;
    HashMap<String,String> requestHeader;
    HashMap<String,FormData> formData;

    Request() {
        requestHeader = new HashMap<>();
        formData = new HashMap<>();
    }

    HttpURLConnection commitRequest() throws Exception {
        HttpURLConnection connection = null;
        try {
            /*开启URL连接*/
            connection = (HttpURLConnection) new URL(url).openConnection();

            /*设置HTTP连接前的一些请求参数*/
            connection.setRequestMethod(method);
            /*设置头参数*/
            if(requestHeader.size() != 0) {
                for(Map.Entry<String,String> entry : requestHeader.entrySet())
                    connection.setRequestProperty(entry.getKey(),entry.getValue());
            }

            /*开始连接*/
            if(method.equals(HTTPMethodType.POST)) {
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestProperty("Cache-Control","no-cache");

                String boundary = null;
                /*如果为Multipart,则要生成boundary*/
                if(isMultipart) {
                    Random random = new Random();
                    StringBuilder boundaryBuilder = new StringBuilder();
                    boundaryBuilder.append("-----BoundaryByStateFramework");
                    for(int i = 0 ; i < 10;i++)
                        boundaryBuilder.append(BOUNDARY_MAP[random.nextInt(BOUNDARY_MAP.length)]);
                    boundary = boundaryBuilder.toString();
                    connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
                } else {
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                }

                connection.connect();

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

                if(isMultipart) {
                    boundary = "--" + boundary;
                    for (FormData formData : this.formData.values()) {
                        dataOutputStream.writeBytes(boundary);
                        dataOutputStream.writeBytes("\n");
                        formData.writeToStream(dataOutputStream);
                    }
                    dataOutputStream.writeBytes(boundary);
                    dataOutputStream.writeBytes("--\r\n");
                } else {
                    boolean isFirst = true;
                    for (FormData formData : this.formData.values()) {
                        if (isFirst)
                            isFirst = false;
                        else dataOutputStream.write("&".getBytes(charset));
                        formData.writeToStream(dataOutputStream);
                        dataOutputStream.flush();
                    }
                }
            } else {
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.connect();
            }

            return connection;
        } catch (ProtocolException e) {
            throw new RuntimeException("使用了错误的请求方法:" + method + "  定位请求URL为:" + url,e);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL地址错误:" + url,e);
        } catch (Exception e) {
            throw e;
        }
    }


    public static class Builder {
        Request requestBody;
        public Builder () {
            requestBody = new Request();
        }

        public Builder url(String url) {
            requestBody.url = url;
            return this;
        }

        public Builder method(String method) {
            requestBody.method = method.toUpperCase();
            return this;
        }

        public Builder charset(String charset) {
            requestBody.charset = charset;
            return this;
        }

        public Builder header(String key,String value) {
            requestBody.requestHeader.put(key, value);
            return this;
        }

        public Builder addFormData(FormData formData) {
            if(formData instanceof MultiFile)
                requestBody.isMultipart = true;
            formData.requestBody = requestBody;
            requestBody.formData.put(formData.key,formData);HttpURLConnection urlConnection;
            return this;
        }

        public Request build() {
            return requestBody;
        }
    }

    static abstract class FormData {
        final String key;
        Request requestBody;

        FormData(String key) {
            this.key = key;
        }

        abstract void writeToStream(DataOutputStream dataOutputStream) throws Exception;
    }


}
