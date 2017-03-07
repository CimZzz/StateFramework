package com.virtualightning.stateframework;

import com.squareup.javapoet.TypeName;

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

    final String suffixConnector;//后缀连接符
    final String suffix;//后缀

    public EnclosingSet(String suffixConnector, String suffix) {
        this.suffixConnector = suffixConnector;
        this.suffix = suffix;

        this.enclosingClassMap = new HashMap<>();
    }

    public void init(Elements elements) {
        this.elements = elements;
    }

    public String createEnclosingClass(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();

        if(!enclosingClassMap.containsKey(className)) {
            EnclosingClass enclosingClass = new EnclosingClass(
                    elements.getPackageOf(typeElement).getQualifiedName().toString(),
                    typeElement.asType(),
                    className);
            enclosingClassMap.put(className,enclosingClass);

            enclosingClass.suffix = suffix;
            enclosingClass.suffixConnector = suffixConnector;
            enclosingClassMap.put(className,enclosingClass);
        }

        return className;
    }

    public EnclosingClass getEnclosingClass(TypeElement typeElement) {
        return getEnclosingClass(typeElement,true);
    }

    public EnclosingClass getEnclosingClass(TypeElement typeElement,boolean autoCreate) {
        String className = typeElement.getSimpleName().toString();
        EnclosingClass enclosingClass = enclosingClassMap.get(className);

        if(enclosingClass == null && autoCreate){
            enclosingClass = new EnclosingClass(
                    elements.getPackageOf(typeElement).getQualifiedName().toString(),
                    typeElement.asType(),
                    className);
            enclosingClass.suffix = suffix;
            enclosingClass.suffixConnector = suffixConnector;
            enclosingClassMap.put(className,enclosingClass);
        }

        return enclosingClass;
    }

    public Collection<EnclosingClass> values() {
        return enclosingClassMap.values();
    }
}
