package com.virtualightning.stateframework.state;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 观察者基类
 */
@SuppressWarnings("unused")
public abstract class BaseObserver {
    int runType;
    int refType;
    boolean allowStop;

    protected abstract void notify(Object... objects);
}
