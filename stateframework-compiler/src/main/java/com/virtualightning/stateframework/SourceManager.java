package com.virtualightning.stateframework;

import java.util.HashMap;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class SourceManager<T> {
    private final HashMap<String,UniqueHashMap<Integer,T>> sourceMap;

    public SourceManager() {
        this.sourceMap = new HashMap<>();
    }

    public boolean putSource(String className,Integer key,T t) {
        UniqueHashMap<Integer,T> uniqueHashMap = sourceMap.get(className);
        if(uniqueHashMap == null)
            this.sourceMap.put(className,uniqueHashMap = new UniqueHashMap<>());


        return uniqueHashMap.put(key, t);
    }

    public UniqueHashMap<Integer,T> getUniqueHashMap(String className) {
        return sourceMap.get(className);
    }
}
