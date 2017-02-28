package com.virtualightning.stateframework.core;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class ThreadPoolWrapper extends StateWrapper {

    ThreadPoolWrapper(BaseObserver observer, InnerState innerState) {
        super(observer, innerState);
    }

    @Override
    public void notifyAction(Object... args) {
        observerReference.getObserver().notify(args);
    }
}
