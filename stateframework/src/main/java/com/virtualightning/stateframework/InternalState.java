package com.virtualightning.stateframework;

import java.io.Serializable;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.6 添加了销毁状态和初始化状态，当状态处于销毁状态时不会改变状态<br>
 * Description:<br>
 * 状态记录的内部状态
 */
@SuppressWarnings("unused")
public final class InternalState implements Serializable {
    public static final int INTERNAL_STATE_CREATE = 0;
    public static final int INTERNAL_STATE_RUN = 1;
    public static final int INTERNAL_STATE_STOP = 2;
    public static final int INTERNAL_STATE_DESTORY = 3;

    private int internalState;

    /*构造时设定默认状态*/

    /**
     * 构造函数，默认状态为{@link #INTERNAL_STATE_CREATE}（创建状态）
     */
    InternalState()
    {
        internalState = INTERNAL_STATE_CREATE;
    }

    /*状态的设置与判断*/

    /**
     * 判断当前的内部状态是否处于运行状态。
     * @return 如果当前内部状态为{@link #INTERNAL_STATE_RUN}时返回true
     */
    boolean isRunState()
    {
        return internalState == INTERNAL_STATE_RUN;
    }

    /**
     * 判断当前的内部状态是否处于创建状态。
     * @since : VLSimple2Develop_0.1.6
     * @return 如果当前内部状态为{@link #INTERNAL_STATE_CREATE}时返回true
     */
    boolean isCreateState()
    {
        return internalState == INTERNAL_STATE_CREATE;
    }

    /**
     * 判断当前的内部状态是否处于销毁状态。
     * @since : VLSimple2Develop_0.1.6
     * @return 如果当前内部状态为{@link #INTERNAL_STATE_DESTORY}时返回true
     */
    boolean isDestroyState()
    {
        return internalState == INTERNAL_STATE_DESTORY;
    }

    /**
     * 获取当前的内部状态ID
     * @since : VLSimple2Develop_0.1.6
     * @return 返回当前状态的状态ID
     */
    int getCurrnetInternalState()
    {
        return internalState;
    }


    /**
     * 设置内部状态<br>
     * Modify : VLSimple2Develop_0.1.6 当处于销毁状态时不会改变状态<br>
     * @param internalState 内部状态
     */
    public void setInternalState(int internalState) {
        this.internalState = this.internalState != INTERNAL_STATE_DESTORY ? internalState : INTERNAL_STATE_DESTORY;
    }
}
