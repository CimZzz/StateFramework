package com.virtualightning.stateframework;

import java.util.concurrent.ExecutorService;

/**
 * Created by CimZzz on 16/6/26.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.2.3 添加了线程池配置方式，与初始化方法的模版方法<br>
 * Description:<br>
 * 状态管理器（单例）（备忘录）
 */
@SuppressWarnings("unused")
public final class StateManagement {
    private static StateManagement management;

    private boolean isInit;

    private ExecutorService threadPool;

    /*定义单例方法*/

    private StateManagement()
    {
        isInit = false;
    }

    public static StateManagement getInstance()
    {
        return management != null ? management : (management = new StateManagement());
    }





    /*状态记录操作方法*/


    /**
     * 创建临时的状态记录，但不存入缓存池内
     * @param key Class对象
     * @return 临时的状态记录
     */
    public StateRecord getTempStateRecord(Class key)
    {
        return new StateRecord();
    }



    /**
     * 使用指定标签发送消息日志，监控状态管理器的内部状态
     * @param tag 标签
     */
    public static void sendInformationLog(String tag)
    {
        //send LogMessage
    }

    /*配置操作*/

    /**
     * 初始化配置状态管理
     * @since VLSimple2Develop_0.2.3
     * @param creater 状态管理初始器
     */
    public void init(StateManagementCreater creater) {
        synchronized (this) {
            if(isInit)
                return;
            isInit = true;
            this.threadPool = creater.getThreadPool();
        }


    }

    /**
     * 获取线程池，如果不存在则会默认创建 Cache 线程池
     * @since VLSimple2Develop_0.2.3
     * @return 线程池
     */
    public ExecutorService getThreadPool() {
        if(threadPool == null)
            throw new RuntimeException("状态管理器的线程池尚未初始化");
        return threadPool;
    }
}
