package com.virtualightning.stateframework;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.1 将获取单例方法从包共享设置为共有，供开发者使用<br>
 * Modify : VLSimple2Develop_0.2.0 添加了消息序列号管理类，并对其兼容做出修改<br>
 * Description:<br>
 * 主线程调用类
 */
@SuppressWarnings("unused")
public final class MainLoopCall{
    static final int MSG_STATE_UPDATE = 1001;

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
    Message obtainMessage()
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

        /**
         * 处理Simple2Develop的内部消息<br>
         * <ol>
         *     <li>消息名：{@link #MSG_STATE_UPDATE} ： 执行状态观察者的更新</li>
         * </ol>
         * Modify : VLSimple2Develop_0.2.0 修改了序列号判断的方式<br>
         * @param msg 消息
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MSG_STATE_UPDATE :
                    Object[] objects = (Object[]) msg.obj;
                    StateMediator mediator = (StateMediator)objects[0];
                    msg.obj = null;
                    if(mediator.validateSequenceId(msg.arg1))
                        mediator.updateObserver((Object[])objects[1]);
                    break;
            }
        }
    }
}
