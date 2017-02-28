package com.virtualightning.stateframework.core;

import android.os.Message;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 *
 */
@SuppressWarnings("unused")
public final class MainLoopWrapper extends StateWrapper {

    MainLoopWrapper(BaseObserver observer, InnerState innerState) {
        super(observer, innerState);
    }

    @Override
    public void notifyAction(Object... args) {
        Message message = MainLoopCall.getInstance().obtainMessage();
        message.what = MainLoopCall.MSG_STATE_UPDATE;
        message.obj = new Object[]{this,args};
        message.sendToTarget();
    }
}
