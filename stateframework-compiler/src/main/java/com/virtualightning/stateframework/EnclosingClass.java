package com.virtualightning.stateframework;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
    TypeName[] superInterfaces;//继承接口

    private TypeSpec.Builder typeSpecBuilder;

    EnclosingClass(String packageName, TypeMirror classType, String className) {
        this.packageName = packageName;
        this.classType = TypeName.get(classType);
        this.className = className;
    }

    public void prepare() {
        typeSpecBuilder = TypeSpec.classBuilder(className + suffixConnector + suffix)
                .addModifiers(Modifier.PUBLIC);

        for(TypeName superInterface : superInterfaces)
            typeSpecBuilder.addSuperinterface(superInterface);
    }

    public JavaFile generateJavaFile() {

        return JavaFile.builder(packageName,typeSpecBuilder.build()).build();
    }
}
