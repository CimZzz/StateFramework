package com.virtualightning.stateframework.utils;

import com.virtualightning.stateframework.http.RequestTransform;
import com.virtualightning.stateframework.state.AnnotationBinder;
import com.virtualightning.stateframework.state.NullBinder;

import java.util.HashMap;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.6 将无法找到绑定类而抛出的异常替换为返回空实现<br>
 * Modify : StateFrameWork_0.3.0 修复了内部类寻址问题<br>
 * Description:<br>
 * 绑定类查找器
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
                String className;
                if(objCls.isMemberClass())
                    className = objCls.getPackage().getName() + objCls.getSimpleName() + "$$$AnnotationBinder";
                else className = objCls.getName() + "$$$AnnotationBinder";
                Class findCls = Class.forName(className);

                binder = (AnnotationBinder) findCls.newInstance();
            } catch (ClassNotFoundException e) {
                return new NullBinder<>();
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
                String className;
                if(objCls.isMemberClass())
                    className = objCls.getPackage().getName() + objCls.getSimpleName() + "$$$RequestTransform";
                else className = objCls.getName() + "$$$RequestTransform";
                Class findCls = Class.forName(className);

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
