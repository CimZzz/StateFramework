package com.virtualightning.stateframework.http;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.anno.http.BindHTTPRequest;
import com.virtualightning.stateframework.constant.Charset;
import com.virtualightning.stateframework.constant.RequestMethod;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by CimZzz on ${Date}.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class BindHTTPRequestAnalyzing extends AnalyzingElem<Object> {

    public BindHTTPRequestAnalyzing() {
        modifyValidHelper.addMustContain(Modifier.PUBLIC)
                .addBanContain(Modifier.ABSTRACT)
                .addBanContain(Modifier.STATIC);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return BindHTTPRequest.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element;

        if(!modifyValidHelper.valid(element.getModifiers())){
            error("@BindHTTPRequest 绑定HTTP请求必须为最外层共有类 ,定位于 " + typeElement.getSimpleName());
            return false;
        }

        EnclosingClass enclosingClass = enclosingSet.getEnclosingClass(typeElement);

        BindHTTPRequest bindHTTPRequest = element.getAnnotation(BindHTTPRequest.class);

        enclosingClass.putResource("URL",bindHTTPRequest.url());
        enclosingClass.putResource("Method",bindHTTPRequest.method());
        enclosingClass.putResource("Charset",bindHTTPRequest.charset());

        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {

        ClassName requestBuilder = ClassName.get("com.virtualightning.stateframework.http","Request.Builder");

        RequestMethod method = (RequestMethod) enclosingClass.getResource("Method");
        Charset charset = (Charset) enclosingClass.getResource("Charset");


        MethodSpec.Builder transferMethodBuilder = MethodSpec
                .methodBuilder("transferData")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(enclosingClass.classType,"rawData",Modifier.FINAL)
                .returns(ClassName.get("com.virtualightning.stateframework.http","Request"))
                .addStatement("$T requestBuilder = new $T()",requestBuilder,requestBuilder);

        transferMethodBuilder.addStatement("requestBuilder.method($T.$L)",method.getClass(),method);
        transferMethodBuilder.addStatement("requestBuilder.charset($T.$L)",charset.getClass(),charset);

        return transferMethodBuilder;
    }
}
