package com.virtualightning.stateframework;

import android.util.Log;

/**
 * Created by CimZzz on 16/8/30.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.2.0<br>
 * Description:<br>
 * 消息序列<br>
 * 用于进行序列判断，如果序列号相等表明当前消息为最新消息，则进行更新
 */
public final class MessageSequence {
    private int sequenceId;

    public MessageSequence()
    {
        sequenceId = -1;
    }

    /**
     * 初始化序列号为 -1
     */
    void initSequence()
    {
        synchronized (this)
        {
            sequenceId = -1;
        }
    }

    /**
     * 当前序列号自增加 1，并返回最新的序列号
     * @return 序列号
     */
    int nextSequence()
    {
        synchronized (this)
        {
            sequenceId ++;

            return sequenceId;
        }
    }

    /**
     * 判断当前序列号是否为最新的消息序列号
     * @param sequenceId 当前序列号
     * @return 如果是最新序列号返回true
     */
    boolean validateSequence(int sequenceId)
    {
        Log.v("Test",sequenceId + "  ,  " + this.sequenceId);
        return this.sequenceId == sequenceId;
    }
}
