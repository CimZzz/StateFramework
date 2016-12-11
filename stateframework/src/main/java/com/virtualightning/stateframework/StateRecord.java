package com.virtualightning.stateframework;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.4 添加切换相反状态方法<br>
 * Modify : VLSimple2Develop_0.1.6 添加状态记录自身的内部状态以及判断方法<br>
 * Modify : VLSimple2Develop_0.1.9 再次修正了内存泄露问题<br>
 * Modify : VLSimple2Develop_0.2.0 添加了消息序列号管理类，并对其兼容做出修改<br>
 * Modify : VLSimple2Develop_0.2.3 添加了通过观察者构造器注册观察者的方法，其余注册方法均为过时方法;添加了轮询注册状态观察者的方法<br>
 * Description:<br>
 * 状态记录
 */
@SuppressWarnings("unused")
public final class StateRecord implements Serializable{

    private static final ConcurrentHashMap<String,StateMediator> globalStates = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,StateMediator> monitorStates;
    private InternalState internalState;
    private final Object locker;

    StateRecord()
    {
        monitorStates = new ConcurrentHashMap<>();
        internalState = new InternalState();
        locker = new Object();
    }

    /*注册状态与内部状态设置*/

    /**
     * 注册全局状态，如果当前状态名已经存在则无法覆盖添加
     * @param stateID 状态ID
     * @param state 状态
     */
    public static void registGlobalState(String stateID,boolean state)
    {
        synchronized (globalStates)
        {
            if(!globalStates.containsKey(stateID))
                globalStates.put(stateID,new StateMediator(state,null));
        }
    }

    /**
     * 设置当前状态为可运行状态，更新全部状态<br>
     * Modify : VLSimple2Develop_0.1.6 如果当前状态处于Destory时不会执行任何动作<br>
     */
    public void setRunState()
    {
        if(!isDestroyState()) {
            internalState.setInternalState(InternalState.INTERNAL_STATE_RUN);
            synchronized (locker) {
                for (StateMediator mediator : monitorStates.values())
                    mediator.notifyObserver(false);
            }
        }
    }

    /**
     * 设置当前状态为停止状态
     */
    public void setStopState()
    {
        internalState.setInternalState(InternalState.INTERNAL_STATE_STOP);
    }

    /**
     * 设置当前状态为销毁状态
     * Modify : VLSimple2Develop_0.1.8 清除内部全部引用<br>
     * Modify : VLSimple2Develop_0.1.9 清除内部全部引用同时调用其中自我清理的方法<br>
     * @since : VLSimple2Develop_0.1.6
     */
    public void setDestroyState()
    {
        synchronized (locker) {
            internalState.setInternalState(InternalState.INTERNAL_STATE_DESTORY);

            /*清除监控状态表*/
            for (StateMediator mediator : monitorStates.values())
                mediator.clear();

            monitorStates.clear();
        }
    }

    /**
     * 判断当前的内部状态是否处于运行状态。
     * @return 如果当前内部状态为 {@link InternalState#INTERNAL_STATE_RUN} 时返回true
     */
    public boolean isRunState()
    {
        return internalState.isRunState();
    }

    /**
     * 判断当前的内部状态是否处于创建状态。
     * @since : VLSimple2Develop_0.1.6
     * @return 如果当前内部状态为 {@link InternalState#INTERNAL_STATE_CREATE} 时返回true
     */
    public boolean isCreateState()
    {
        return internalState.isCreateState();
    }

    /**
     * 判断当前的内部状态是否处于销毁状态。
     * @since : VLSimple2Develop_0.1.6
     * @return 如果当前内部状态为 {@link InternalState#INTERNAL_STATE_DESTORY} 时返回true
     */
    public boolean isDestroyState()
    {
        return internalState.isDestroyState();
    }

    /**
     * 获取当前的内部状态ID
     * @since : VLSimple2Develop_0.1.6
     * @return 返回当前状态的状态ID
     */
    public int getCurrnetInternalState()
    {
        return internalState.getCurrnetInternalState();
    }



    /*添加监控状态，如果当前状态名已经存在则无法覆盖添加*/

    /**
     * 新建状态并监控
     * @param stateID 状态ID
     * @param state 状态
     */
    void monitorState(String stateID,Boolean state)
    {
        synchronized (locker)
        {
            if(monitorStates.containsKey(stateID))
                return;

            monitorStates.put(stateID,new StateMediator(state,internalState));
        }
    }

    /**
     * 捕捉全局状态并监控
     * @param stateID 全局状态ID
     */
    void monitorState(String stateID)
    {
        synchronized (locker)
        {
            if(!globalStates.containsKey(stateID) || monitorStates.containsKey(stateID))
                return;

            StateMediator mediator = globalStates.get(stateID);

            monitorStates.put(stateID,mediator.cloneState(internalState));
        }
    }

    /**
     * 继承状态并监控
     * @param record 状态记录
     * @param stateIDs 状态ID数组
     */
    void monitorState(StateRecord record,String... stateIDs)
    {
        synchronized (locker)
        {
            Map<String,StateMediator> parentMonitorMap = record.monitorStates;
            for(String stateID : stateIDs)
                if(parentMonitorMap.containsKey(stateID) && !monitorStates.containsKey(stateID))
                    monitorStates.put(stateID,parentMonitorMap.get(stateID).cloneState(internalState));
        }
    }

    /*注册状态观察者*/


