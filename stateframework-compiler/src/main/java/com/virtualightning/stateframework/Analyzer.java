package com.virtualightning.stateframework;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public abstract class Analyzer {
    Filer filer; //文件相关的辅助类
    Elements elements; //用来获取包名等属性辅助
    protected final PipeLineHandler pipeLineHandler;
    protected final List<AnalyzingElem> analyzingElemList;
    protected final EnclosingSet enclosingSet;

    public Analyzer(EnclosingSet enclosingSet) {
        pipeLineHandler = new PipeLineHandler();
        analyzingElemList = new ArrayList<>();
        this.enclosingSet = enclosingSet;
    }

    public final void init(Messager messager, Filer filer, Elements elements) {
        this.filer = filer;
        this.elements = elements;
        for(AnalyzingElem elem : analyzingElemList)
            elem.init(messager, elements);
        enclosingSet.init(elements);
    }

    public abstract void analyze(RoundEnvironment roundEnv);

    public void getSupportedAnnotationTypes(Set<String> annotations) {
        for(AnalyzingElem elem : analyzingElemList)
            if(elem instanceof AnalyzeGroup)
                ((AnalyzeGroup)elem).getSupportedAnnotationTypes(annotations);
            else annotations.add(elem.getSupportAnnotation().getName());
    }


    public Filer getFiler() {
        return filer;
    }

    public Elements getElements() {
        return elements;
    }
}
