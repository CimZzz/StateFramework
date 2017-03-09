package com.virtualightning.stateframework.state;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public abstract class PacketObserver<T> extends BaseObserver {
    protected T packetElem;


    public PacketObserver(T packetElem) {
        this.packetElem = packetElem;
    }

    public abstract void doNotify(Object... notify);
}
