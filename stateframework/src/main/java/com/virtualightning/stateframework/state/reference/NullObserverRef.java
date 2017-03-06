package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public class NullObserverRef implements ObserverReference{

    public NullObserverRef(BaseObserver observer) {
    }

    @Override
    public BaseObserver getObserver() {
        return null;
    }
}