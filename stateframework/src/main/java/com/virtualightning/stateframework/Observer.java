package com.virtualightning.stateframework;

import java.io.Serializable;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.2.3 添加观察者的执行方式设置，将观察者运行环境作为可选项，其余方法标记为过时<br>
 * Description:<br>
 * 状态观察者（基类）
 */
@SuppressWarnings("unused")
public abstract class Observer implements Serializable {
    public static final int RUN_TYPE_MAIN_LOOP = 0;
    public static final int RUN_TYPE_IMMEDIATELY = 1;
    public static final int RUN_TYPE_THREAD_POOL = 2;

    private boolean isActive;
    private boolean runWhenStop;
    private int runType;

    /*构造时设定状态运行环境*/

    /**
     * 初始化观察者
     * @since VLSimple2Develop_0.2.3
     */
    Observer() {
        this.runWhenStop = false;
        this.isActive = false;
        this.runType = RUN_TYPE_MAIN_LOOP;
    }

    /**
     * 设定状态观察者运行环境，如果为true则无论什么情况下均会执行更新，反之只能在状态记录处于运行状态下才可执行更新
     * Modify : VLSimple2Develop_0.2.3 过时<br>
     * @param runWhenStop 性质标识
     */
    @Deprecated
    Observer(boolean runWhenStop)
    {
        this.runWhenStop = runWhenStop;
        this.isActive = false;
        this.runType = RUN_TYPE_MAIN_LOOP;
    }

    /*设定状态观察者性质*/

    /**
     * 设定状态观察者性质，如果为true则界定观察者为活性状态观察者，反之为惰性状态观察者
     * @param active 性质标识
     */
    void setActive(boolean active)
    {
        this.isActive = active;
    }

    /**
     * 根据内部性质标识判断是否为活性状态观察者
     * @return 如果是返回true
     */
    boolean isActiveObserver()
    {
        return isActive;
    }

    /**
     * 设置状态观察者的执行方式：<br>
     * <ol>
     *     <li>{@link #RUN_TYPE_MAIN_LOOP} 运行在主线程</li>
     *     <li>{@link #RUN_TYPE_IMMEDIATELY} 立即运行</li>
     *     <li>{@link #RUN_TYPE_THREAD_POOL} 运行在线程池</li>
     * <ol/>
     * @since VLSimple2Develop_0.2.3
     * @param runType 运行方式
     */
    void setRunType(int runType) {
        this.runType = runType;
    }

    /**
     * 获取观察者的执行方式
     * @since VLSimple2Develop_0.2.3
     * @return 状态观察者的执行方式
     */
    int getRunType() {
        return runType;
    }

    /**
     * 设定状态观察者运行环境，如果为true则无论什么情况下均会执行更新，反之只能在状态记录处于运行状态下才可执行更新
     * @since VLSimple2Develop_0.2.3
     * @param runWhenStop 状态观察者运行环境
     */
    void setRunWhenStop(boolean runWhenStop) {
        this.runWhenStop = runWhenStop;
    }

    /*更新处理*/

    /**
     * 处理观察者更新事件（模板方法）
     * @param state 状态
     * @param isRunState 状态记录状态
     * @param arg 额外的参数
     */
    void handle(boolean state,boolean isRunState,Object... arg){
        if(runWhenStop || isRunState) {
            if (verify(state)) {
                if (state)
                    trueStateUpdate(arg);
                else falseStateUpdate(arg);
            }
        }
    }

    /**
     * 检测此状态是否满足更新条件
     * @param state 状态
     * @return 如果满足返回true
     */
    abstract boolean verify(boolean state);

    /**
     * 当处于true状态下更新操作
     * @param arg 额外的参数
     */
    protected abstract void trueStateUpdate(Object... arg);

    /**
     * 当处于false状态下更新操作
     * @param arg 额外的参数
     */
    protected abstract void falseStateUpdate(Object... arg);
}
