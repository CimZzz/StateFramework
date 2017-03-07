package com.virtualightning.stateframework.state;

import android.view.View;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public interface AnnotationBinder<T> {
    void bindState(StateRecord stateRecord, T source);
    void bindView(T source,View view);
    void bindEvent(T source,View view);
}
