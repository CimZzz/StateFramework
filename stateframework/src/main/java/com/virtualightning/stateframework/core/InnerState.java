package com.virtualightning.stateframework.core;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public final class InnerState {
    private boolean isStop;

    InnerState() {
        isStop = false;
    }

    boolean isStop() {
        return isStop;
    }

    void setStop(boolean stop) {
        isStop = stop;
    }
}
