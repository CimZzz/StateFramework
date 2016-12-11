package com.virtualightning.stateframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 16/6/26.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Description:<br>
 * 监控内部状态注解
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnalyzeState {
    /**
     * 初始化状态名数组
     * @return 状态名数组
     */
    String[] stateNames() default {};

    /**
     * 初始化状态数组
     * @return 状态数组
     */
    boolean[] states() default {};
}
