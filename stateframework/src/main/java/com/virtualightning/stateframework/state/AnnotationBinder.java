package com.virtualightning.stateframework.state;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public interface AnnotationBinder<T> {
    void bindState(T source,StateRecord stateRecord);
    void bindView(T source,View view);
    void bindResources(T source, Resources resources);
    void bindEvent(T source,View view);
}
