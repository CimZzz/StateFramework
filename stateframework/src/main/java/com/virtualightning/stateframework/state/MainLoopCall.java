package com.virtualightning.stateframework.state;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 主线程调用
 */
@SuppressWarnings("unused")
public final class MainLoopCall {
    public static final int MSG_STATE_UPDATE = 1001;

    private static MainLoopCall call;
    private Handler handler;

    /*定义单例方法*/

    private MainLoopCall()
    {
        handler = new InternalHandler(Looper.getMainLooper());
    }

    public static MainLoopCall getInstance()
    {
        return call != null ? call : (call = new MainLoopCall());
    }



    /*委托方法*/

    /**
     * 运行Runnable方法
     * @param runnable Runnable
     */
    public void postRunnable(Runnable runnable){
        handler.post(runnable);
    }

    /**
     * 获得消息
     * @return 消息
     */
    public Message obtainMessage()
    {
        return handler.obtainMessage();
    }

    /*内部类*/

    /*内部Handler*/

    static class InternalHandler extends Handler{
        private InternalHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MSG_STATE_UPDATE :
                    Object[] objects = (Object[]) msg.obj;
                    StateWrapper wrapper = (StateWrapper) objects[0];
                    msg.obj = null;
                    if(wrapper.notifyCallBack(msg.arg1))
                        wrapper.notifyReally((Object[])objects[1]);
                    break;
            }
        }
    }
}