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
     * @return 观察者监视状态ID
     */
    String stateId();

    /**
     * @return 观察者是否允许被停止
     */
    boolean allowStop() default false;

    /**
     * @return {@link RunType} 观察者运行类型
     */
    int runType() default RunType.MAIN_LOOP;

    /**
     * 是否为自变长参数<br>
     * 如果为是，则方法的参数只能为 Object[];如果为否，参数会自动转型，但是如果参数长度不对或类型不匹配则会抛出异常
     * @return 是否为自变长参数
     */
    boolean isVarParameters() default true;
}
