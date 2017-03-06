package com.virtualightning.stateframework.http;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.virtualightning.stateframework.constant.Charset;
import com.virtualightning.stateframework.constant.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created by CimZzz on 3/5/17.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class EnclosingSet {
    String packageName;
    TypeMirror sourceType;
    String sourceName;

    String url;
    RequestMethod method;
    Charset charset;
    List<NamePair> namePairs;
    List<MultipartFile> multipartFiles;
    Map<Integer,UrlParams> urlParams;
    HashMap<String,Header> headers;
    Set<String> nameRepeatValidSet;


    EnclosingSet(String packageName, TypeMirror sourceType, String sourceName) {
        this.packageName = packageName;
        this.sourceType = sourceType;
        this.sourceName = sourceName;

        namePairs = new ArrayList<>();
        multipartFiles = new ArrayList<>();
        urlParams = new HashMap<>();
        headers = new HashMap<>();
        nameRepeatValidSet = new HashSet<>();
    }

    public boolean putUrlParams(int index,UrlParams elem) {
        if(urlParams.containsKey(index))
            return false;

        urlParams.put(index,elem);
        return true;
    }

    public boolean putNamePair(NamePair namePair) {
        String name = namePair.key;
        if(nameRepeatValidSet.contains(name)) {
            return false;
        }

        nameRepeatValidSet.add(name);
        namePairs.add(namePair);
        return true;
    }

    public boolean putMultipartFile(MultipartFile multipartFile) {
        String name = multipartFile.key;
        if(nameRepeatValidSet.contains(name)) {
            return false;
        }

        nameRepeatValidSet.add(name);
        multipartFiles.add(multipartFile);
        return true;
    }

    public boolean putHeader(Header header) {
        String name = header.key;
        if(headers.containsKey(name))
            return false;

        headers.put(name,header);
        return true;
    }


    public JavaFile generateJavaFile() throws Exception{
        ClassName requestBuilder = ClassName.get("com.virtualightning.stateframework.http","Request.Builder");
        ClassName namePairCls = ClassName.get("com.virtualightning.stateframework.http","NamePair");
        ClassName multiFileCls = ClassName.get("com.virtualightning.stateframework.http","MultiFile");
        MethodSpec.Builder transferMethodBuilder = MethodSpec
                .methodBuilder("transferData")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(sourceType),"rawData",Modifier.FINAL)
                .returns(ClassName.get("com.virtualightning.stateframework.http","Request"))
                .addStatement("$T requestBuilder = new $T()",requestBuilder,requestBuilder);

        /*处理请求头部参数*/
        transferMethodBuilder.addStatement("requestBuilder.method($T.$L)",method.getClass(),method);
        transferMethodBuilder.addStatement("requestBuilder.charset($T.$L)",charset.getClass(),charset);

        /*处理URL*/
        if(method.equals(RequestMethod.GET)) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("\"");
            int length = url.length();
            int urlParamsIndex = 0;
            int tokenFlag = 0;
            for(int i = 0 ; i < length ; i ++) {
                char c = url.charAt(i);

                if(c == '{' && tokenFlag == 0) {
                    tokenFlag = 1;
                    continue;
                } else if (c == '$' && tokenFlag == 1) {
                    tokenFlag = 2;
                    continue;
                }else if (c == '}' && tokenFlag == 2) {
                    tokenFlag = 3;
                    UrlParams params = urlParams.get(urlParamsIndex++);
                    if(params != null) {
                        tokenFlag = 0;
                        urlBuilder.append("\"+rawData.").append(params.fieldName).append("+\"");
                        continue;
                    }
                }

                switch (tokenFlag) {
                    case 1:
                        urlBuilder.append('{');
                        tokenFlag = 0;
                        break;
                    case 2:
                        urlBuilder.append('{');
                        urlBuilder.append('$');
                        tokenFlag = 0;
                        break;
                    case 3:
                        urlBuilder.append('{');
                        urlBuilder.append('$');
                        urlBuilder.append('}');
                        tokenFlag = 0;
                        break;
                }

                urlBuilder.append(c);
            }



            switch (tokenFlag) {
                case 1:
                    urlBuilder.append('{');
                    break;
                case 2:
                    urlBuilder.append('{');
                    urlBuilder.append('$');
                    break;
            }

            urlBuilder.append("\"");
            transferMethodBuilder.addStatement("requestBuilder.url($L)",urlBuilder.toString());

        } else if(urlParams.size() != 0)
            throw new Exception("只有GET方法才能使用URLParams");
        else transferMethodBuilder.addStatement("requestBuilder.url($S)",url);

        /*处理头部信息*/
        for(Header header : headers.values()) {
            transferMethodBuilder.addStatement("requestBuilder.header($S,rawData.$L)",header.key,header.fieldName);
        }

        /*处理NamePair*/
        for(NamePair namePair : namePairs) {
            transferMethodBuilder.addStatement("requestBuilder.addFormData(new $T($S,rawData.$L))",namePairCls,namePair.key,namePair.fieldName);
        }

        /*处理MultipartFile*/
        for(MultipartFile multipartFile : multipartFiles) {
            transferMethodBuilder.addStatement("requestBuilder.addFormData(new $T($S,$S,$S,rawData.$L))",multiFileCls,
                    multipartFile.key,
                    multipartFile.filename,
                    multipartFile.contentType,
                    multipartFile.fieldName);
        }

        transferMethodBuilder.addStatement("return requestBuilder.build()");

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(sourceName + "$$$RequestTransform")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get("com.virtualightning.stateframework.http","RequestTransform"), TypeName.get(sourceType)))
                .addMethod(transferMethodBuilder.build());

        return JavaFile.builder(packageName,typeSpec.build()).build();
    }
}
