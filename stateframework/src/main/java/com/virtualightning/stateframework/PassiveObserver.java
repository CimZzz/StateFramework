package com.virtualightning.stateframework;

/**
 * Created by CimZzz on 16/7/22.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.2.3 将观察者运行环境作为可选项，其余方法标记为过时<br>
 * Description:<br>
 * 被动状态观察者<br>
 * 每次验证均返回true
 */
@SuppressWarnings("unused")
public abstract class PassiveObserver extends Observer {

    /**
     * @since VLSimple2Develop_0.2.3
     */
    public PassiveObserver() {}

    public PassiveObserver(boolean runWhenStop) {
        super(runWhenStop);
    }

    @Override
    boolean verify(boolean state) {
        return true;
    }

    @Override
    protected void trueStateUpdate(Object... arg) {

    }

    @Override
    protected void falseStateUpdate(Object... arg) {

    }
}
