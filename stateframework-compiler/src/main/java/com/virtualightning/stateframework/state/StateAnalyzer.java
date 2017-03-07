package com.virtualightning.stateframework.state;

import com.virtualightning.stateframework.Analyzer;
import com.virtualightning.stateframework.anno.BindObserver;
import com.virtualightning.stateframework.anno.BindView;
import com.virtualightning.stateframework.anno.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by CimZzz on 3/4/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class StateAnalyzer extends Analyzer {

    public StateAnalyzer() {

    }



    @Override
    public void analyze(RoundEnvironment roundEnv) {
        HashMap<String,EnclosingSet2> setMap = new HashMap<>();

        for(Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if(modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {

                }
            }

            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet2 enclosingSet = setMap.get(className);

            if(enclosingSet == null) {
                enclosingSet = new EnclosingSet2(elements.getPackageOf(typeElement).getQualifiedName().toString(), typeElement.asType(), typeElement.getSimpleName().toString());
                setMap.put(className,enclosingSet);
            }


            BindView bindView = element.getAnnotation(BindView.class);
            FieldElem fieldElem = new FieldElem();
            fieldElem.viewId = bindView.value();
            fieldElem.fieldType = element.asType();
            fieldElem.fieldName = element.getSimpleName().toString();

            if(!enclosingSet.putField(fieldElem)) {
                error("视图ID不能相同,视图ID : " + fieldElem.viewId + " ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(BindObserver.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            ExecutableElement executableElement = (ExecutableElement) element;

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if(modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                    error("绑定为观察者的方法必须为公有或包私有权限，并且不能为抽象或本地方法 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                }
            }

            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet2 enclosingSet = setMap.get(className);

            if(enclosingSet == null) {
                enclosingSet = new EnclosingSet2(elements.getPackageOf(typeElement).getQualifiedName().toString(), typeElement.asType(), typeElement.getSimpleName().toString());
                setMap.put(className,enclosingSet);
            }

            BindObserver bindState = element.getAnnotation(BindObserver.class);
            StateElem annotationElem = new StateElem();
            annotationElem.allowStop = bindState.allowStop();
            annotationElem.refType = bindState.refType();
            annotationElem.runType = bindState.runType();
            annotationElem.stateId = bindState.stateId();
            annotationElem.methodName = element.getSimpleName().toString();

            if( !(annotationElem.isVarParameters = bindState.isVarParameters()) ) {
                annotationElem.paramTypes = new ArrayList<>();

                for(VariableElement var : executableElement.getParameters())
                    annotationElem.paramTypes.add(var.asType());

            } else {
                List<? extends VariableElement> list = executableElement.getParameters();
                if(list.size() != 1 || !list.get(0).asType().toString().equals("java.lang.Object[]"))
                    error("方法参数错误，可变长参数必须为 Object[]  ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            }

            if(!enclosingSet.putState(annotationElem)) {
                error("状态ID不能相同,状态ID : " + annotationElem.stateId + " ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                    error("绑定为观察者的方法必须为公有或包私有权限，并且不能为抽象或本地方法 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                }
            }

            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet2 enclosingSet = setMap.get(className);

            if (enclosingSet == null) {
                enclosingSet = new EnclosingSet2(elements.getPackageOf(typeElement).getQualifiedName().toString(), typeElement.asType(), typeElement.getSimpleName().toString());
                setMap.put(className, enclosingSet);
            }

            OnClick onClick = element.getAnnotation(OnClick.class);
            ClickEvent event = new ClickEvent();

            for(int viewId : onClick.value()) {
                event.viewId = viewId;
                event.listenerDesc = onClick.listenerDesc();
                event.methodName = element.getSimpleName().toString();

                if(!enclosingSet.clickEventMap.put(event.viewId,event)) {
                    error("一个视图不能同时绑定多个Click方法 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 方法");
                    break;
                }
            }


        }

        for(EnclosingSet2 enclosingSet : setMap.values()) {
            try {
                enclosingSet.generateJavaFile().writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
                error("创建源文件失败 ,定位于 " + enclosingSet.sourceName);
            }
        }
    }

    @Override
    public void getSupportedAnnotationTypes(Set<String> annotations) {

    }
}
