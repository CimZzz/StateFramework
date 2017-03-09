package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

import java.lang.ref.SoftReference;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class SoftObserverRef extends BaseRef<SoftReference<BaseObserver>> {

    public SoftObserverRef(BaseObserver observer) {
        super(new SoftReference<>(observer));
    }
}
