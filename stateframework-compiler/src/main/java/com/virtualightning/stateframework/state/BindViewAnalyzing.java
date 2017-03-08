package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.BindView;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 绑定视图解析器
 */
public class BindViewAnalyzing extends AnalyzingElem<BindViewAnalyzing.BindViewElem> {

    public BindViewAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.FINAL)
                .addBanContain(Modifier.STATIC)
                .addBanContain(Modifier.PRIVATE);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return BindView.class;
    }

    @Override
    public boolean handleElement(Element element, com.virtualightning.stateframework.EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();

        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@BindView 修饰的属性不能有 final/private/static 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        String className = enclosingSet.createEnclosingClass(typeElement);

        BindView bindView = element.getAnnotation(BindView.class);
        BindViewElem bindViewElem = new BindViewElem();
        bindViewElem.viewId = bindView.value();
        bindViewElem.fieldType = element.asType();
        bindViewElem.fieldName = element.getSimpleName().toString();

        if(!sourceManager.putSource(className,bindViewElem.viewId,bindViewElem)) {
            error("@BindView 视图ID不能相同 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }

        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,BindViewElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);
        ClassName viewCls = ClassName.get("android.view","View");

        MethodSpec.Builder viewMethodBuilder = MethodSpec
                .methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(enclosingClass.classType,"source")
                .addParameter(viewCls,"view");

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return viewMethodBuilder;


        Collection<BindViewElem> bindViewElems = uniqueHashMap.values();

        viewMethodBuilder.beginControlFlow("if(view == null)");

        for(BindViewElem bindViewElem : bindViewElems) {
            viewMethodBuilder.addStatement("source.$L = ($T)source.findViewById($L)",bindViewElem.fieldName,bindViewElem.fieldType,bindViewElem.viewId);
        }

        viewMethodBuilder.endControlFlow()
                .beginControlFlow("else");

        for(BindViewElem bindViewElem : bindViewElems) {
            viewMethodBuilder.addStatement("source.$L = ($T)view.findViewById($L)",bindViewElem.fieldName,bindViewElem.fieldType,bindViewElem.viewId);
        }

        viewMethodBuilder.endControlFlow();

        return viewMethodBuilder;
    }

    static class BindViewElem {
        int viewId;
        TypeMirror fieldType;
        String fieldName;
    }

}
