package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class NullObserverRef implements ObserverReference {

    public NullObserverRef() {
    }

    @Override
    public BaseObserver getObserver() {
        return null;
    }

    @Override
    public void notify(Object... args) {

    }

    @Override
    public void clear() {

    }
}
