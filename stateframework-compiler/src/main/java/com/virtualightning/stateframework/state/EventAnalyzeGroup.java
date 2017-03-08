package com.virtualightning.stateframework.state;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.virtualightning.stateframework.AnalyzeGroup;
import com.virtualightning.stateframework.AnalyzingElem;
import com.virtualightning.stateframework.EnclosingClass;

import javax.lang.model.element.Modifier;

/**
 * Created by CimZzz on 17/3/8.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.1.2<br>
 * Description:<br>
 * Description
 */
public class EventAnalyzeGroup extends AnalyzeGroup {

    public EventAnalyzeGroup(AnalyzingElem... analyzingElemArray) {
        super(analyzingElemArray);
    }

    @Override
    protected MethodSpec.Builder generatedRootMethodBuilder(EnclosingClass enclosingClass) {
        ClassName viewCls = ClassName.get("android.view","View");

        return MethodSpec
                .methodBuilder("bindEvent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(enclosingClass.classType,"source",Modifier.FINAL)
                .addParameter(viewCls,"view");
    }
}
