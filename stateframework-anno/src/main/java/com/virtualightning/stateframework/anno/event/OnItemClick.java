package com.virtualightning.stateframework.anno.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@SuppressWarnings("unused")
public @interface OnItemClick {
    /**
     * @return 视图ID
     */
    int[] value();

    /**
     * 设置接口方法描述<br>
     * 如果一些视图设置接口方法不一致的话，可以自定义设置其方法
     * @return 接口方法描述
     */
    String listenerDesc() default "setOnItemClickListener";
}
