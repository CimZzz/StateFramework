package com.virtualightning.stateframework;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created by CimZzz on ${Date}.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class EnclosingSet {

    final String packageName;
    final TypeMirror sourceType;
    final String sourceName;
    final HashMap<String,StateElem> stateMap;
    final HashMap<Integer,FieldElem> fieldMap;

    public EnclosingSet(String packageName, TypeMirror sourceType, String sourceName) {
        this.packageName = packageName;
        this.sourceType = sourceType;
        this.sourceName = sourceName;

        stateMap = new HashMap<>();
        fieldMap = new HashMap<>();
    }


    public boolean putState(StateElem elem) {
        String stateId = elem.stateId;
        if(stateMap.containsKey(stateId))
            return false;

        stateMap.put(stateId,elem);
        return true;
    }

    public boolean putField(FieldElem elem) {
        int viewId = elem.viewId;
        if(fieldMap.containsKey(viewId))
            return false;

        fieldMap.put(viewId,elem);
        return true;
    }

    public JavaFile generateJavaFile() {
        ClassName observerBuilder = ClassName.get("com.virtualightning.stateframework.core","ObserverBuilder");

        MethodSpec.Builder stateMethodBuilder =  MethodSpec
                .methodBuilder("bindState")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get("com.virtualightning.stateframework.core","StateRecord"),"stateRecord")
                .addParameter(TypeName.get(sourceType),"source",Modifier.FINAL)
                .addStatement("$T observerBuilder",observerBuilder);

        for(StateElem elem : stateMap.values()) {
            MethodSpec.Builder subMethodBuilder = MethodSpec.methodBuilder("notify")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(Object[].class,"args");

            if(elem.isVarParameters) {
                subMethodBuilder.addStatement("source.$L(args)",elem.methodName);
            } else {
                int size = elem.paramTypes.size();

                subMethodBuilder.beginControlFlow("if(args.length != $L || args == null)",elem.paramTypes.size());
                subMethodBuilder.addStatement("throw new RuntimeException($S)","激活状态的参数有误，所需参数长度为 " + size + " ,定位于 " + sourceType + " 的 " + elem.stateId + " 状态");
                subMethodBuilder.endControlFlow();

                if(size == 0)
                    subMethodBuilder.addStatement("source.$L()",elem.methodName);
                else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("($T)args[0]");
                    for(int i = 1 ; i < size ; i ++)
                        stringBuilder.append(",($T)args[")
                                .append(i)
                                .append("]");

                    List<Object> objectList = new ArrayList<>();
                    objectList.add(elem.methodName);
                    objectList.addAll(elem.paramTypes);
                    subMethodBuilder.addStatement("source.$L(" +stringBuilder.toString()+")",objectList.toArray());
                }

            }

            TypeSpec observer = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ClassName.get("com.virtualightning.stateframework.core","BaseObserver"))
                    .addMethod(subMethodBuilder.build())
                    .build();
            stateMethodBuilder
                    .addStatement("observerBuilder = new $T()",observerBuilder)
                    .addStatement("observerBuilder.stateId($S)",elem.stateId)
                    .addStatement("observerBuilder.allowStop($L)",elem.allowStop)
                    .addStatement("observerBuilder.refType($L)",elem.refType)
                    .addStatement("observerBuilder.runType($L)",elem.runType)
                    .addStatement("observerBuilder.observer($L)",observer)
                    .addStatement("stateRecord.registerByBuilder(observerBuilder);");
        }

        MethodSpec.Builder viewMethodBuilder = MethodSpec
                .methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(sourceType),"source");

        for(FieldElem elem : fieldMap.values()) {
            viewMethodBuilder.addStatement("source.$L = ($T)source.findViewById($L)",elem.fieldName,elem.fieldType,elem.viewId);
        }


        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(sourceName + "$$$AnnotationBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get("com.virtualightning.stateframework.core","AnnotationBinder"),TypeName.get(sourceType)))
                .addMethod(stateMethodBuilder.build())
                .addMethod(viewMethodBuilder.build());

        return JavaFile.builder(packageName,typeSpec.build()).build();
    }
}
