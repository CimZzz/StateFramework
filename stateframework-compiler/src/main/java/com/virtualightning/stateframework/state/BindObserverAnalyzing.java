package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;
import com.virtualightning.stateframework.EnclosingSet;
import com.virtualightning.stateframework.UniqueHashMap;
import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.constant.ReferenceType;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by CimZzz(王彦雄) on 3/7/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * 描述
 */
public class BindObserverAnalyzing extends AnalyzingElem<BindObserverAnalyzing.BindObserverElem>{

    public BindObserverAnalyzing() {
        modifyValidHelper.addBanContain(Modifier.PRIVATE)
                .addBanContain(Modifier.ABSTRACT);
    }

    @Override
    public Class<? extends Annotation> getSupportAnnotation() {
        return BindObserver.class;
    }

    @Override
    public boolean handleElement(Element element, EnclosingSet enclosingSet) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement) element;

        if(!modifyValidHelper.valid(element.getModifiers())) {
            error("@BindObserver 修饰的属性不能有 private/abstract 关键字 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }

        String className = enclosingSet.createEnclosingClass(typeElement);

        BindObserver bindObserver = element.getAnnotation(BindObserver.class);
        BindObserverElem bindObserverElem = new BindObserverElem();
        bindObserverElem.allowStop = bindObserver.allowStop();
        bindObserverElem.refType = ReferenceType.STRONG;
        bindObserverElem.runType = bindObserver.runType();
        bindObserverElem.stateId = bindObserver.stateId();
        bindObserverElem.methodName = element.getSimpleName().toString();

        if( !(bindObserverElem.isVarParameters = bindObserver.isVarParameters()) ) {
            bindObserverElem.paramTypes = new ArrayList<>();

            for(VariableElement var : executableElement.getParameters())
                bindObserverElem.paramTypes.add(var.asType());

        } else {
            List<? extends VariableElement> list = executableElement.getParameters();
            if(list.size() != 1 || !list.get(0).asType().toString().equals("java.lang.Object[]")) {
                error("@BindObserver 委托方法参数错误，可变长参数必须为 Object[]  ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                return false;
            }
        }

        if(!sourceManager.putSource(className,bindObserverElem.stateId,bindObserverElem)) {
            error("@BindObserver 状态ID不能相同,状态ID : " + bindObserverElem.stateId + " ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            return false;
        }


        return true;
    }

    @Override
    public MethodSpec.Builder generateMethod(MethodSpec.Builder builder,EnclosingClass enclosingClass) {
        UniqueHashMap<Object,BindObserverElem> uniqueHashMap = sourceManager.getUniqueHashMap(enclosingClass.className);


        ClassName observerBuilderCls = ClassName.get("com.virtualightning.stateframework.state","ObserverBuilder");
        ClassName stateRecordCls = ClassName.get("com.virtualightning.stateframework.state","StateRecord");
        ClassName baseObserverCls = ClassName.get("com.virtualightning.stateframework.state","BaseObserver");

        MethodSpec.Builder stateMethodBuilder =  MethodSpec
                .methodBuilder("bindState")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(enclosingClass.classType,"source")
                .addParameter(stateRecordCls,"stateRecord");

        if(uniqueHashMap == null || uniqueHashMap.size() == 0)
            return stateMethodBuilder;

        stateMethodBuilder.addStatement("$T observerBuilder",observerBuilderCls)
                .addStatement("final $T<$T> sourceRef = new $T<>(source)",WeakReference.class,enclosingClass.classType,WeakReference.class);

        for(BindObserverElem elem : uniqueHashMap.values()) {
            MethodSpec.Builder subMethodBuilder = MethodSpec.methodBuilder("notify")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(Object[].class,"args")
                    .addStatement("$T sourceData = sourceRef.get()",enclosingClass.classType)
                    .beginControlFlow("if (sourceData == null)")
                    .addStatement("return")
                    .endControlFlow();



            if(elem.isVarParameters) {
                subMethodBuilder.addStatement("sourceData.$L(args)",elem.methodName);
            } else {
                int size = elem.paramTypes.size();

                subMethodBuilder.beginControlFlow("if(args.length != $L || args == null)",elem.paramTypes.size());
                subMethodBuilder.addStatement("throw new RuntimeException($S)","激活状态的参数有误，所需参数长度为 " + size + " ,定位于 " + enclosingClass.className + " 的 " + elem.stateId + " 状态");
                subMethodBuilder.endControlFlow();

                if(size == 0)
                    subMethodBuilder.addStatement("sourceData.$L()",elem.methodName);
                else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("($T)args[0]");
                    for(int i = 1 ; i < size ; i ++)
                        stringBuilder.append(",($T)args[")
                                .append(i)
                                .append("]");

                    List<Object> objectList = new ArrayList<>();
                    objectList.add(elem.methodName);
                    objectList.addAll(elem.paramTypes);
                    subMethodBuilder.addStatement("sourceData.$L(" +stringBuilder.toString()+")",objectList.toArray());
                }

            }

            TypeSpec observer = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(baseObserverCls)
                    .addMethod(subMethodBuilder.build())
                    .build();
            stateMethodBuilder
                    .addStatement("observerBuilder = new $T()",observerBuilderCls)
                    .addStatement("observerBuilder.stateId($S)",elem.stateId)
                    .addStatement("observerBuilder.allowStop($L)",elem.allowStop)
                    .addStatement("observerBuilder.refType($L)",elem.refType)
                    .addStatement("observerBuilder.runType($L)",elem.runType)
                    .addStatement("observerBuilder.observer($L)",observer)
                    .addStatement("stateRecord.registerByBuilder(observerBuilder);");
        }

        return stateMethodBuilder;
    }

    static class BindObserverElem {
        String methodName;
        String stateId;
        boolean allowStop;
        int runType;
        int refType;

        boolean isVarParameters;
        List<TypeMirror> paramTypes;
    }
}
