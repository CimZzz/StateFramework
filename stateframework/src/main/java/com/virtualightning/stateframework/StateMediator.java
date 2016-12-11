package com.virtualightning.stateframework;

import android.os.Message;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.4 添加切换相反状态方法<br>
 * Modify : VLSimple2Develop_0.1.6 修正了严重的内存泄漏错误（当观察者是内部类并且StateRecord被外部引用）<br>
 * Modify : VLSimple2Develop_0.1.8 修正了Observer被非正常回收错误<br>
 * Modify : VLSimple2Develop_0.1.9 再次修正了内存泄露问题<br>
 * Modify : VLSimple2Develop_0.2.0 增加了消息序列号管理类，将序列号功能分离作为可选项<br>
 * Modify : VLSimple2Develop_0.2.2 修复了消息队列的空指针异常<br>
 * Modify : VLSimple2Develop_0.2.3 添加观察者的执行方式<br>
 * Description:<br>
 * 状态中介者
 */
@SuppressWarnings("unused")
public final class StateMediator implements Serializable {
    private State state;
    private InternalState internalState;
    private Observer observer;
    private MessageSequence msgSequence;

    /**
     * 只用于注册新状态时使用的构造函数，每次构造的同时会创建一个新的状态对象
     * @param state 状态
     * @param internalState 状态记录内部状态
     */
    StateMediator(boolean state,InternalState internalState)
    {
        this.state = new State(state);
        this.internalState = internalState;

        this.state.addMediator(this);
    }

    /**
     * 只用于捕获全局状态或继承状态时使用的构造函数，其中的状态对象为被克隆实例的状态对象
     * @param state 状态对象
     * @param internalState 状态记录内部状态
     */
    private StateMediator(State state,InternalState internalState)
    {
        this.state = state;
        this.internalState = internalState;

        this.state.addMediator(this);
    }

    /*注册状态观察者*/

    /**
     * 注册状态观察者，一旦注册之后不能更改<br>
     * Modify : VLSimple2Develop_0.1.6 由内部强引用变为弱引用，确保了内部类的垃圾回收<br>
     * Modify : VLSimple2Develop_0.1.8 由弱引用改回强引用，通过 StateRecord 的内部状态来确保垃圾回收<br>
     * Modify : VLSimple2Develop_0.2.0 添加条件判断是否需要使用消息序列号管理类<br>
     * @param observer 状态观察者
     * @param hasSequence 判断是否需要消息序列号管理类
     */
    void registObserver(Observer observer,boolean hasSequence)
    {
        this.observer = observer;

        this.msgSequence = hasSequence ? new MessageSequence() : null;
    }


    /*状态的复制与继承*/

    /**
     * 克隆状态中介，多个克隆体之间共享同一个状态对象，实现状态的一致性。
     * @param internalState 内部状态
     * @return 状态中介
     */
    StateMediator cloneState(InternalState internalState)
    {
        return new StateMediator(state,internalState);
    }


    /*状态变更*/

    /**
     * 更改状态为相反状态
     * @since VLSimple2Develop_0.1.4
     * @param arg 额外的参数
     */
    void changeStateAgainst(Object... arg)
    {
        synchronized (this) {
            this.state.changeStateAgainst(arg);
        }
    }

    /**
     * 改变状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    void changeState(boolean state,Object... arg)
    {
        this.state.changeState(state, arg);
    }

    /*获取状态*/

    /**
     * 获取状态
     * @return 返回状态
     */
    boolean getState()
    {
        return state.getState();
    }

    /*执行更新*/

    /**
     * 发送通知更新消息
     * Modify : VLSimple2Develop_0.2.0 添加条件判断，当状态记录处于销毁状态时<br>
     * Modify : VLSimple2Develop_0.2.3 添加条件判断，如果状态观察者属于立即运行状态时，则会判断是否为耗时操作决定立即执行或者在线程池中执行<br>
     * @param arg 额外的参数
     */
    void notifyObserver(boolean isStateCall,Object... arg)
    {
        synchronized (this) {
            if ((observer == null) || (!isStateCall && !observer.isActiveObserver()) || internalState.isDestroyState())
                return;

        }

        switch (observer.getRunType()) {
            /*运行在主线程*/
            case Observer.RUN_TYPE_MAIN_LOOP :
                /*生成下一个序列号*/
                int sequenceId = msgSequence != null ? msgSequence.nextSequence() : -1;

                Message msg = MainLoopCall.getInstance().obtainMessage();
                msg.what = MainLoopCall.MSG_STATE_UPDATE;
                msg.arg1 = sequenceId;
                msg.obj = new Object[]{this,arg};
                msg.sendToTarget();
                break;
            /*立即运行*/
            case Observer.RUN_TYPE_IMMEDIATELY :
                updateObserver(arg);
                break;
            /*运行在线程池*/
            case Observer.RUN_TYPE_THREAD_POOL:
                StateManagement.getInstance().getThreadPool().execute(new StateRunnable(this,arg));
                break;
        }
    }

    /**
     * 更新观察者
     * Modify : VLSimple2Develop_0.2.0 修改序列号初始化方式<br>
     * @param arg 额外的参数
     */
    synchronized void updateObserver(@Nullable Object... arg)
    {
        Observer observer = this.observer;

        /*初始化序列号*/
        if(msgSequence != null)
            msgSequence.initSequence();

        /*处理观察者更新*/
        if(observer != null)
            observer.handle(state.getState(),internalState.isRunState(),arg);
    }



    /*序列ID*/

    /**
     * 验证当前序列号是否为最新序列号。如果没有使用消息序列机制则直接返回true
     * @since VLSimple2Develop_0.2.0<br>
     * @param sequenceId 当前序列号ID
     * @return 如果验证成功返回true
     */
    boolean validateSequenceId(int sequenceId)
    {
        return msgSequence == null || msgSequence.validateSequence(sequenceId);
    }


    /*清空中介状态*/

    /**
     * 清空中介状态，保证垃圾回收
     * Modify : VLSimple2Develop_0.2.2 修复了消息队列的空指针异常<br>
     * @since VLSimple2Develop_0.1.9<br>
     */
    void clear()
    {
        synchronized (this) {
            observer = null;
            state = null;
            if(msgSequence != null)
                msgSequence.initSequence();
        }
    }
}
