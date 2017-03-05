package com.virtualightning.stateframework.anno;

/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 请求头部注解
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface VLHeader {
    /**
     * @return 请求头部Key
     */
    String value();
}
