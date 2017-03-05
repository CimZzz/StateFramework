package com.virtualightning.stateframework.utils;

import com.virtualightning.stateframework.http.RequestTransform;
import com.virtualightning.stateframework.state.AnnotationBinder;

import java.util.HashMap;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public final class FindUtils {
    private static final HashMap<Class,AnnotationBinder> binderMap;
    private static final HashMap<Class,RequestTransform> transferMap;

    static {
        binderMap = new HashMap<>();
        transferMap = new HashMap<>();
    }

    public static <T> AnnotationBinder<T> findBinderClassByObject(T obj) {
        Class objCls = obj.getClass();
        AnnotationBinder binder = binderMap.get(objCls);

        if(binder == null) {
            try {
                Class findCls = Class.forName(objCls.getName() + "$$$AnnotationBinder");

                binder = (AnnotationBinder) findCls.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("未能找到合适的 AnnotationBinder , 查找类为 " + objCls.getName(),e);
            } catch (InstantiationException e) {
                throw new RuntimeException("AnnotationBinder 初始化错误, 查找类为 " + objCls.getName(),e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("AnnotationBinder 初始化权限不足, 查找类为 " + objCls.getName(),e);
            }
        }


        return (AnnotationBinder<T>) binder;
    }

    public static <T> RequestTransform<T> findTransferClassByObject(T obj) {
        Class objCls = obj.getClass();
        RequestTransform transform = transferMap.get(objCls);

        if(transform == null) {
            try {
                Class findCls = Class.forName(objCls.getName() + "$$$RequestTransform");

                transform = (RequestTransform) findCls.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("未能找到合适的 RequestTransform , 查找类为 " + objCls.getName(),e);
            } catch (InstantiationException e) {
                throw new RuntimeException("RequestTransform 初始化错误, 查找类为 " + objCls.getName(),e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("RequestTransform 初始化权限不足, 查找类为 " + objCls.getName(),e);
            }
        }


        return (RequestTransform<T>) transform;
    }
}
