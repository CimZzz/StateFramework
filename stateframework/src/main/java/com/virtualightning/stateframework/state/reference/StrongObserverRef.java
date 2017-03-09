package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class StrongObserverRef implements ObserverReference {
    private PassiveRef<BaseObserver> strongRef;

    public StrongObserverRef(BaseObserver observer) {
        this.strongRef = new PassiveRef<>(observer);
    }

    @Override
    public BaseObserver getObserver() {
        return strongRef.get();
    }

    @Override
    public void notify(Object... args) {
        BaseObserver observer = strongRef.get();
        if(observer != null)
            observer.notify(args);
    }

    @Override
    public void clear() {
        strongRef.clear();
    }
}
