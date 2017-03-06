package com.virtualightning.stateframework.http;

import com.virtualightning.stateframework.Analyzer;
import com.virtualightning.stateframework.anno.BindHTTPRequest;
import com.virtualightning.stateframework.anno.VLHeader;
import com.virtualightning.stateframework.anno.VLMultiFile;
import com.virtualightning.stateframework.anno.VLNamePair;
import com.virtualightning.stateframework.anno.VLUrlParams;
import com.virtualightning.stateframework.constant.RequestMethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class HTTPAnalyzer extends Analyzer {
    public HTTPAnalyzer(Messager mMessager, Filer filer, Elements elements) {
        super(mMessager, filer, elements);
    }

    @Override
    public void analyze(RoundEnvironment roundEnv) {
        HashMap<String,EnclosingSet> setMap = new HashMap<>();
        HashSet<Element> repeatValidSet = new HashSet<>();

        for(Element element : roundEnv.getElementsAnnotatedWith(BindHTTPRequest.class)) {
            TypeElement typeElement = (TypeElement) element;

                /*第一步，检查 Modify 权限必须为最外层共有类*/
            Set<Modifier> modifiers = element.getModifiers();
            if (!modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.ABSTRACT)) {
                error("绑定HTTP请求必须为最外层共有类 ,定位于 " + typeElement.getSimpleName());
            }

            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet enclosingSet = setMap.get(className);

            if (enclosingSet == null) {
                enclosingSet = new EnclosingSet(elements.getPackageOf(typeElement).getQualifiedName().toString(), typeElement.asType(), typeElement.getSimpleName().toString());
                setMap.put(className, enclosingSet);
            } else {
                error("同一个类不能绑定多个HTTP请求 ,定位于 " + typeElement.getSimpleName());
            }


            BindHTTPRequest bindHTTPRequest = element.getAnnotation(BindHTTPRequest.class);
            enclosingSet.url = bindHTTPRequest.url();
            enclosingSet.method = bindHTTPRequest.method();
            enclosingSet.charset = bindHTTPRequest.charset();
        }




        for(Element element : roundEnv.getElementsAnnotatedWith(VLUrlParams.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                    error("绑定为URL填充参数属性必须为公有或包私有权限 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                }
            }

            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet enclosingSet = setMap.get(className);
            if (enclosingSet == null) {
                error("绑定URL填充参数的属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                return;
            }

            VariableElement variableElement = (VariableElement) element;
            /*绑定的属性类型必须为 String,且 HTTP 请求的方法必须为 GET*/
            if(!variableElement.asType().toString().equals("java.lang.String"))
                error("绑定URL填充参数的属性类型必须为 String ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            if(!enclosingSet.method.equals(RequestMethod.GET))
                error("绑定URL填充参数属性所在类绑定HTTP请求的请求方式必须为GET ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");

            VLUrlParams vlUrlParams = element.getAnnotation(VLUrlParams.class);
            UrlParams urlParams = new UrlParams();
            urlParams.fieldName = element.getSimpleName().toString();
            if(!enclosingSet.putUrlParams(vlUrlParams.value(),urlParams))
                error("绑定URL填充参数属性填充位置不可重复 index:"+vlUrlParams.value()+" ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");

            repeatValidSet.add(element);
        }




        for(Element element : roundEnv.getElementsAnnotatedWith(VLHeader.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                    error("绑定为Header参数属性必须为公有或包私有权限 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                }
            }

            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet enclosingSet = setMap.get(className);
            if (enclosingSet == null) {
                error("绑定Header参数的属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                return;
            }

            VariableElement variableElement = (VariableElement) element;
            /*绑定的属性类型必须为 String,且 HTTP 请求的方法必须为 GET*/
            if(!variableElement.asType().toString().equals("java.lang.String"))
                error("绑定Header参数的属性类型必须为 String ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");

            VLHeader vlHeader = element.getAnnotation(VLHeader.class);
            Header header = new Header();
            header.key = vlHeader.value();
            header.fieldName = element.getSimpleName().toString();
            if(!enclosingSet.putHeader(header))
                error("绑定Header参数Key值冲突 key:"+vlHeader.value()+" ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
        }


        for(Element element : roundEnv.getElementsAnnotatedWith(VLNamePair.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                    error("绑定NamePair的属性必须为公有或包私有权限 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                }
            }
            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet enclosingSet = setMap.get(className);
            if (enclosingSet == null) {
                error("绑定NamePair的属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                return;
            }

            if(repeatValidSet.contains(element)) {
                error("绑定HTTP请求参数必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            }

            repeatValidSet.add(element);

            VLNamePair vlNamePair = element.getAnnotation(VLNamePair.class);
            NamePair namePair = new NamePair();
            namePair.key = vlNamePair.value();
            namePair.fieldName = element.getSimpleName().toString();

            if(!enclosingSet.putNamePair(namePair))
                error("绑定HTTP请求参数的name必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
        }


        for(Element element : roundEnv.getElementsAnnotatedWith(VLMultiFile.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            /*第一步，检查 Modify 权限是否为 公有/包私有 并且不能为 抽象/本地 方法 ，如果修饰符等于0默认为包私有*/
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.size() != 0) {
                if ((modifiers.contains(Modifier.ABSTRACT)) || (modifiers.contains(Modifier.NATIVE)) || (!modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.DEFAULT))) {
                    error("绑定MultiFile的属性必须为公有或包私有权限 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                }
            }
            /*第二部，判断当前类是否存在*/
            String className = typeElement.getSimpleName().toString();

            EnclosingSet enclosingSet = setMap.get(className);
            if (enclosingSet == null) {
                error("绑定MultiFile的属性所在类必须先绑定HTTP请求 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
                return;
            }

            if(repeatValidSet.contains(element)) {
                error("绑定HTTP请求参数必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
            }

            repeatValidSet.add(element);

            VLMultiFile vlMultiFile = element.getAnnotation(VLMultiFile.class);
            MultipartFile multipartFile = new MultipartFile();
            multipartFile.key = vlMultiFile.name();
            multipartFile.filename = vlMultiFile.fileName();
            multipartFile.contentType = vlMultiFile.contentType();
            multipartFile.fieldName = element.getSimpleName().toString();

            if(!enclosingSet.putMultipartFile(multipartFile))
                error("绑定HTTP请求参数的name必须唯一 ,定位于 " + typeElement.getSimpleName() + " 的 " + element.getSimpleName() + " 属性");
        }


        for(EnclosingSet enclosingSet : setMap.values()) {
            try {
                enclosingSet.generateJavaFile().writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
                error("创建源文件失败 ,定位于 " + enclosingSet.sourceName + " , 原因为:"+e.getMessage());
            }
        }
    }
}
