package com.virtualightning.stateframework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.virtualightning.stateframework.state.AnnotationBinder;
import com.virtualightning.stateframework.state.StateRecord;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.5<br>
 * Modify : StateFrameWork_0.1.5<br>
 * Description:<br>
 * 注解解析类
 */
@SuppressWarnings("unused")
public class Analyzer {


    /**
     * 绑定状态观察者
     * @param source 绑定目标
     * @param stateRecord 状态记录
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeState(T source,StateRecord stateRecord) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        stateBinder.bindState(source,stateRecord);
    }


    /**
     * 绑定视图
     * @param source 绑定目标
     * @param <T> 绑定目标类型基类为Activity
     */
    public static <T extends Activity> void analyzeView(T source) {
        analyzeView(source,source.getWindow().getDecorView());
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
     * @param <T> 绑定目标类型基类为Activity
     */
    public static <T extends Activity> void analyzeEvent(T source) {
        analyzeEvent(source,source.getWindow().getDecorView());
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


    /**
     * 绑定资源
     * @param source 绑定目标
     * @param <T> 绑定目标类型基类为Activity
     */
    public static <T extends Activity> void analyzeResources(T source) {
        analyzeResources(source,source.getResources());
    }


    /**
     * 绑定资源
     * @param source 绑定目标
     * @param context 上下文
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeResources(T source, Context context) {
        analyzeResources(source,context.getResources());
    }


    /**
     * 绑定资源
     * @param source 绑定目标
     * @param resources 资源访问类
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeResources(T source, Resources resources) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        stateBinder.bindResources(source,resources);
    }

    /**
     * 绑定全部注解
     * @param source 绑定目标
     * @param stateRecord 状态记录
     * @param <T> 绑定目标类型基类为Activity
     */
    public static <T extends Activity> void analyzeAll(T source,StateRecord stateRecord) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        stateBinder.bindView(source,source.getWindow().getDecorView());
        stateBinder.bindResources(source,source.getResources());
        stateBinder.bindEvent(source,source.getWindow().getDecorView());
        if(stateRecord != null)
            stateBinder.bindState(source,stateRecord);
    }

    /**
     *
     * 绑定全部注解
     * @param source 绑定目标
     * @param rootView 根视图
     * @param stateRecord 状态记录
     * @param resources 资源访问类
     * @param <T> 绑定目标类型
     */
    public static <T> void analyzeAll(T source,View rootView,Resources resources,StateRecord stateRecord) {
        AnnotationBinder<T> stateBinder = FindUtils.findBinderClassByObject(source);
        if(resources != null)
            stateBinder.bindResources(source,resources);
        if(rootView != null) {
            stateBinder.bindView(source, rootView);
            stateBinder.bindEvent(source, rootView);
        }
        if(stateRecord != null)
            stateBinder.bindState(source,stateRecord);
    }
}
