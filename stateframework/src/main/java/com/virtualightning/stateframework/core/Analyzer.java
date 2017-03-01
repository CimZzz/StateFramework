package com.virtualightning.stateframework.core;

import com.virtualightning.stateframework.utils.FindUtils;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.5<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public class Analyzer {

    public static <T> void analyzeState(StateRecord stateRecord,T object) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(object);
        stateBinder.bindState(stateRecord,object);
    }


    public static <T> void analyzeView(T object) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(object);
        stateBinder.bindView(object);
    }
}
