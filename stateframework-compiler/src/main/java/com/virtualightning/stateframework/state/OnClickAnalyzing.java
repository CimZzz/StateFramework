package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.OnClick;

import java.lang.annotation.Annotation;
import java.util.HashMap;
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
public class OnClickAnalyzing extends AnalyzingElem<OnClickAnalyzing.OnClickElem> {
    public OnClickAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.PRIVATE)
                .addBanContain(Modifier.ABSTRACT);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return OnClick.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement) element;


        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@OnClick 修饰的属性不能有 private/abstract 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }

        String className = enclosingSet.createEnclosingClass(typeElement);

        List<? extends VariableElement> list = executableElement.getParameters();

        if(list.size() != 1 || !list.get(0).asType().toString().equals("android.view.View")) {
            error("@OnClick 委托方法参数错误，必须为 android.view.View ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }

        OnClick onClick = element.getAnnotation(OnClick.class);

        for(int viewId : onClick.value()) {
            OnClickElem onClickElem = new OnClickElem();
            onClickElem.viewId = viewId;
            onClickElem.listenerDesc = onClick.listenerDesc();
            onClickElem.methodName = element.getSimpleName().toString();

            if(!sourceManager.putSource(className,onClickElem.viewId,onClickElem)) {
                error("@OnClick 一个视图不能同时绑定多个Click方法 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                break;
            }
        }
        return true;
    }


    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,OnClickElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);

        ClassName viewCls = ClassName.get("android.view","View");
        ClassName clickListenerCls = ClassName.get("android.view","View.OnClickListener");

        MethodSpec.Builder eventMethodBuilder = MethodSpec
                .methodBuilder("bindEvent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(enclosingClass.classType,"source",Modifier.FINAL)
                .addParameter(viewCls,"view");

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return eventMethodBuilder;

        eventMethodBuilder.beginControlFlow("if(view == null)");

        HashMap<String,TypeSpec> listenerMap = new HashMap<>();
        for(OnClickElem clickElem : uniqueHashMap.values()) {
            TypeSpec annonymousCls = listenerMap.get(clickElem.methodName);
            if(annonymousCls == null) {
                annonymousCls = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(clickListenerCls)
                        .addMethod(MethodSpec.methodBuilder("onClick")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(viewCls,"view")
                                .addStatement("source.")
                                .build())
                        .build();
                listenerMap.put(clickElem.methodName,annonymousCls);
            }
            eventMethodBuilder.addStatement("source.findViewById($L).$L($L)",clickElem.viewId,clickElem.viewId,annonymousCls);
        }

        eventMethodBuilder.endControlFlow().beginControlFlow("else");

        for(OnClickElem clickElem : uniqueHashMap.values()) {
            TypeSpec annonymousCls = listenerMap.get(clickElem.methodName);
            eventMethodBuilder.addStatement("source.findViewById($L).$L($L)",clickElem.viewId,clickElem.viewId,annonymousCls);
        }

        eventMethodBuilder.endControlFlow();

        return eventMethodBuilder;
    }

    static class OnClickElem {
        int viewId;
        String listenerDesc;
        String methodName;
    }
}
