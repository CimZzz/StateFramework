package com.virtualightning.stateframework.state;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by CimZzz on 17/3/1.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class StateElem {
    String methodName;
    String stateId;
    boolean allowStop;
    int runType;
    int refType;

    boolean isVarParameters;
    List<TypeMirror> paramTypes;
}
