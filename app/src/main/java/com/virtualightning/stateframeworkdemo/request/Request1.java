package com.virtualightning.stateframeworkdemo.request;

import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.constant.RequestMethod;
import com.virtualightning.stateframeworkdemo.http.HTTP;


/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@BindHTTPRequest(
        url = HTTP.HTTP1,
        method = RequestMethod.GET
)
public class Request1 {
}
