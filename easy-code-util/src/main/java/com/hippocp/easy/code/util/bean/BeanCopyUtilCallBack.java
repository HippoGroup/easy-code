package com.hippocp.easy.code.util.bean;

/**
 * 对象拷贝回调接口
 *
 * @author ZhouYifan
 * @date 2021/12/15
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack<S, T> {

    /**
     * 定义默认回调方法
     *
     * @param source 源数据类
     * @param target 目标类
     */
    void customCallBack(S source, T target);

}
