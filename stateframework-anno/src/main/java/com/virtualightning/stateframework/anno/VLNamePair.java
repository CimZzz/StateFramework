package com.virtualightning.stateframework.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * POST数据键值对,会自动调用其toString()方法
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface VLNamePair {
    String value();
}
