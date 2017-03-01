package com.virtualightning.stateframework.core;

import com.virtualightning.stateframework.constant.ReferenceType;
import com.virtualightning.stateframework.reference.NullObserverRef;
import com.virtualightning.stateframework.reference.ObserverReference;
import com.virtualightning.stateframework.reference.SoftObserverRef;
import com.virtualightning.stateframework.reference.StrongObserverRef;
import com.virtualightning.stateframework.reference.WeakObserverRef;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public abstract class StateWrapper {
    final ObserverReference observerReference;
    final InnerState innerState;

    protected StateWrapper(BaseObserver observer,InnerState innerState) {
        switch (observer.refType) {
            case ReferenceType.WEAK:
                observerReference = new WeakObserverRef(observer);
                break;
            case ReferenceType.SOFT:
                observerReference = new SoftObserverRef(observer);
                break;
            case ReferenceType.STRONG:
                observerReference = new StrongObserverRef(observer);
                break;
            default:
                observerReference = new NullObserverRef(observer);
                break;
        }

        this.innerState = innerState;
    }

    synchronized final void notify(Object... args) {
        BaseObserver observer = observerReference.getObserver();
        if(observer == null || (observer.allowStop && innerState.isStop()))
            return;

        notifyAction(args);
    }

    public synchronized final void notifyReally(Object... args) {
        observerReference.getObserver().notify(args);
    }
    public abstract void notifyAction(Object... args);
    public boolean notifyCallBack(Object obj) {
        return true;
    }
}
