package com.virtualightning.stateframework.http;

import com.virtualightning.stateframework.utils.FindUtils;

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.2.4 增加连接超时设置<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public final class HTTPClient {
    ExecutorService threadPool;
    Integer connectTimeOut;
    Integer readTimeOut;

    HTTPClient(Builder builder) {
        this.threadPool = builder.threadPool != null ? builder.threadPool : Executors.newCachedThreadPool();
    }


    public static class Builder {
        ExecutorService threadPool;
        Integer connectTimeOut;
        Integer readTimeOut;

        public Builder threadPool(ExecutorService executor) {
            this.threadPool = executor;
            return this;
        }

        public Builder connectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder readTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public HTTPClient build() {
            return new HTTPClient(this);
        }
    }

    public void execute(Request request,IHTTPCallback callback) {
        HttpURLConnection connection = null;
        try {
            request.connectTimeOut = connectTimeOut;
            request.readTimeOut = readTimeOut;
            connection = request.commitRequest();

            Response response = new Response(connection);

            callback.onSuccess(response);
        } catch (Exception e) {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            callback.onFailure(e);
        } finally {
            if(connection != null)
                connection.disconnect();

        }
    }

    public void enqueue(final Request request,final IHTTPCallback callback) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                execute(request, callback);
            }
        });
    }

    public void execute(Object object,IHTTPCallback callback) {
        final Request request = FindUtils.findTransferClassByObject(object).transferData(object);
        execute(request,callback);
    }

    public void enqueue(Object object, final IHTTPCallback callback) {
        final Request request = FindUtils.findTransferClassByObject(object).transferData(object);
        enqueue(request,callback);
    }

    public void close() {
        threadPool.shutdownNow();
    }
}
