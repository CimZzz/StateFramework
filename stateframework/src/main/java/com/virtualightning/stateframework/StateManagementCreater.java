package com.virtualightning.stateframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by CimZzz on 11/16/16.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.2.3<br>
 * Description:<br>
 * 状态管理初始器
 * 用于构建状态管理者一些初始化参数
 */
@SuppressWarnings("unused")
public class StateManagementCreater {
    private ExecutorService threadPool;

    /**
     * 配置线程池
     * @param threadPool 线程池
     */
    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * 配置缺省的线程池，默认为 Cache 线程池
     */
    public void defaultThreadPool() {
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * 获得线程池
     * @return 获得配置的线程池
     */
    ExecutorService getThreadPool() {
        return threadPool;
    }
}
