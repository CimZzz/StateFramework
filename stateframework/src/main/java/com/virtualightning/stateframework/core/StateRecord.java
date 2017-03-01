package com.virtualightning.stateframework.core;

import com.virtualightning.stateframework.constant.RunType;
import com.virtualightning.stateframework.utils.FindUtils;
import com.virtualightning.stateframework.wrappers.ImmediatelyWrapper;
import com.virtualightning.stateframework.wrappers.MainLoopSeqWrapper;
import com.virtualightning.stateframework.wrappers.MainLoopWrapper;
import com.virtualightning.stateframework.wrappers.ThreadPoolWrapper;

import java.util.HashMap;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 状态记录者
 */
@SuppressWarnings("unused")
public final class StateRecord {
    private final HashMap<String,StateWrapper> observerMap;
    private final InnerState innerState;


    public StateRecord() {
        this.observerMap = new HashMap<>();

        innerState = new InnerState();
    }



    /*注册观察者方法*/

    public <T> void registerByAnnotation(T object) {
        StateBinder<T> stateBinder = FindUtils.findBinderClassByObject(object);
        stateBinder.bindAnnotation(this,object);
    }


    public void registerByBuilder(ObserverBuilder builder) {
        BaseObserver observer = builder.build();

        String stateId = builder.getStateId();

        synchronized (this) {
            if(observerMap.containsKey(stateId))
                throw new RuntimeException("不能重复监控同一状态.状态ID : " + stateId);

            StateWrapper stateWrapper = null;
            switch (observer.runType) {
                case RunType.MAIN_LOOP:
                    stateWrapper = new MainLoopWrapper(observer,innerState);
                    break;

                case RunType.MAIN_LOOP_SEQ:
                    stateWrapper = new MainLoopSeqWrapper(observer,innerState);
                    break;

                case RunType.CURRENT:
                    stateWrapper = new ImmediatelyWrapper(observer,innerState);
                    break;

                case RunType.THREAD_POOL:
                    stateWrapper = new ThreadPoolWrapper(observer,innerState);
                    break;
            }

            if(stateWrapper == null)
                throw new RuntimeException("不合法的运行方式.运行方式 : " + observer.runType);

            observerMap.put(stateId,stateWrapper);
        }
    }


    public void notifyState(String stateId,Object... args) {
        StateWrapper stateWrapper = observerMap.get(stateId);

        if(stateWrapper == null)
            return;

        stateWrapper.notify(args);
    }



    /*设置运行状态*/

    public void setRecordState(boolean isRun) {
        innerState.setStop(!isRun);
    }
}
