package com.virtualightning.stateframework.http;

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;

/**
 * Created by CimZzz on 17/5/24.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.2.9<br>
 * Description:<br>
 * Description
 */
public class Execute {
    final Request request;
    final IHTTPCallback callback;
    final ExecutorService threadPool;
    HttpURLConnection connection;


    boolean isRunning;

    public Execute(Request request, IHTTPCallback callback, ExecutorService threadPool) {
        this.request = request;
        this.callback = callback;
        this.threadPool = threadPool;
        this.isRunning = true;
    }

    public void stopNow() {
        synchronized (this) {
            this.isRunning = false;
            if(connection != null)
                connection.disconnect();
            connection = null;
        }
    }

    public void execute() {
        try {
            synchronized (this) {
                if(isRunning)
                    connection = request.commitRequest();
                else throw new StopException();
            }

            Response response = new Response(connection);

            callback.onSuccess(response);
        } catch (Exception e) {
            if(!(e instanceof StopException) && !isRunning)
                callback.onFailure(new StopException(e));
            else callback.onFailure(e);
        } finally {
            if(connection != null)
                connection.disconnect();

        }
    }

    public void enqueue() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        });
    }
}
