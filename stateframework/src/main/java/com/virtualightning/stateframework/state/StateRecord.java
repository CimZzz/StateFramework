package com.virtualightning.stateframework.state;

import com.virtualightning.stateframework.constant.RunType;
import com.virtualightning.stateframework.state.wrappers.ImmediatelyWrapper;
import com.virtualightning.stateframework.state.wrappers.MainLoopSeqWrapper;
import com.virtualightning.stateframework.state.wrappers.MainLoopWrapper;
import com.virtualightning.stateframework.state.wrappers.ThreadPoolWrapper;

import java.util.HashMap;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.3 添加全局状态操作的若干方法<br>
 * Description:<br>
 * 状态记录者
 */
@SuppressWarnings("unused")
public final class StateRecord {
    private final HashMap<String,StateWrapper> internalMap;
    private final InnerState innerState;
    private final Class clsKey;

    StateRecord(Class clsKey) {
        this.clsKey = clsKey;

        this.internalMap = new HashMap<>();

        innerState = new InnerState();
    }

    /*实例方法*/

    /**
     * 生成状态记录者,绑定ClsKey
     * @param clsKey 类键
     * @return 状态记录者
     */
    public static StateRecord newInstance(Class clsKey) {
        return new StateRecord(clsKey);
    }


    /*注册观察者方法*/

    /**
     * 注册内部状态
     * @param builder 状态构建者
     */
    public void registerObserver (ObserverBuilder builder) {
        BaseObserver observer = builder.build();

        String stateId = builder.getStateId();

        synchronized (internalMap) {
            if(internalMap.containsKey(stateId))
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

            internalMap.put(stateId,stateWrapper);
        }
    }

    /**
     * 注册全局状态
     * @param builder 状态构建者
     */
    public void registerWholeObserver(ObserverBuilder builder) {
        BaseObserver observer = builder.build();

        String stateId = builder.getStateId();


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


        StaticStatePool.staticPool.registerWholeState(stateId,clsKey,stateWrapper);
    }

    /**
     * 注销全局状态
     */
    public void unregisterObserver() {
        internalMap.clear();
        StaticStatePool.staticPool.unregisterWholeState(clsKey);
    }

    /*唤醒状态观察者方法*/

    /**
     * 唤醒内部状态观察者
     * @param stateId 状态ID
     * @param args 唤醒代价
     */
    public void notifyState(String stateId,Object... args) {
        StateWrapper stateWrapper = internalMap.get(stateId);

        if(stateWrapper == null)
            return;

        stateWrapper.notify(args);
    }

    /**
     * 唤醒全局状态观察者
     * @param stateId 状态ID
     * @param args 唤醒代价
     */
    public static void notifyWholeState(String stateId,Object... args) {
        notifyWholeState(null,stateId,args);
    }

    /**
     * 唤醒指定类键的全局状态观察者
     * @param clsKey 类键
     * @param stateId 状态ID
     * @param args 唤醒代价
     */
    public static void notifyWholeState(Class clsKey,String stateId,Object... args) {
        StaticStatePool.staticPool.notifyWholeState(clsKey,stateId,args);
    }



    /*设置运行状态*/

    public boolean isStop() {
        return innerState.isStop();
    }

    public void setRecordState(boolean isRun) {
        innerState.setStop(!isRun);
    }
}
