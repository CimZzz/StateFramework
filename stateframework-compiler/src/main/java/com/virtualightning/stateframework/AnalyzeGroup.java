package com.virtualightning.stateframework;

import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public abstract class AnalyzeGroup extends AnalyzingElem {
    private final AnalyzingElem[] analyzingElemArray;

    public AnalyzeGroup(AnalyzingElem... analyzingElemArray) {
        this.analyzingElemArray = analyzingElemArray;
    }

    @Override
    void init(Messager messager, Elements elements) {
        for(AnalyzingElem elem : analyzingElemArray)
            elem.init(messager, elements);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return null;
    }

    public void getSupportedAnnotationTypes(Set<String> annotations) {
        for(AnalyzingElem analyzingElem : analyzingElemArray)
            annotations.add(analyzingElem.getSupportAnnotation().getName());
    }

    @Override
    public boolean distinguishElement(RoundEnvironment roundEnv, EnclosingSet enclosingSet) {
        for(AnalyzingElem analyzingElem : analyzingElemArray)
            if(!analyzingElem.distinguishElement(roundEnv,enclosingSet))
                return false;

        return true;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        for(AnalyzingElem analyzingElem : analyzingElemArray)
            if(!analyzingElem.handleElement(element,enclosingSet))
                return false;
        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder, EnclosingClass enclosingClass) {
        builder = generatedRootMethodBuilder(enclosingClass);

        for(AnalyzingElem analyzingElem : analyzingElemArray)
            analyzingElem.generateMethod(builder,enclosingClass);

        return builder;
    }



    protected abstract MethodSpec.Builder generatedRootMethodBuilder(EnclosingClass enclosingClass);
}
