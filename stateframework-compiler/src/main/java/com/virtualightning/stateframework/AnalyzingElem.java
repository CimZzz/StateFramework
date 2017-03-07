package com.virtualightning.stateframework;

import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 进程元基类
 */
public abstract class AnalyzingElem<T> {
    Messager messager; //日志相关的辅助类
    Elements elements; //用来获取包名等属性辅助
    protected final SourceManager<T> sourceManager;//数据源管理类
    protected final ModifyValidHelper modifyValidHelper;//权限修饰符检测工具

    public AnalyzingElem() {
        sourceManager = new SourceManager<>();
        modifyValidHelper = new ModifyValidHelper();
    }

    public final void init(Messager messager, Elements elements) {
        this.messager = messager;
        this.elements = elements;
    }

    protected abstract Class<? extends Annotation> getSupportAnnotation();
    protected abstract boolean handleElement(Element element,EnclosingSet enclosingSet);
    protected abstract MethodSpec generateMethod(EnclosingClass enclosingClass);

    public Elements getElements() {
        return elements;
    }

    public final void log(Object object) {
        messager.printMessage(Diagnostic.Kind.NOTE,object != null ? object.toString() : "null");
    }

    public final void error(Object object) {
        messager.printMessage(Diagnostic.Kind.ERROR,object != null ? object.toString() : "null");
    }
}
