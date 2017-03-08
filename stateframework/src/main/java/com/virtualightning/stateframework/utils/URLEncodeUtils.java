package com.virtualightning.stateframework.utils;

import com.virtualightning.stateframework.constant.Charset;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * URL编码工具
 */
public class URLEncodeUtils {

    public static String encodeContent(String source, Charset charset) {
        try {
            return URLEncoder.encode(source,charset.Value);
        } catch (UnsupportedEncodingException e) {
            return source;
        }
    }
}
