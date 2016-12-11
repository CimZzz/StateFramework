package com.virtualightning.stateframework;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.4 添加切换相反状态方法<br>
 * Modify : VLSimple2Develop_0.2.1 修复了对于状态中介的列表的同步问题<br>
 * Description:<br>
 * 状态对象
 */
@SuppressWarnings("unused")
public final class State implements Serializable {
    private boolean state;
    private List<WeakReference<StateMediator>> mediatorReferences;
    private ListIterator<WeakReference<StateMediator>> mediatorIterator;

    State(boolean state)
    {
        this.state = state;
        mediatorReferences = new LinkedList<>();
    }

    /*多个中介者同时共享一个状态对象，保证状态的一致性*/

    /**
     * 添加中介者至状态对象
     * Modify : VLSimple2Develop_0.2.1 判断当前是否处于迭代状态，如果是，则会通过列表迭代器添加；如果否，则直接添加至列表<br>
     * @param mediator 中介者
     */
    void addMediator(StateMediator mediator)
    {
        synchronized (this) {
            if(mediatorIterator != null)
                mediatorIterator.add(new WeakReference<>(mediator));
            else mediatorReferences.add(new WeakReference<>(mediator));
        }
    }

    /*获取状态*/

    /**
     * 获取状态
     * @return 返回状态
     */
    boolean getState()
    {
        return state;
    }


    /*状态变更*/

    /**
     * 更改状态为相反状态
     * @since VLSimple2Develop_0.1.4
     * @param arg 额外的参数
     */
    void changeStateAgainst(Object... arg)
    {
        synchronized (this)
        {
            this.state = !state;

            notifyMediator(arg);
        }
    }

    /**
     * 改变状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    void changeState(boolean state,Object... arg)
    {
        this.state = state;

        notifyMediator(arg);
    }


    /*通知中介进行观察者更新，并在更新的过程中删除被回收的实例引用*/

    /**
     * 通知中介进行观察者更新
     * Modify : VLSimple2Develop_0.2.1 每次产生迭代器时都会将其记录为最新迭代器，当最新的迭代器完成迭代后设置为空<br>
     * @param arg 额外的参数
     */
    private void notifyMediator(Object... arg)
    {
        ListIterator<WeakReference<StateMediator>> iterator;

        /*获取列表迭代器*/
        synchronized (this) {
            iterator = mediatorReferences.listIterator();
            this.mediatorIterator = iterator;
        }

        while (iterator.hasNext()) {
            WeakReference<StateMediator> mediatorReference = iterator.next();
            final StateMediator mediator = mediatorReference.get();

            if (mediator == null) {
                iterator.remove();
                continue;
            }

            mediator.notifyObserver(true, arg);
        }

        /*判断当前的列表迭代器是否为最新的列表迭代器，如果是则表示全部迭代完成，将列表迭代器*/
        synchronized (this)
        {
            if(iterator == this.mediatorIterator)
                this.mediatorIterator = null;
        }
    }
}
