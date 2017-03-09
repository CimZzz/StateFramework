package com.virtualightning.stateframework.anno;

import com.virtualightning.stateframework.constant.ResType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Modify : StateFrameWork_0.1.3 添加了资源类型属性<br>
 * Description:<br>
 * 绑定资源
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface BindResources {
    /**
     * @return 资源ID
     */
    int resId();

    /**
     * @return 资源类型
     */
    ResType type();
}
