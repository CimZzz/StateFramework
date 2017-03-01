package com.virtualightning.stateframework.wrappers;

import com.virtualightning.stateframework.core.BaseObserver;
import com.virtualightning.stateframework.core.InnerState;
import com.virtualightning.stateframework.core.StateWrapper;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class ThreadPoolWrapper extends StateWrapper {

    public ThreadPoolWrapper(BaseObserver observer, InnerState innerState) {
        super(observer, innerState);
    }

    @Override
    public void notifyAction(Object... args) {
        notifyReally(args);
    }
}
