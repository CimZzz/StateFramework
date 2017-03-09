package com.virtualightning.stateframework.state;

import com.virtualightning.stateframework.state.reference.PassiveRef;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.3<br>
 * Description:<br>
 * 全局状态池
 */
public class StaticStatePool {
    private final HashMap<String,StaticStateWrapper> staticStateMap;
    private final HashMap<Class,Map<String,PassiveRef<StateWrapper>>> stateWrapperRefMap;
    static final StaticStatePool staticPool = new StaticStatePool();

    StaticStatePool() {
        this.staticStateMap = new HashMap<>();
        this.stateWrapperRefMap = new HashMap<>();
    }


    /**
     * 注册全局状态观察者，如果已经存在相同的状态观察者，则会进行覆盖操作
     * @param stateId 状态ID
     * @param clsKey 类键
     * @param stateWrapper 状态包装器
     */
    void registerWholeState(String stateId,Class clsKey,StateWrapper stateWrapper) {
        PassiveRef<StateWrapper> passiveRef = new PassiveRef<>(stateWrapper);

        StaticStateWrapper staticWrapper = null;
        synchronized (staticStateMap) {
            staticWrapper = staticStateMap.get(stateId);
            if (staticWrapper == null)
                staticStateMap.put(stateId, staticWrapper = new StaticStateWrapper());

            synchronized (stateWrapperRefMap) {
                Map<String,PassiveRef<StateWrapper>> subRefMap = stateWrapperRefMap.get(clsKey);

                if(subRefMap == null)
                    stateWrapperRefMap.put(clsKey,subRefMap = new HashMap<>());

                /*如果之前存在监控此状态的StateWrapper，则清除*/
                PassiveRef<StateWrapper> previousRef = subRefMap.put(stateId,passiveRef);
                if(previousRef != null)
                    previousRef.clear();
            }

            staticWrapper.putStateWrapper(clsKey, passiveRef);
        }
    }

    /**
     * 注销全局状态观察者
     * @param clsKey 类键
     */
    void unregisterWholeState(Class clsKey) {
        synchronized (stateWrapperRefMap) {
            Map<String,PassiveRef<StateWrapper>> subRefMap = stateWrapperRefMap.get(clsKey);

            if(subRefMap != null) {
                for(PassiveRef<StateWrapper> stateWrapperRef : subRefMap.values())
                    stateWrapperRef.clear();

                subRefMap.clear();

                stateWrapperRefMap.remove(clsKey);

                System.gc();
            }
        }
    }


    /*唤醒状态观察者*/

    void notifyWholeState(Class clsKey,String stateId,Object... args) {
        StaticStateWrapper staticStateWrapper = null;

        synchronized (staticStateMap) {
            staticStateWrapper = staticStateMap.get(stateId);
        }

        if (staticStateWrapper == null)
            return;

        staticStateWrapper.notifyWholeState(clsKey,args);
    }
}
