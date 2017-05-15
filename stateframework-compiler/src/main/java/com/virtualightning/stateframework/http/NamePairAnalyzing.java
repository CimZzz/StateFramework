package com.virtualightning.stateframework.http;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.http.VLNamePair;
import com.virtualightning.stateframework.constant.RequestMethod;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by CimZzz on ${Date}.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.7 允许修饰GET方法<br>
 * Description:<br>
 * 键值对解析进程元
 */
public class NamePairAnalyzing extends AnalyzingElem<NamePairAnalyzing.NamePairElem> {

    public NamePairAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.FINAL)
                .addBanContain(Modifier.STATIC)
                .addBanContain(Modifier.PRIVATE);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return VLNamePair.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();

        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@VLNamePair 修饰的属性不能有 final/private/static 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        EnclosingClass enclosingClass = enclosingSet.getEnclosingClass(typeElement,false);

        if (enclosingClass == null) {
            error("@VLNamePair 绑定属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }


        VLNamePair vlNamePair = element.getAnnotation(VLNamePair.class);
        NamePairElem elem = new NamePairElem();
        elem.elementName = element.getSimpleName().toString();

        if(!sourceManager.putSource(enclosingClass.className,vlNamePair.value(),elem)) {
            error("@VLNamePair 绑定参数的name必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        if(!enclosingClass.putConflict("Unique",element)) {
            error("@VLNamePair 绑定类型必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,NamePairElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return builder;




        if(enclosingClass.getResource("Method").equals(RequestMethod.GET)) {
            for (Map.Entry<Object, NamePairElem> entry : uniqueHashMap.entrySet())
                builder.addStatement("requestBuilder.urlParams($S,rawData.$L)", entry.getKey(), entry.getValue().elementName);
        } else {
            ClassName namePairCls = ClassName.get("com.virtualightning.stateframework.http","NamePair");
            for (Map.Entry<Object, NamePairElem> entry : uniqueHashMap.entrySet())
                builder.addStatement("requestBuilder.addFormData(new $T($S,rawData.$L))", namePairCls, entry.getKey()
                        , entry.getValue().elementName);
        }
        return builder;
    }


    static class NamePairElem {
        String elementName;
    }
}
