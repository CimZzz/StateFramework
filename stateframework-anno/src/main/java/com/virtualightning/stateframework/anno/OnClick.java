package com.virtualightning.stateframework.anno;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 绑定视图点击事件
 */
public @interface OnClick {
    /**
     * @return 视图ID
     */
    int[] value();

    /**
     * 设置接口方法描述<br>
     * 如果一些视图设置接口方法不一致的话，可以自定义设置其方法
     * @return 接口方法描述
     */
    String listenerDesc() default "setOnClickListener";
}