    /**
     * 根据注册状态观察者构造器注册状态观察者<br>
     * @since VLSimple2Develop_0.2.3
     * @param builder 注册状态观察者构造器
     */
    public void registerObserver(ObserverBuilder builder) {
        builder.build();
        String stateID = builder.getStateId();
        boolean hasSequence = builder.isHasSequence();
        Observer observer = builder.getObserver();

        synchronized (locker){
            if(!monitorStates.containsKey(stateID))
                return;

            monitorStates.get(stateID).registObserver(observer,hasSequence);
        }
    }

    /**
     * 通过状态观察者轮询注册接口配置注册状态者构造器，根据注册状态观察者构造器注册状态观察者<br>
     * @since VLSimple2Develop_0.2.3
     * @param registerObserverCallback 状态观察者轮询注册接口
     */
    public void registerObserverByLoop(IRegisterObserverCallback registerObserverCallback) {
        synchronized (locker) {
            ObserverBuilder builder = new ObserverBuilder();
            for(Map.Entry<String,StateMediator> entry : monitorStates.entrySet()) {
                String stateId = entry.getKey();

                builder.init();
                builder.stateId(stateId);
                if(registerObserverCallback.configObserverBuilder(stateId,builder)) {
                    builder.build();

                    entry.getValue().registObserver(builder.getObserver(),builder.isHasSequence());
                }
            }
        }
    }

    /**
     * 注册状态观察者<br>
     * Modify : VLSimple2Develop_0.2.3 过时<br>
     * @param stateID 状态ID
     * @param observer 状态观察者
     * @param isActivateObserver 判断是否为活性状态观察者
     * @param hasSequence 判断是否使用消息序列管理类
     */
    @Deprecated
    private void registObserver(String stateID,Observer observer,boolean isActivateObserver,boolean hasSequence){
        synchronized (locker){
            if(!monitorStates.containsKey(stateID))
                return;

            observer.setActive(isActivateObserver);

            monitorStates.get(stateID).registObserver(observer,hasSequence);
        }
    }

    /**
     * 注册活性状态观察者<br>
     * 活性状态观察者：每次内部状态切换至运行状态时自动通知更新一次<br>
     * Modify : VLSimple2Develop_0.2.0 默认使用序列号管理类<br>
     * Modify : VLSimple2Develop_0.2.3 过时<br>
     * @param stateID 状态ID
     * @param observer 状态观察者
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void registActiviteObserver(String stateID,Observer observer)
    {
        registObserver(stateID,observer,true,true);
    }

    /**
     * 注册活性状态观察者，可主动设置是否使用消息序列管理类<br>
     * Modify : VLSimple2Develop_0.2.3 过时<br>
     * @since VLSimple2Develop_0.2.0
     * @param stateID 状态ID
     * @param observer 状态观察者
     * @param hasSequence 判断是否使用消息序列管理类
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void registActiviteObserver(String stateID,boolean hasSequence,Observer observer)
    {
        registObserver(stateID,observer,true,hasSequence);
    }

    /**
     * 注册惰性状态观察者<br>
     * 惰性状态观察者：只有状态改变时才会通知更新<br>
     * Modify : VLSimple2Develop_0.2.0 默认使用序列号管理类<br>
     * Modify : VLSimple2Develop_0.2.3 过时<br>
     * @param stateID 状态ID
     * @param observer 状态观察者
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void registInactiveObserver(String stateID,Observer observer)
    {
        registObserver(stateID,observer,false,true);
    }

    /**
     * 注册惰性状态观察者，可主动设置是否使用消息序列管理类<br>
     * Modify : VLSimple2Develop_0.2.3 过时<br>
     * @since VLSimple2Develop_0.2.0
     * @param stateID 状态ID
     * @param observer 状态观察者
     * @param hasSequence 判断是否使用消息序列管理类
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void registInactiveObserver(String stateID,boolean hasSequence,Observer observer)
    {
        registObserver(stateID,observer,false,hasSequence);
    }


    /*状态变更*/


    /**
     * 更改状态为相反状态<br>
     * @since VLSimple2Develop_0.1.4
     * @param stateID 状态ID
     * @param arg 额外的参数
     */
    public void changeStateAgainst(String stateID,Object... arg)
    {
        synchronized (locker)
        {
            if(!monitorStates.containsKey(stateID))
                return;

            monitorStates.get(stateID).changeStateAgainst(arg);
        }
    }

    /**
     * 根据状态ID改变状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param stateID 状态ID
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    public void changeState(String stateID,boolean state,Object... arg)
    {
        synchronized (locker)
        {
            if(!monitorStates.containsKey(stateID))
                return;

            monitorStates.get(stateID).changeState(state,arg);
        }
    }

    /**
     * 根据状态ID改变全局状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param stateID 状态ID
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    public static void changeGlobalState(String stateID,boolean state,Object... arg)
    {
        synchronized (globalStates){
            if(!globalStates.containsKey(stateID))
                return;

            globalStates.get(stateID).changeState(state, arg);
        }
    }

    /*获取状态，如果状态不存在则返回空*/

    /**
     * 根据状态ID获取状态
     * @param stateID 状态ID
     * @return 状态
     */
    public Boolean getState(String stateID)
    {
        synchronized (locker)
        {
            if(!monitorStates.containsKey(stateID))
                return null;

            return monitorStates.get(stateID).getState();
        }
    }




    /**
     * 根据状态ID获取全局状态
     * @param stateID 状态ID
     * @return 状态
     */
    public static Boolean getGlobalState(String stateID)
    {
        synchronized (globalStates)
        {
            if(!globalStates.containsKey(stateID))
                return null;

            return globalStates.get(stateID).getState();
        }
    }

}
