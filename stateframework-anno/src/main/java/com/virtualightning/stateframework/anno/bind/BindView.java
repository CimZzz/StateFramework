package com.virtualightning.stateframework.anno.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 绑定视图
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface BindView {
    /**
     * @return 视图ID
     */
    int value();
}
