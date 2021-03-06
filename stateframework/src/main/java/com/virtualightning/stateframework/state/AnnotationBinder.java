package com.virtualightning.stateframework.state;

import android.content.res.Resources;
import android.view.View;

import com.virtualightning.stateframework.anno.bind.BindResources;
import com.virtualightning.stateframework.anno.bind.BindView;
import com.virtualightning.stateframework.anno.event.OnClick;
import com.virtualightning.stateframework.anno.event.OnItemClick;
import com.virtualightning.stateframework.anno.state.BindObserver;

/**
 * Created by CimZzz on 17/2/28.<br>
 * Project Name : Virtual-Lightning StateFrameWork<br>
 * Since : StateFrameWork_0.0.1<br>
 * Modify : StateFrameWork_0.1.3 由接口改为基类，减少编译器生成的代码量；添加状态绑定接口消除可能存在的内存泄露<br>
 * Description:<br>
 * 注解绑定者<br>
 * 处理的注解类型为
 * <ol>
 *     <li>{@link BindView}</li>
 *     <li>{@link BindResources}</li>
 *     <li>{@link BindObserver}</li>
 *     <li>{@link OnClick}</li>
 *     <li>{@link OnItemClick}</li>
 * </ol>
 * @param <T> 泛型参数
 */
public abstract class AnnotationBinder<T> {
    public final void bindState(T source,StateRecord stateRecord) {
        IStateBinder<T> stateBinder = getStateBinder();

        if(stateBinder != null) {
            stateBinder.bindState(source, stateRecord);
        }
    }
    public void bindView(T source,View view) {

    }

    public void bindResources(T source, Resources resources) {

    }

    public void bindEvent(T source,View view) {

    }

    protected IStateBinder<T> getStateBinder() {
        return null;
    }

    /**
     * 状态绑定接口<br>
     * Since : StateFrameWork_0.0.1<br>
     * @param <T> 泛型参数
     */
    public interface IStateBinder<T> {
        void bindState(T source,StateRecord stateRecord);
    }
}
