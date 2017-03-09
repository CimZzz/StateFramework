package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.OnItemClick;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by CimZzz on ${Date}.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class OnItemClickAnalyzing extends AnalyzingElem<OnItemClickAnalyzing.OnItemClickElem> {
    private List<String> allowBindTypeList;

    public OnItemClickAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.PRIVATE)
                .addBanContain(Modifier.ABSTRACT);

        allowBindTypeList = new ArrayList<>();
        allowBindTypeList.add("android.widget.AdapterView");
        allowBindTypeList.add("android.view.View");
        allowBindTypeList.add("int");
        allowBindTypeList.add("long");
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return OnItemClick.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement) element;


        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@OnItemClick 修饰的属性不能有 private/abstract 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }

        String className = enclosingSet.createEnclosingClass(typeElement);

        List<? extends VariableElement> list = executableElement.getParameters();
        String[] params = null;



        if(list.size() > 4) {
            error("@OnItemClick 委托方法参数长度上限为4个，类型可选 AdapterView,View,int,long  ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        } else if (list.size() != 0) {
            List<String> validList = new LinkedList<>(allowBindTypeList);
            params = new String[list.size()];
            int index = 0;
            for(VariableElement variableElement : list) {
                String varTypeName = variableElement.asType().toString();
                if(validList.contains(varTypeName)) {
                    switch (varTypeName) {
                        case "android.view.View":
                            params[index ++] = "view";
                            break;
                        case "android.widget.AdapterView":
                            params[index ++] = "parent";
                            break;
                        case "int":
                            params[index ++] = "position";
                            break;
                        case "long":
                            params[index ++] = "id";
                            break;
                    }
                    validList.remove(varTypeName);
                } else {
                    error("@OnItemClick 委托方法参数错误，类型可选 AdapterView,View,int,long  错误的类型 "+varTypeName+" ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                    return false;
                }
            }
        }

        OnItemClick onItemClick = element.getAnnotation(OnItemClick.class);

        for(int viewId : onItemClick.value()) {
            OnItemClickElem onItemClickElem = new OnItemClickElem();
            onItemClickElem.viewId = viewId;
            onItemClickElem.listenerDesc = onItemClick.listenerDesc();
            onItemClickElem.methodName = element.getSimpleName().toString();
            onItemClickElem.params = params;

            if(!sourceManager.putSource(className,onItemClickElem.viewId,onItemClickElem)) {
                error("@OnItemClick 一个视图不能同时绑定多个OnItemClick方法 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                break;
            }
        }
        return true;
    }


    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,OnItemClickElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return null;


        ClassName adapterViewCls = ClassName.get("android.widget","AdapterView");
        ClassName viewCls = ClassName.get("android.view","View");
        ClassName clickListenerCls = ClassName.get("android.widget","AdapterView.OnItemClickListener");
        HashMap<String,TypeSpec> listenerMap = new HashMap<>();



        for(OnItemClickElem itemClickElem : uniqueHashMap.values()) {
            TypeSpec annonymousCls = listenerMap.get(itemClickElem.methodName);
            if(annonymousCls == null) {


                MethodSpec.Builder overrider = MethodSpec.methodBuilder("onItemClick")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(adapterViewCls,"parent")
                        .addParameter(viewCls,"view")
                        .addParameter(int.class,"position")
                        .addParameter(long.class,"id");


                if(itemClickElem.params != null) {
                    String stateMentFormat = "";

                    for(int i = 0 ; i < itemClickElem.params.length ; i ++)
                        if(i == 0)
                            stateMentFormat += "$L";
                        else stateMentFormat += ",$L";
                    overrider.addStatement("source."+itemClickElem.methodName+"("+stateMentFormat+")",itemClickElem.params);
                }
                else overrider.addStatement("source.$L()",itemClickElem.methodName);

                annonymousCls = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(clickListenerCls)
                        .addMethod(overrider.build())
                        .build();
                listenerMap.put(itemClickElem.methodName,annonymousCls);
            }
            builder.addStatement("(($T)view.findViewById($L)).$L($L)",adapterViewCls,itemClickElem.viewId,itemClickElem.listenerDesc,annonymousCls);
        }

        return builder;
    }

    static class OnItemClickElem {
        int viewId;
        String listenerDesc;
        String methodName;
        Object[] params;
    }
}
