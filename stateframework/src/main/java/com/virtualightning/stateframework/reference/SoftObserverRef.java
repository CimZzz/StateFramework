package com.virtualightning.stateframework.reference;

import com.virtualightning.stateframework.core.BaseObserver;

import java.lang.ref.SoftReference;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public class SoftObserverRef implements ObserverReference{
    private final SoftReference<BaseObserver> observer;

    public SoftObserverRef(BaseObserver observer) {
        this.observer = new SoftReference<>(observer);
    }

    @Override
    public BaseObserver getObserver() {
        return observer.get();
    }
}
