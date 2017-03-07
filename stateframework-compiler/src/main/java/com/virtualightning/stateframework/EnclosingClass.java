package com.virtualightning.stateframework;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class EnclosingClass {
    public final String packageName;//包名
    public final TypeName classType;//类类型
    public final String className;//类名

    String suffixConnector;//后缀连接符
    String suffix;//后缀

    final HashMap<String,Object> resourceMap;//共享表
    final HashMap<Object,Set<Object>> conflictMap;//冲突表

    private TypeSpec.Builder typeSpecBuilder;

    EnclosingClass(String packageName, TypeMirror classType, String className) {
        this.packageName = packageName;
        this.classType = TypeName.get(classType);
        this.className = className;
        this.conflictMap = new HashMap<>();
        this.resourceMap = new HashMap<>();
    }

    public void putResource(String key,Object res) {
        resourceMap.put(key, res);
    }

    public Object getResource(String key) {
        return resourceMap.get(key);
    }

    public boolean putConflict(Object type,Object key) {
        Set<Object> conflictSet = conflictMap.get(type);

        if(conflictSet == null) {
            conflictSet = new HashSet<>();
            conflictMap.put(type,conflictSet);
        }

        if(conflictSet.contains(key))
            return false;

        conflictSet.add(key);
        return true;
    }

    public TypeSpec.Builder prepare() {
        if(typeSpecBuilder == null) {
            typeSpecBuilder = TypeSpec.classBuilder(className + suffixConnector + suffix)
                    .addModifiers(Modifier.PUBLIC);
        }

        return typeSpecBuilder;
    }

    public JavaFile generateJavaFile() {
        return JavaFile.builder(packageName,typeSpecBuilder.build()).build();
    }
}
