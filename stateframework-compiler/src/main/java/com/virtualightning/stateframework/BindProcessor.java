package com.virtualightning.stateframework;

import com.google.auto.service.AutoService;
import com.virtualightning.stateframework.anno.BindObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.virtualightning.stateframework.anno.BindObserver")
public class BindProcessor extends AbstractProcessor {
    private Messager mMessager; //日志相关的辅助类
    private Filer filer;
    private Elements elements;
    private static HashMap<String,EnclosingSet> setMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.filer = processingEnv.getFiler();
        this.elements = processingEnv.getElementUtils();
        this.mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        setMap = new HashMap<>();

        for(Element element : roundEnv.getElementsAnnotatedWith(BindObserver.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            ExecutableElement executableElement = (ExecutableElement) element;

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 */
            Set<Modifier> modifiers = element.getModifiers();
            if((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                error("绑定为观察者的方法必须为公有或包私有权限，并且不能为抽象或本地方法 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            }

            //判断当前类是否存在
            String className = typeElement.getSimpleName().toString();

            EnclosingSet enclosingSet = setMap.get(className);

            if(enclosingSet == null) {
                enclosingSet = new EnclosingSet(elements.getPackageOf(typeElement).getQualifiedName().toString(), typeElement.asType(), typeElement.getSimpleName().toString());
                setMap.put(className,enclosingSet);
            }

            BindObserver bindView = element.getAnnotation(BindObserver.class);
            AnnotationElem annotationElem = new AnnotationElem();
            annotationElem.allowStop = bindView.allowStop();
            annotationElem.refType = bindView.refType();
            annotationElem.runType = bindView.runType();
            annotationElem.stateId = bindView.stateId();
            annotationElem.methodName = element.getSimpleName().toString();

            if( !(annotationElem.isVarParameters = bindView.isVarParameters()) ) {
                annotationElem.paramTypes = new ArrayList<>();

                for(VariableElement var : executableElement.getParameters())
                    annotationElem.paramTypes.add(var.asType());

            } else {
                List<? extends VariableElement> list = executableElement.getParameters();
                if(list.size() != 1 || !list.get(0).asType().toString().equals("java.lang.Object[]"))
                    error("方法参数错误，可变长参数必须为 Object[]  ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            }

            if(!enclosingSet.putElem(annotationElem)) {
                error("状态ID不能相同,状态ID : " + annotationElem.stateId + " ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            }
        }

        for(EnclosingSet enclosingSet : setMap.values()) {
            try {
                enclosingSet.generateJavaFile().writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
                error("创建源文件失败 ,定位于 " + enclosingSet.sourceName);
            }
        }

        return true;
    }


    public void log(Object object) {
        mMessager.printMessage(Diagnostic.Kind.NOTE,object != null ? object.toString() : "null");
    }

    public void error(Object object) {
        mMessager.printMessage(Diagnostic.Kind.ERROR,object != null ? object.toString() : "null");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
