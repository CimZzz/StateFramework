package com.virtualightning.stateframework.state;

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
    BaseObserver observer;

    public ObserverBuilder() {
        stateId = null;
        allowStop = false;
        runType = RunType.MAIN_LOOP;
        refType = ReferenceType.WEAK;
    }

    String getStateId() {
        return stateId;
    }

    public ObserverBuilder runType(int runType) {
        this.runType = runType;
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

    public ObserverBuilder observer(BaseObserver observer) {
        this.observer = observer;
        return this;
    }

    public BaseObserver build() {
        if(stateId == null || observer == null)
            throw new RuntimeException("在构建Observer时stateId,observer不能为空");

        observer.allowStop = allowStop;
        observer.refType = refType;
        observer.runType = runType;

        return observer;
    }
}
