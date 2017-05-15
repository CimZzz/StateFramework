package com.virtualightning.stateframework.anno.http;

import com.virtualightning.stateframework.constant.Charset;
import com.virtualightning.stateframework.constant.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.2.5 添加URL编码选项<br>
 * Description:<br>
 * 绑定HTTP请求注解<br>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@SuppressWarnings("unused")
public @interface BindHTTPRequest {
    /**
     * @return URL路径<br>
     * 可以配合 {@link VLUrlParams}组合路径
     */
    String url();

    /**
     * @return {@link RequestMethod} HTTP 请求方法<br>
     */
    RequestMethod method() default RequestMethod.GET;

    /**
     * @return HTTP 请求编码
     */
    Charset charset() default Charset.UTF_8;

    /**
     * @return 是否需要URLEncode
     */
    boolean urlEncode() default true;
}
