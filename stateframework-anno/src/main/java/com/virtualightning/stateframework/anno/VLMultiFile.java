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
 * POST文件上传<br>
 * 只能修饰File类型的属性
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface VLMultiFile {
    /**
     * MultiFile键值
     */
    String name();

    /**
     * MultiFile文件名
     */
    String fileName();

    /**
     * MultiFile文件类型
     */
    String contentType();
}
