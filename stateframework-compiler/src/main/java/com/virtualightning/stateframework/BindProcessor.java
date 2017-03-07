package com.virtualightning.stateframework;

import com.google.auto.service.AutoService;
import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnClick;
import com.virtualightning.stateframework.anno.VLMultiFile;
import com.virtualightning.stateframework.anno.VLNamePair;
import com.virtualightning.stateframework.anno.VLUrlParams;
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
@SuppressWarnings("unused")
public class BindProcessor extends AbstractProcessor {
    private final List<Analyzer> analyzerList;

    public BindProcessor() {
        analyzerList = new ArrayList<>();
        analyzerList.add(new StateAnalyzer());
        analyzerList.add(new HTTPAnalyzer());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Messager messager = processingEnv.getMessager();
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

        set.add(BindObserver.class.getName());
        set.add(BindView.class.getName());
        set.add(BindHTTPRequest.class.getName());
        set.add(VLMultiFile.class.getName());
        set.add(VLNamePair.class.getName());
        set.add(VLUrlParams.class.getName());
        set.add(OnClick.class.getName());

        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Analyzer analyzer : analyzerList)
            analyzer.analyze(roundEnv);


        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
