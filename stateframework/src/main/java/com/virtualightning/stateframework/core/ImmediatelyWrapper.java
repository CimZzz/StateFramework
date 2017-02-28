package com.virtualightning.stateframework.core;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public final class ImmediatelyWrapper extends StateWrapper {

    ImmediatelyWrapper(BaseObserver observer, InnerState innerState) {
        super(observer, innerState);
    }

    @Override
    public void notifyAction(Object... args) {
        observerReference.getObserver().notify(args);
    }
}
