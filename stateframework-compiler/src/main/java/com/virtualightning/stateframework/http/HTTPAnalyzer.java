package com.virtualightning.stateframework.http;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.virtualightning.stateframework.Analyzer;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.PipeLineHandler;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class HTTPAnalyzer extends Analyzer {
    public HTTPAnalyzer() {
        super(new EnclosingSet("$$$","RequestTransform"));
        analyzingElemList.add(new BindHTTPRequestAnalyzing());
        analyzingElemList.add(new UrlParamsAnalyzing());
        analyzingElemList.add(new HeaderAnalyzing());
        analyzingElemList.add(new NamePairAnalyzing());
        analyzingElemList.add(new MultiFileAnalyzing());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void analyze(final RoundEnvironment roundEnv) {

        for (AnalyzingElem analyzingElem : analyzingElemList) {
            Class<? extends Annotation> annotationCls = analyzingElem.getSupportAnnotation();
            for (Element element : roundEnv.getElementsAnnotatedWith(annotationCls))
                if (!analyzingElem.handleElement(element, enclosingSet))
                    return;
        }

        for (EnclosingClass enclosingClass : enclosingSet.values()) {
            TypeSpec.Builder builder = enclosingClass.prepare();
            builder.addSuperinterface(
                    ParameterizedTypeName.get(
                            ClassName.get("com.virtualightning.stateframework.http", "RequestTransform"),
                            enclosingClass.classType));


            MethodSpec.Builder methodSpecBuilder = null;
            for (AnalyzingElem analyzingElem : analyzingElemList) {
                methodSpecBuilder = analyzingElem.generateMethod(methodSpecBuilder, enclosingClass);
            }


            if (methodSpecBuilder != null) {
                methodSpecBuilder.addStatement("return requestBuilder.build()");
                builder.addMethod(methodSpecBuilder.build());
                try {
                    enclosingClass.generateJavaFile().writeTo(getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
