package com.virtualightning.stateframework.http;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.VLMultiFile;
import com.virtualightning.stateframework.anno.VLNamePair;
import com.virtualightning.stateframework.constant.RequestMethod;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
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
public class MultiFileAnalyzing extends AnalyzingElem<MultiFileAnalyzing.MultiFileElem> {

    public MultiFileAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.FINAL)
                .addBanContain(Modifier.STATIC)
                .addBanContain(Modifier.PRIVATE);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return VLMultiFile.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();

        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@VLMultiFile 修饰的属性不能有 final/private/static 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        EnclosingClass enclosingClass = enclosingSet.getEnclosingClass(typeElement,false);

        if (enclosingClass == null) {
            error("@VLMultiFile 绑定属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        VariableElement variableElement = (VariableElement) element;
        if(!variableElement.asType().toString().equals("java.io.File")) {
            error("@VLMultiFile 绑定属性类型必须为 File ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        if(enclosingClass.getResource("Method").equals(RequestMethod.GET)) {
            error("@VLMultiFile 绑定属性所在类绑定HTTP请求的请求方式不能为GET ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        VLMultiFile vlMultiFile = element.getAnnotation(VLMultiFile.class);
        MultiFileElem multiFileElem = new MultiFileElem();
        multiFileElem.key = vlMultiFile.name();
        multiFileElem.filename = vlMultiFile.fileName();
        multiFileElem.contentType = vlMultiFile.contentType();
        multiFileElem.fieldName = element.getSimpleName().toString();

        if(!sourceManager.putSource(enclosingClass.className,multiFileElem.key,multiFileElem)) {
            error("@VLMultiFile 绑定参数的name必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        if(!enclosingClass.putConflict("Unique",element)) {
            error("@VLMultiFile 绑定类型必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,MultiFileElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return builder;

        ClassName multiFileCls = ClassName.get("com.virtualightning.stateframework.http","MultiFile");

        for(MultiFileElem multiFileElem : uniqueHashMap.values()) {
            builder.addStatement("requestBuilder.addFormData(new $T($S,$S,$S,rawData.$L))",multiFileCls,
                    multiFileElem.key,
                    multiFileElem.filename,
                    multiFileElem.contentType,
                    multiFileElem.fieldName);
        }

        return builder;
    }


    static class MultiFileElem {
        String key;
        String filename;
        String contentType;
        String fieldName;
    }
}
