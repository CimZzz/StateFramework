package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.virtualightning.stateframework.Analyzer;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;

import java.io.IOException;

import javax.annotation.processing.RoundEnvironment;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class StateAnalyzer extends Analyzer {
    public StateAnalyzer() {
        super(new EnclosingSet("$$$","AnnotationBinder"));

        analyzingElemList.add(new BindViewAnalyzing());
        analyzingElemList.add(new BindObserverAnalyzing());
        analyzingElemList.add(new BindResourcesAnalyzing());
        analyzingElemList.add(new EventAnalyzeGroup(
                new OnClickAnalyzing(),
                new OnItemClickAnalyzing()
        ));

    }

    @SuppressWarnings("unchecked")
    @Override
    public void analyze(RoundEnvironment roundEnv) {
        for(AnalyzingElem analyzingElem : analyzingElemList)
            if(!analyzingElem.distinguishElement(roundEnv,enclosingSet))
                return;


        for(EnclosingClass enclosingClass : enclosingSet.values()) {
            TypeSpec.Builder builder = enclosingClass.prepare();
            builder.superclass(
                    ParameterizedTypeName.get(
                            ClassName.get("com.virtualightning.stateframework.state","AnnotationBinder"),
                            enclosingClass.classType));


            for(AnalyzingElem analyzingElem : analyzingElemList) {
                MethodSpec.Builder methodSpecBuilder = analyzingElem.generateMethod(null,enclosingClass);

                if(methodSpecBuilder != null)
                    builder.addMethod(methodSpecBuilder.build());
            }

            try {
                enclosingClass.generateJavaFile().writeTo(getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
