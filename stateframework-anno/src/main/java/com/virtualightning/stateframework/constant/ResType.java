package com.virtualightning.stateframework.constant;

/**
 * Created by CimZzz on 17/3/9.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public enum ResType {

    /**
     * 字符串
     */
    STRING("java.lang.String", "getString"),


    /**
     * 字符串
     */
    STRING_ARRAY("java.lang.String[]", "getStringArray"),


    /**
     * 尺寸
     */
    DIMENSION("float","getDimension");


    public final String TypeName;
    public final String MethodName;

    ResType(String typeName, String methodName) {
        TypeName = typeName;
        MethodName = methodName;
    }
}
