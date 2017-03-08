package com.virtualightning.stateframework.state.wrappers;

import android.os.Looper;
import android.os.Message;

import com.virtualightning.stateframework.state.BaseObserver;
import com.virtualightning.stateframework.state.InnerState;
import com.virtualightning.stateframework.state.MainLoopCall;
import com.virtualightning.stateframework.state.StateWrapper;

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
        if(Looper.myLooper() == Looper.getMainLooper()){
            notifyReally(args);
        } else {
            Message message = loopCall.obtainMessage();
            message.what = MainLoopCall.MSG_STATE_UPDATE;
            message.obj = new Object[]{this,args};
            message.sendToTarget();
        }


    }
}
