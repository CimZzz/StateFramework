package com.virtualightning.stateframework.reference;

import com.virtualightning.stateframework.core.BaseObserver;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public class StrongObserverRef implements ObserverReference{
    private final BaseObserver observer;

    public StrongObserverRef(BaseObserver observer) {
        this.observer = observer;
    }

    @Override
    public BaseObserver getObserver() {
        return observer;
    }
}
