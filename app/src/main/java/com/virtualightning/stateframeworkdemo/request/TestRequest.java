package com.virtualightning.stateframeworkdemo.request;

import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.constant.HTTPMethodType;


/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
@BindHTTPRequest(
        url = "http://192.168.1.100/StormLive-PHP/index.php",
        method = HTTPMethodType.POST
)
public class TestRequest {
}
