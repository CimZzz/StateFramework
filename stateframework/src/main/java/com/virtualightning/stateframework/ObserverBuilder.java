package com.virtualightning.stateframework;

import android.support.annotation.NonNull;

/**
 * Created by CimZzz on 11/16/16.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.2.3<br>
 * Description:<br>
 * 注册状态观察者构造器
 * 根据属性用来构造注册观察者所需的一些参数
 */
@SuppressWarnings("unused")
public final class ObserverBuilder {
    private String stateId;
    private Observer observer;

    private boolean isActiveObserver;
    private boolean isRunWhenStop;
    private boolean hasSequence;
    private int runType;

    public ObserverBuilder() {
        init();
    }

    /**
     * 初始化注册状态观察者构造器
     */
    void init() {
        stateId = null;
        observer = null;

        isRunWhenStop = false;
        isActiveObserver = false;
        hasSequence = true;
        runType = Observer.RUN_TYPE_MAIN_LOOP;
    }

    /*配置方法*/

    /**
     * 配置状态观察者是否为活性状态观察者
     * @param isActiveObserver 是否为活性状态观察者
     * @return 注册状态观察者构造器
     */
    public ObserverBuilder activeObserver(boolean isActiveObserver) {
        this.isActiveObserver = isActiveObserver;
        return this;
    }

    /**
     * 配置状态观察者运行环境
     * @param isRunWhenStop 观察者运行环境
     * @return 注册状态观察者构造器
     */
    public ObserverBuilder runWhenStop(boolean isRunWhenStop) {
        this.isRunWhenStop = isRunWhenStop;
        return this;
    }

    /**
     * 配置状态观察者是否拥有消息序列（只限执行方式为 {@link Observer#RUN_TYPE_MAIN_LOOP} 下有效）
     * @param hasSequence 是否拥有消息序列
     * @return 注册状态观察者构造器
     */
    public ObserverBuilder sequence(boolean hasSequence) {
        this.hasSequence = hasSequence;
        return this;
    }

    /**
     * 配置状态观察者的运行方式
     * @param runType 状态观察者的运行方式
     * @return 注册状态观察者构造器
     */
    public ObserverBuilder runType(int runType) {
        this.runType = runType;
        return this;
    }

    /**
     * 配置状态观察者的观察的状态名
     * @param stateId 状态名
     * @return 注册状态观察者构造器
     */
    public ObserverBuilder stateId(@NonNull String stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * 配置状态观察者对象
     * @param observer 状态观察者对象
     * @return 注册状态观察者构造器
     */
    public ObserverBuilder observer(@NonNull Observer observer) {
        this.observer = observer;
        return this;
    }

    /*构造方法*/

    /**
     * 构造状态观察者
     */
    void build() {
        if(stateId == null)
            throw new RuntimeException("状态观察者构造器时发生错误 : 状态名为 null");

        if(observer == null)
            throw new RuntimeException("状态观察者构造器时发生错误 : 状态观察者为 null , 所监控状态的状态名为 " + stateId);

        observer.setActive(isActiveObserver);
        observer.setRunWhenStop(isRunWhenStop);
        observer.setRunType(runType);
    }

    /**
     * 获取状态ID
     * @return 状态ID
     */
    String getStateId() {
        return stateId;
    }

    /**
     * 获取状态观察者
     * @return 状态观察者
     */
    Observer getObserver() {
        return observer;
    }

    /**
     * 获取是否拥有消息序列（执行方式为 {@link Observer#RUN_TYPE_MAIN_LOOP} 下有效，其余情况下返回 false）
     * @return 是否拥有消息序列
     */
    boolean isHasSequence() {
        return runType == Observer.RUN_TYPE_MAIN_LOOP && hasSequence;
    }
}
