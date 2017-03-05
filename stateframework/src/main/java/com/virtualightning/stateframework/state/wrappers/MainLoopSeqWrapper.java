package com.virtualightning.stateframework.state.wrappers;

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
public final class MainLoopSeqWrapper extends StateWrapper {
    private int sequenceId;

    public MainLoopSeqWrapper(BaseObserver observer, InnerState innerState) {
        super(observer, innerState);

        sequenceId = -1;
    }

    @Override
    public void notifyAction(Object... args) {
        sequenceId ++;

        if(sequenceId == Integer.MAX_VALUE)
            sequenceId = 0;

        Message message = MainLoopCall.getInstance().obtainMessage();
        message.what = MainLoopCall.MSG_STATE_UPDATE;
        message.obj = new Object[]{this,args};
        message.arg1 = sequenceId;
        message.sendToTarget();
    }


    @Override
    public boolean notifyCallBack(Object obj) {
        return obj.equals(sequenceId);
    }
}
