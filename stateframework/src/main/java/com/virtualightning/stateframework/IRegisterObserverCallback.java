package com.virtualightning.stateframework;

/**
 * Created by CimZzz on 11/16/16.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.2.3<br>
 * Description:<br>
 * 状态观察者轮询注册接口
 */
public interface IRegisterObserverCallback {
    /**
     * 配置注册状态观察者构造器
     * @param stateId 状态名
     * @param builder 注册状态观察者构造器
     * @return 如果不对此状态注册状态观察者则返回 false
     */
    boolean configObserverBuilder(String stateId, ObserverBuilder builder);
}
