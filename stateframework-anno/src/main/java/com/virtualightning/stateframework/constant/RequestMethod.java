package com.virtualightning.stateframework.constant;

/**
 * Created by CimZzz on 17/3/6.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public enum RequestMethod {

    /**
     * POST方法
     */
    POST("POST"),



    /**
     * GET方法
     */
    GET("GET");


    /**
     * 请求方法值
     */
    public final String Value;

    RequestMethod(String str) {
        this.Value = str;
    }
}
