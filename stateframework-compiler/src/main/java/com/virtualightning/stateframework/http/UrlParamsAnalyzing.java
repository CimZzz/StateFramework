package com.virtualightning.stateframework.http;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.http.VLUrlParams;
import com.virtualightning.stateframework.constant.Charset;
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
 * Modify : StateFrameWork_0.1.7 添加URLParamsElem作为解析元，拓展了URLParams注解的属性<br>
 * Description:<br>
 * URL参数解析进程元
 */
public class UrlParamsAnalyzing extends AnalyzingElem<UrlParamsAnalyzing.UrlParamsElem> {

    public UrlParamsAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.FINAL)
                .addBanContain(Modifier.STATIC)
                .addBanContain(Modifier.PRIVATE);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return VLUrlParams.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();

        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@VLUrlParams 修饰的属性不能有 final/private/static 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        EnclosingClass enclosingClass = enclosingSet.getEnclosingClass(typeElement,false);

        if (enclosingClass == null) {
            error("@VLUrlParams 绑定属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        VariableElement variableElement = (VariableElement) element;
        /*绑定的属性类型必须为 String,且 HTTP 请求的方法必须为 GET*/
        if(!variableElement.asType().toString().equals("java.lang.String")) {
            error("@VLUrlParams 绑定属性类型必须为 String ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }
        if(!enclosingClass.getResource("Method").equals(RequestMethod.GET)) {
            error("@VLUrlParams 绑定属性所在类绑定HTTP请求的请求方式必须为GET ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        VLUrlParams vlUrlParams = element.getAnnotation(VLUrlParams.class);
        UrlParamsElem elem = new UrlParamsElem();
        elem.elementName = element.getSimpleName().toString();
        elem.index = vlUrlParams.value();
        elem.isEncode = vlUrlParams.encoding();

        if(!sourceManager.putSource(enclosingClass.className,vlUrlParams.value(),elem)) {
            error("@VLUrlParams 绑定属性填充位置不可重复 index:" + vlUrlParams.value() + " ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        if(!enclosingClass.putConflict("Unique",element)) {
            error("@VLUrlParams 绑定类型必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            return false;
        }

        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        UniqueHashMap<Object,UrlParamsElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);

        String url = (String) enclosingClass.getResource("URL");

        if(uniqueHashMap == null || uniqueHashMap.size() == 0) {
            builder.addStatement("requestBuilder.url($S)",url);
            return builder;
        }

        ClassName URLEncodeUtilsCls = ClassName.get("com.virtualightning.stateframework.utils","URLEncodeUtils");
        Charset charset = (Charset) enclosingClass.getResource("Charset");

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("\"");
        int length = url.length();
        int urlParamsIndex = 0;
        int tokenFlag = 0;
        for(int i = 0 ; i < length ; i ++) {
            char c = url.charAt(i);

            if(c == '{' && tokenFlag == 0) {
                tokenFlag = 1;
                continue;
            } else if (c == '$' && tokenFlag == 1) {
                tokenFlag = 2;
                continue;
            }else if (c == '}' && tokenFlag == 2) {
                tokenFlag = 3;
                UrlParamsElem params = uniqueHashMap.get(urlParamsIndex++);
                if(params != null) {
                    tokenFlag = 0;
                    String paramsIndex = "params" + (urlParamsIndex - 1);
                    if(params.isEncode)
                        builder.addStatement("$T "+ paramsIndex + "=$T.encodeContent(rawData." + params.elementName + ",$T.$L)",String.class,URLEncodeUtilsCls,charset.getClass(),charset);
                    else builder.addStatement("$T " + paramsIndex + "= rawData." + params.elementName,String.class);
                    urlBuilder.append("\"+").append(paramsIndex).append("+\"");
                    continue;
                }
            }

            switch (tokenFlag) {
                case 1:
                    urlBuilder.append('{');
                    tokenFlag = 0;
                    break;
                case 2:
                    urlBuilder.append('{');
                    urlBuilder.append('$');
                    tokenFlag = 0;
                    break;
                case 3:
                    urlBuilder.append('{');
                    urlBuilder.append('$');
                    urlBuilder.append('}');
                    tokenFlag = 0;
                    break;
            }

            urlBuilder.append(c);
        }



        switch (tokenFlag) {
            case 1:
                urlBuilder.append('{');
                break;
            case 2:
                urlBuilder.append('{');
                urlBuilder.append('$');
                break;
        }

        urlBuilder.append("\"");
        builder.addStatement("requestBuilder.url($L)",urlBuilder.toString());

        return builder;
    }


    static class UrlParamsElem {
        String elementName;
        int index;
        boolean isEncode;
    }
}
