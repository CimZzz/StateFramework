package com.virtualightning.stateframeworkdemo.request;

import com.virtualightning.stateframework.anno.http.BindHTTPRequest;
import com.virtualightning.stateframework.anno.http.VLNamePair;
import com.virtualightning.stateframework.anno.http.VLUrlParams;
import com.virtualightning.stateframework.constant.RequestMethod;


/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@BindHTTPRequest(
        url = "{$}/HelloWorld",
        method = RequestMethod.POST
)
public class Request1 {
    @VLUrlParams(0)
    public String host;
    @VLNamePair("uid")
    public Integer uid;
}
