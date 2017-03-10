package com.virtualightning.stateframework.anno.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * URL路径参数,只适用于注解的方式生成RequestBody,示例如下:<br>
 * http://localhost/?ss={$}&amp;s2={$}<br>
 * 可以通过此注解填充URL形成完整的路径,只能修饰String类型的属性
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface VLUrlParams {
    /**
     * @return URL路径参数下标
     */
    int value();
}
