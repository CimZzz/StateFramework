package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

import java.lang.ref.WeakReference;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.2 添加了内部处理{@link #notify(Object...)} 的方法<br>
 * Description:<br>
 * 弱引用状态观察者
 */
@SuppressWarnings("unused")
public class WeakObserverRef implements ObserverReference{
    private final WeakReference<BaseObserver> observer;

    public WeakObserverRef(BaseObserver observer) {
        this.observer = new WeakReference<>(observer);
    }

    @Override
    public BaseObserver getObserver() {
        return observer.get();
    }

    @Override
    public void notify(Object... args) {
        BaseObserver baseObserver = observer.get();

        if(baseObserver != null)
            baseObserver.notify(args);
    }
}
