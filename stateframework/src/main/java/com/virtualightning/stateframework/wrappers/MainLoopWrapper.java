package com.virtualightning.stateframework.wrappers;

import android.os.Message;

import com.virtualightning.stateframework.core.BaseObserver;
import com.virtualightning.stateframework.core.InnerState;
import com.virtualightning.stateframework.core.MainLoopCall;
import com.virtualightning.stateframework.core.StateWrapper;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 *
 */
@SuppressWarnings("unused")
public final class MainLoopWrapper extends StateWrapper {
    private MainLoopCall loopCall;

    public MainLoopWrapper(BaseObserver observer, InnerState innerState) {
        super(observer, innerState);
        loopCall = MainLoopCall.getInstance();
    }

    @Override
    public void notifyAction(Object... args) {
        Message message = loopCall.obtainMessage();
        message.what = MainLoopCall.MSG_STATE_UPDATE;
        message.obj = new Object[]{this,args};
        message.sendToTarget();
    }
}
