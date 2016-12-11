package com.virtualightning.stateframework;

/**
 * Created by CimZzz on 16/7/22.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.2.3 将观察者运行环境作为可选项，其余方法标记为过时<br>
 * Description:<br>
 * 记忆状态观察者<br>
 * 只有当更新的状态与内部的旧状态不一致时才会执行更新
 */
@SuppressWarnings("unused")
public abstract class MemoryObserver extends Observer{
    private Boolean oldState;

    /**
     * @since VLSimple2Develop_0.2.3
     */
    public MemoryObserver() {}

    public MemoryObserver(boolean runWhenStop) {
        super(runWhenStop);
    }

    @Override
    final synchronized boolean verify(boolean state) {
        boolean change = oldState == null || !oldState.equals(state);

        oldState = state;

        return change;
    }

    @Override
    protected void trueStateUpdate(Object... arg) {

    }

    @Override
    protected void falseStateUpdate(Object... arg) {

    }
}
