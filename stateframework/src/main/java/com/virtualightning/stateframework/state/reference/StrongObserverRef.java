package com.virtualightning.stateframework.state.reference;

import com.virtualightning.stateframework.state.BaseObserver;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.2 添加了内部处理{@link #notify(Object...)} 的方法<br>
 * Description:<br>
 * 强引用状态观察者
 */
@SuppressWarnings("unused")
public class StrongObserverRef implements ObserverReference{
    private final BaseObserver observer;

    public StrongObserverRef(BaseObserver observer) {
        this.observer = observer;
    }

    @Override
    public BaseObserver getObserver() {
        return observer;
    }

    @Override
    public void notify(Object... args) {
        observer.notify(args);
    }
}
