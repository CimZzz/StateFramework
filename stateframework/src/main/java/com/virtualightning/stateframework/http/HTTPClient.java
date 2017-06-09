package com.virtualightning.stateframework.http;

import com.virtualightning.stateframework.utils.FindUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.2.4 增加连接超时设置<br>
 * Modify : StateFrameWork_0.2.8 onFailure函数现在会处理全部异常<br>
 * Modify : StateFrameWork_0.2.9 增加Execute类作为HTTP执行类<br>
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

    public Execute genExecute(Request request, IHTTPCallback callback) {
        request.connectTimeOut = connectTimeOut;
        request.readTimeOut = readTimeOut;
        return new Execute(request, callback, threadPool);
    }

    public Execute genExecute(Object object, IHTTPCallback callback) {
        final Request request = FindUtils.findTransferClassByObject(object).transferData(object);
        return genExecute(request,callback);
    }

    public void close() {
        threadPool.shutdownNow();
    }
}
