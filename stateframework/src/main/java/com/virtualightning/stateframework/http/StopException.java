package com.virtualightning.stateframework.http;

/**
 * Created by CimZzz on 17/5/24.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.2.9<br>
 * Description:<br>
 * Description
 */
public class StopException extends Exception {
    StopException() {
        super("连接被强制关闭");
    }
    StopException(Throwable e) {
        super("连接被强制关闭",e);
    }
}
