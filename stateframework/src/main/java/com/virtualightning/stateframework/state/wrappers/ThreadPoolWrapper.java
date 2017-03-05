package com.virtualightning.stateframework.state.wrappers;

import com.virtualightning.stateframework.state.BaseObserver;
import com.virtualightning.stateframework.state.InnerState;
import com.virtualightning.stateframework.state.StateWrapper;

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
