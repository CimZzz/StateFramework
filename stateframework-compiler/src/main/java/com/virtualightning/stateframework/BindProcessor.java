package com.virtualightning.stateframework;

import com.google.auto.service.AutoService;
import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.VLMultiFile;
import com.virtualightning.stateframework.anno.VLNamePair;
import com.virtualightning.stateframework.anno.VLUrlParams;
import com.virtualightning.stateframework.http.HTTPAnalyzer;
import com.virtualightning.stateframework.state.EnclosingSet;
import com.virtualightning.stateframework.state.StateAnalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
@SuppressWarnings("unused")
public class BindProcessor extends AbstractProcessor {
    private Messager mMessager; //日志相关的辅助类
    private Filer filer;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.filer = processingEnv.getFiler();
        this.elements = processingEnv.getElementUtils();
        this.mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();

        set.add(BindObserver.class.getName());
        set.add(BindView.class.getName());
        set.add(BindHTTPRequest.class.getName());
        set.add(VLMultiFile.class.getName());
        set.add(VLNamePair.class.getName());
        set.add(VLUrlParams.class.getName());

        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /*State注解解析*/
        StateAnalyzer stateAnalyzer = new StateAnalyzer(mMessager,filer,elements);
        stateAnalyzer.analyze(roundEnv);
        /*HTTP注解解析*/
        HTTPAnalyzer httpAnalyzer = new HTTPAnalyzer(mMessager,filer,elements);
        httpAnalyzer.analyze(roundEnv);


        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
