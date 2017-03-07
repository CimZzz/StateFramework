package com.virtualightning.stateframework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class UniqueHashMap<K,V> {
    private HashMap<K,V> subMap;

    public UniqueHashMap() {
        subMap = new HashMap<>();
    }

    public boolean put(K k,V v) {
        if(subMap.containsKey(k))
            return false;

        subMap.put(k,v);
        return true;
    }

    public Collection<V> values() {
        return subMap.values();
    }

    public Set<Map.Entry<K,V>> entrySet() {
        return subMap.entrySet();
    }

    public int size() {
        return subMap.size();
    }
}
