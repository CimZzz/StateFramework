package com.virtualightning.stateframework.anno.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.7 允许此注解修饰GET方法<br>
 * Modify : StateFrameWork_0.2.5 添加URL编码选项<br>
 * Description:<br>
 * POST数据键值对,会自动调用其toString()方法<br>
 * GET方法使用时会自动添加至url尾部
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface VLNamePair {
    /**
     * @return NamePair键值
     */
    String value();

    /**
     *
     * @return 是否进行URLEncode编码
     */
    boolean allowEncode() default true;
}
