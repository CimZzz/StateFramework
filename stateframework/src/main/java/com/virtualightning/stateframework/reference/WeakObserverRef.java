package com.virtualightning.stateframework.reference;

import com.virtualightning.stateframework.core.BaseObserver;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public class WeakObserverRef implements ObserverReference{
    private final WeakReference<BaseObserver> observer;

    public WeakObserverRef(BaseObserver observer) {
        this.observer = new WeakReference<>(observer);
    }

    @Override
    public BaseObserver getObserver() {
        return observer.get();
    }
}
