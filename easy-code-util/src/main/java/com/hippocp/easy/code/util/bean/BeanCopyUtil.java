package com.hippocp.easy.code.util.bean;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * 对象属性拷贝工具类
 *
 * @author ZhouYifan
 * @date 2021/12/15
 */
public class BeanCopyUtil {

    /**
     * 拷贝集合中的对象属性
     *
     * @param sources: 源数据类集合
     * @param target:  目标类::new(SchoolMottoDTO::new)
     * @return {@link List}
     */
    public static <S, T> List<T> copyListProperties(Collection<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 带回调函数的集合数据的拷贝（可自定义对象属性拷贝规则）
     *
     * @param sources:        源数据类集合
     * @param target:         目标类::new(SchoolMottoDTO::new)
     * @param customCallBack: 回调函数
     * @return {@link List}
     */
    public static <S, T> List<T> copyListProperties(Collection<S> sources, Supplier<T> target,
                                                    BeanCopyUtilCallBack<S, T> customCallBack) {
        List<T> list = new ArrayList<>(sources.size());

        for (S source : sources) {
            // 获取目标对象
            T t = target.get();
            // 拷贝
            BeanUtils.copyProperties(source, t);
            // 加入列表
            list.add(t);

            if (customCallBack != null) {
                // 回调
                customCallBack.customCallBack(source, t);
            }

        }
        // 返回列表
        return list;

    }

    private BeanCopyUtil() {
    }

}
