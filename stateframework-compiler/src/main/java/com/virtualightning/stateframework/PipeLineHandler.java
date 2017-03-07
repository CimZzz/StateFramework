package com.virtualightning.stateframework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by CimZzz on 17/3/7.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Description:<br>
 * Description
 */
public class PipeLineHandler {
    public static final int APPEND_START = -1;
    public static final int APPEND_END = -2;

    private final List<PipeLine> baseLine;
    private final Map<Integer,List<PipeLine>> subLineMap;

    public PipeLineHandler() {
        baseLine = new LinkedList<>();
        subLineMap = new HashMap<>();
    }

    public void addBaseLine(PipeLine pipeLine) {
        baseLine.add(pipeLine);
    }

    public void addSubLine(int subLineIndex,PipeLine pipeLine) {
        List<PipeLine> lineList = subLineMap.get(subLineIndex);

        if(lineList == null) {
            lineList = new LinkedList<>();
            subLineMap.put(subLineIndex,lineList);
        }

        lineList.add(pipeLine);
    }

    public void integrateSubLine(int subLineIndex,int position) {
        List<PipeLine> lineList = subLineMap.get(subLineIndex);

        if(lineList == null)
            return;

        if(position == APPEND_START)
            position = 0;
        else if(position == APPEND_END)
            position = baseLine.size();
        else if(position < 0 || position > baseLine.size())
            return;

        baseLine.addAll(position,lineList);
        subLineMap.remove(subLineIndex);
        lineList.clear();
    }

    public void doProcess() {
        for(PipeLine pipeLine : baseLine)
            if(!pipeLine.process())
                return;
    }

    public void doSubProcess(int subLineIndex) {
        List<PipeLine> lineList = subLineMap.get(subLineIndex);

        if(lineList == null)
            return;

        for(PipeLine pipeLine : lineList)
            if(!pipeLine.process())
                return;
    }

    public interface PipeLine {
        boolean process();
    }
}
