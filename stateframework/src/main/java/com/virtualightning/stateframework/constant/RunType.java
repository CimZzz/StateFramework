package com.virtualightning.stateframework.constant;

/**
 * Created by CimZzz on 12/11/16.<br>
 * Project Name : Market-Online<br>
 * Since : Market-Online_0.0.1<br>
 * Description:<br>
 * 观察者运行类型
 */
@SuppressWarnings("unused")
public final class RunType {
    /**
     * 主线程
     */
    public static final int MAIN_LOOP = 0;

    /**
     * 当前线程
     */
    public static final int CURRENT = 1;


    /**
     * 线程池
     */
    public static final int THREAD_POOL = 2;


    /**
     * 主线程并使用消息队列保证获取的消息为某一时刻的最新消息
     */
    public static final int MAIN_LOOP_SEQ = 3;
}
