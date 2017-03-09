package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

import java.lang.ref.Reference;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public abstract class BaseRef<T extends Reference<BaseObserver>> implements ObserverReference {
    private final T ref;

    public BaseRef(T ref) {
        this.ref = ref;
    }

    @Override
    public BaseObserver getObserver() {
        return ref.get();
    }

    @Override
    public void notify(Object... args) {
        BaseObserver observer = ref.get();

        if(observer != null)
            observer.notify(args);
    }

    @Override
    public void clear() {
        ref.clear();
    }
}
