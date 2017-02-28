package com.virtualightning.stateframework.core;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public abstract class BaseObserver {
    int runType;
    int refType;
    boolean allowStop;
    StateRunnable runnable;

    public final void notify(Object... objects) {
        runnable.run(objects);
    }
}
