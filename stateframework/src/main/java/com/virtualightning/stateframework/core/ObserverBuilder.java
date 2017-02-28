package com.virtualightning.stateframework.core;

import com.virtualightning.stateframework.constant.ReferenceType;
import com.virtualightning.stateframework.constant.RunType;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public final class ObserverBuilder {
    String stateId;
    boolean allowStop;
    int runType;
    int refType;
    Class<? extends BaseObserver> observerCls;
    StateRunnable runnable;

    public ObserverBuilder() {
        stateId = null;
        allowStop = false;
        runType = RunType.MAIN_LOOP;
        refType = ReferenceType.WEAK;
        observerCls = DefaultObserver.class;
        runnable = null;
    }

    String getStateId() {
        return stateId;
    }

    public ObserverBuilder runType(int runType) {
        this.runType = runType;
        return this;
    }

    public ObserverBuilder runnable(StateRunnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public ObserverBuilder refType(int refType) {
        this.refType = refType;
        return this;
    }

    public ObserverBuilder stateId(String stateId) {
        this.stateId = stateId;
        return this;
    }

    public ObserverBuilder allowStop(boolean allowStop) {
        this.allowStop = allowStop;
        return this;
    }

    public ObserverBuilder observerCls(Class<? extends BaseObserver> observerCls) {
        this.observerCls = observerCls;
        return this;
    }

    public BaseObserver build() {
        if(runnable == null || stateId == null || observerCls == null)
            throw new RuntimeException("在构建Observer时stateId,runnable,observerCls不能为空");

        try {
            BaseObserver observer = this.observerCls.newInstance();

            observer.allowStop = allowStop;
            observer.refType = refType;
            observer.runType = runType;
            observer.runnable = runnable;
        } catch (Exception e) {
            throw new RuntimeException("初始化观察者失败,观察者的构造函数必须为空.观察者 : " + observerCls.getName(),e);
        }

        return null;
    }
}
