package com.virtualightning.stateframework.http;

/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 请求转换接口
 */
public interface RequestTransform<T> {
    Request transferData(T rawData);
}
