package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

import java.lang.ref.WeakReference;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class WeakObserverRef extends BaseRef<WeakReference<BaseObserver>> {

    public WeakObserverRef(BaseObserver observer) {
        super(new WeakReference<>(observer));
    }
}
