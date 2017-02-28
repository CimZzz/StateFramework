package com.virtualightning.stateframework;

import java.util.HashMap;

/**
 * Created by CimZzz on ${Date}.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class EnclosingSet {
    private static HashMap<String,EnclosingSet> setMap;

    static {
        setMap = new HashMap<>();
    }

    public static EnclosingSet getEnclosingSet(String name) {
        return setMap.get(name);
    }

    public static void putEnclosingSet(String name,EnclosingSet set) {
        setMap.put(name,set);
    }



    private String packageName;
    
}
