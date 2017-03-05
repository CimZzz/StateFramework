package com.virtualightning.stateframework;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public abstract class Analyzer {
    protected final Messager mMessager; //日志相关的辅助类
    protected final Filer filer;
    protected final Elements elements;

    public Analyzer(Messager mMessager, Filer filer, Elements elements) {
        this.mMessager = mMessager;
        this.filer = filer;
        this.elements = elements;
    }

    public abstract void analyze(RoundEnvironment roundEnv);



    public void log(Object object) {
        mMessager.printMessage(Diagnostic.Kind.NOTE,object != null ? object.toString() : "null");
    }

    public void error(Object object) {
        mMessager.printMessage(Diagnostic.Kind.ERROR,object != null ? object.toString() : "null");
    }

}
