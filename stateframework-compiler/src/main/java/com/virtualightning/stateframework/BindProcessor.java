package com.virtualightning.stateframework;

import com.google.auto.service.AutoService;
import com.virtualightning.stateframework.http.HTTPAnalyzer;
import com.virtualightning.stateframework.state.StateAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class BindProcessor extends AbstractProcessor {
    private final List<Analyzer> analyzerList;
    private int runCount;

    public BindProcessor() {
        runCount = 0;
        analyzerList = new ArrayList<>();
        analyzerList.add(new StateAnalyzer());
        analyzerList.add(new HTTPAnalyzer());
    }

    private Messager messager;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        Filer filer = processingEnv.getFiler();
        Elements elements = processingEnv.getElementUtils();

        for(Analyzer analyzer : analyzerList)
            analyzer.init(messager,filer,elements);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();

        for (Analyzer analyzer : analyzerList)
            analyzer.getSupportedAnnotationTypes(set);

        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(runCount ++ > 0)
            return false;

        try {
            for (Analyzer analyzer : analyzerList)
                analyzer.analyze(roundEnv);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
