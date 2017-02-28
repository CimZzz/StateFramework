package com.virtualightning.stateframework.anno;

import com.virtualightning.stateframework.constant.ReferenceType;
import com.virtualightning.stateframework.constant.RunType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 绑定观察者注解，通过注解预编译成代码
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@SuppressWarnings("unused")
public @interface BindObserver {

    /**
     * 观察者监视状态ID
     */
    String stateId();

    /**
     * 观察者是否允许被停止
     */
    boolean allowStop() default false;

    /**
     * {@link RunType} 观察者运行类型
     */
    int runType() default RunType.MAIN_LOOP;

    /**
     * {@link ReferenceType} 观察者引用持有类型
     */
    int refType() default ReferenceType.WEAK;
}
