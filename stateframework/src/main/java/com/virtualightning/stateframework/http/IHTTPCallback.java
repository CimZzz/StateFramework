package com.virtualightning.stateframework.http;

import java.io.IOException;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 *
 */
@SuppressWarnings("unused")
public interface IHTTPCallback {
    void onSuccess(Response response) throws IOException;
    void onFailure(Exception e);
}
