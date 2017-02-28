package com.virtualightning.stateframework;

import com.google.auto.service.AutoService;
import com.virtualightning.stateframework.anno.BindObserver;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.virtualightning.stateframework.anno.BindObserver")
public class BindProcessor extends AbstractProcessor {
    private Filer filer;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.filer = processingEnv.getFiler();
        this.elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for(Element element : roundEnv.getElementsAnnotatedWith(BindObserver.class)) {
            log(element.getSimpleName());
            log(element.getEnclosingElement().getSimpleName());
        }

        return true;
    }

    public void collectAnnotation(RoundEnvironment roundEnv) {

    }

    public void log(Object object) {
        System.out.println("[Anno] : " + (object != null ? object.toString() : "null"));
    }
}
