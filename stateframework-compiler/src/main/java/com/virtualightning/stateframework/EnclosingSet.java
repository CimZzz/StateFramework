package com.virtualightning.stateframework;

import java.util.Collection;
import java.util.HashMap;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class EnclosingSet {
    private final HashMap<String,EnclosingClass> enclosingClassMap;
    Elements elements;

    public EnclosingSet() {
        this.enclosingClassMap = new HashMap<>();
    }

    public void init(Elements elements) {
        this.elements = elements;
    }

    public String createEnclosingClass(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();

        if(!enclosingClassMap.containsKey(className))
            enclosingClassMap.put(className,new EnclosingClass(
                    elements.getPackageOf(typeElement).getSimpleName().toString(),
                    typeElement.asType(),
                    className));

        return className;
    }

    public EnclosingClass getEnclosingClass(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();
        EnclosingClass enclosingClass = enclosingClassMap.get(className);

        if(enclosingClass == null){
            enclosingClass = new EnclosingClass(
                    elements.getPackageOf(typeElement).getSimpleName().toString(),
                    typeElement.asType(),
                    className);
            enclosingClassMap.put(className,enclosingClass);
        }

        return enclosingClass;
    }

    public Collection<EnclosingClass> values() {
        return enclosingClassMap.values();
    }
}
