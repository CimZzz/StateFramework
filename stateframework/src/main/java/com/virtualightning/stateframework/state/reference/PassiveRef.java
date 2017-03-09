package com.virtualightning.stateframework.state.reference;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class PassiveRef<T> {
    private T object;

    public PassiveRef(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }

    public void clear() {
        this.object = null;
    }
}
