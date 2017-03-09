package com.virtualightning.stateframework.state;

import com.virtualightning.stateframework.state.reference.PassiveRef;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.3<br>
 * Description:<br>
 *
 */
public class StaticStateWrapper {
    private final ConcurrentHashMap<Class,PassiveRef<StateWrapper>> stateWrapperRefMap;

    public StaticStateWrapper() {
        this.stateWrapperRefMap = new ConcurrentHashMap<>();
    }

    void putStateWrapper(Class clsKey,PassiveRef<StateWrapper> wrapperRef) {
        synchronized (this) {
            stateWrapperRefMap.put(clsKey, wrapperRef);
        }
    }

    public void notifyWholeState(Class clsKey, Object... args) {
        if(clsKey == null) {
            Iterator<PassiveRef<StateWrapper>> iterator = stateWrapperRefMap.values().iterator();
            while (iterator.hasNext()) {
                PassiveRef<StateWrapper> ref = iterator.next();

                StateWrapper stateWrapper = ref.get();

                if(stateWrapper == null) {
                    iterator.remove();
                    continue;
                }

                stateWrapper.notify(args);
            }
        } else {
            PassiveRef<StateWrapper> ref = stateWrapperRefMap.get(clsKey);
            StateWrapper stateWrapper = ref.get();

            if(stateWrapper != null)
                stateWrapper.notify(args);
        }
    }
}
