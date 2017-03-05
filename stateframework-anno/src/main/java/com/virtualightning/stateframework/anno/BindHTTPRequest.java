package com.virtualightning.stateframework.anno;

import com.virtualightning.stateframework.constant.HTTPMethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 绑定HTTP请求注解<br>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@SuppressWarnings("unused")
public @interface BindHTTPRequest {
    /**
     * URL路径<br>
     * 可以配合 {@link VLUrlParams}组合路径
     */
    String url();

    /**
     * HTTP 请求方法<br>
     * 参考{@link HTTPMethodType} 请求方法类型
     */
    String method() default HTTPMethodType.POST;

    /**
     * HTTP 请求编码
     */
    String charset() default "UTF-8";
}
