package com.virtualightning.stateframework;

/**
 * Created by CimZzz on 11/16/16.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.2.3<br>
 * Description:<br>
 * 状态线程池执行类
 */
public class StateRunnable implements Runnable{
    private final StateMediator mediator;
    private final Object[] args;

    StateRunnable(StateMediator mediator, Object[] args) {
        this.mediator = mediator;
        this.args = args;
    }

    @Override
    public void run() {
        this.mediator.updateObserver(args);
    }
}
