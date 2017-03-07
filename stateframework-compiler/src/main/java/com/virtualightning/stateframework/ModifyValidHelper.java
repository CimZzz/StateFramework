package com.virtualightning.stateframework;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 修饰符验证帮助类
 */
public class ModifyValidHelper {
    private final Set<Modifier> mustContainSet;
    private final Set<Modifier> banContainSet;

    public ModifyValidHelper() {
        mustContainSet = new HashSet<>();
        banContainSet = new HashSet<>();
    }

    public ModifyValidHelper addMustContain(Modifier modifier) {
        if(banContainSet.contains(modifier))
            throw new RuntimeException("设置为必须使用的修饰符存在于禁止修饰符集合中");
        else {
            mustContainSet.add(modifier);
        }

        return this;
    }

    public ModifyValidHelper addBanContain(Modifier modifier) {
        if(mustContainSet.contains(modifier))
            throw new RuntimeException("设置为禁止修饰符存在于必须使用的修饰符集合中");
        else {
            banContainSet.add(modifier);
        }

        return this;
    }

    public boolean valid(Set<Modifier> modifierSet) {
        boolean flag = modifierSet.containsAll(mustContainSet);

        if(flag) {
            for(Modifier modifier : banContainSet)
                if(modifierSet.contains(modifier)) {
                    flag = false;
                    break;
                }
        }


        return flag;
    }

    public void clear() {
        mustContainSet.clear();
        banContainSet.clear();
    }
}
