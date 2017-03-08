package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.BindResources;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;


/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
@SuppressWarnings("unused")
public class BindResourcesAnalyzing extends AnalyzingElem<BindResourcesAnalyzing.BindResourcesElem> {
    private Map<String,String> allowBindTypeMap;

    public BindResourcesAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.FINAL)
                .addBanContain(Modifier.STATIC)
                .addBanContain(Modifier.PRIVATE);

        allowBindTypeMap = new HashMap<>();
        allowBindTypeMap.put("java.lang.String[]","getStringArray");
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return BindResources.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();

        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@BindResources 修饰的属性不能有 final/private/static 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        String className = enclosingSet.createEnclosingClass(typeElement);

        VariableElement variableElement = (VariableElement) element;

        BindResources bindResources = element.getAnnotation(BindResources.class);

        String varClsName = variableElement.asType().toString();

        if(!allowBindTypeMap.containsKey(varClsName)){
            error("@BindResources 绑定类型不是合法类型 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        BindResourcesElem bindResourcesElem = new BindResourcesElem();
        bindResourcesElem.resId = bindResources.value();
        bindResourcesElem.fieldName = element.getSimpleName().toString();
        bindResourcesElem.searchStatement = allowBindTypeMap.get(varClsName);

        if(!sourceManager.putSource(className,bindResourcesElem.resId,bindResourcesElem)) {
            error("@BindResources 资源ID不能相同 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }

        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,BindResourcesElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);

        builder = MethodSpec.methodBuilder("bindResources")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(enclosingClass.classType,"source")
                .addParameter(ClassName.get("android.content.res","Resources"),"resources");

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return builder;

        for(BindResourcesElem bindResourcesElem : uniqueHashMap.values())
            builder.addStatement("source.$L = resources.$L($L)",bindResourcesElem.fieldName,bindResourcesElem.searchStatement,bindResourcesElem.resId);


        return builder;
    }

    static class BindResourcesElem {
        int resId;
        String fieldName;
        String searchStatement;
    }
}
