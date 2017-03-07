package com.virtualightning.stateframework.utils;

import android.view.View;

import com.virtualightning.stateframework.state.AnnotationBinder;
import com.virtualightning.stateframework.state.StateRecord;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.5<br>
 * Description:<br>
 * 注解解析类
 */
@SuppressWarnings("unused")
public class Analyzer {

    /**
     * 绑定状态观察者
     * @param stateRecord 状态记录
     * @param source 绑定目标
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeState(StateRecord stateRecord, T source) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        stateBinder.bindState(stateRecord,source);
    }

    /**
     * 绑定视图
     * @param source 绑定目标
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeView(T source) {
        analyzeView(source,null);
    }

    /**
     * 绑定视图，提供根视图作为绑定源
     * @param source 绑定目标
     * @param rootView 根视图
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeView(T source,View rootView) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        stateBinder.bindView(source,rootView);
    }


    /**
     * 绑定视图事件
     * @param source 绑定目标
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeEvent(T source) {
        analyzeEvent(source,null);
    }

    /**
     * 绑定视图事件，提供根视图作为绑定源
     * @param source 绑定目标
     * @param rootView 根视图
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeEvent(T source,View rootView) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        stateBinder.bindEvent(source,rootView);
    }

}
