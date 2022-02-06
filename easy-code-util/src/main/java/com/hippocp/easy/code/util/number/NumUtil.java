package com.hippocp.easy.code.util.number;

import cn.hutool.core.util.StrUtil;

/**
 * 数字工具类，包含判断数字类型判断，是否为数字？原始类型还是包装类型？<br>
 * 以及原始类型、包装类型数字简单名称常量数组<br>
 *
 * @author ZhouYifan
 * @date 2022/1/10
 */
public class NumUtil {

    /**
     * 原始类型整数简单名称数组
     */
    public static final String[] PRIMITIVE_INTEGER_TYPE_SIMPLE_NAME = {
            "byte",
            "int",
            "long",
            "short"
    };

    /**
     * 包装类型整数简单名称数组
     */
    public static final String[] WRAPPER_INTEGER_TYPE_SIMPLE_NAME = {
            "Byte",
            "Integer",
            "Long",
            "Short"
    };

    /**
     * 浮点数原始类型整数简单名称
     */
    public static final String[] PRIMITIVE_DOUBLE_TYPE_SIMPLE_NAME = {
            "double",
            "float"
    };

    /**
     * 浮点数包装类型整数简单名称
     */
    public static final String[] WRAPPER_DOUBLE_TYPE_SIMPLE_NAME = {
            "Double",
            "Float"
    };

    /**
     * 不是整数类型（不区分原始类型和包装类型）
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isNotIntegerType(Class<T> fieldType) {
        return !isIntegerType(fieldType);
    }

    /**
     * 是整数类型（不区分原始类型和包装类型）
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isIntegerType(Class<T> fieldType) {
        return isPrimitiveIntegerType(fieldType) || isWrapperIntegerType(fieldType);
    }

    /**
     * 是原始整数类型
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isPrimitiveIntegerType(Class<T> fieldType) {
        if (fieldType == null) {
            return false;
        }
        String simpleName = fieldType.getSimpleName();
        return StrUtil.equalsAny(simpleName, PRIMITIVE_INTEGER_TYPE_SIMPLE_NAME);
    }

    /**
     * 是包装整数类型
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isWrapperIntegerType(Class<T> fieldType) {
        if (fieldType == null) {
            return false;
        }
        String simpleName = fieldType.getSimpleName();
        return StrUtil.equalsAny(simpleName, WRAPPER_INTEGER_TYPE_SIMPLE_NAME);
    }

    /**
     * 不是浮点数类型（不区分原始类型和包装类型）
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isNotDoubleType(Class<T> fieldType) {
        return !isDoubleType(fieldType);
    }

    /**
     * 不是浮点数类型（不区分原始类型和包装类型）
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isDoubleType(Class<T> fieldType) {
        return isPrimitiveDoubleType(fieldType) || isWrapperDoubleType(fieldType);
    }

    /**
     * 是原始浮点数类型
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isPrimitiveDoubleType(Class<T> fieldType) {
        if (fieldType == null) {
            return false;
        }
        String simpleName = fieldType.getSimpleName();
        return StrUtil.equalsAny(simpleName, PRIMITIVE_DOUBLE_TYPE_SIMPLE_NAME);
    }

    /**
     * 是包装浮点数类型
     *
     * @param fieldType {@link Class}类类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isWrapperDoubleType(Class<T> fieldType) {
        if (fieldType == null) {
            return false;
        }
        String simpleName = fieldType.getSimpleName();
        return StrUtil.equalsAny(simpleName, WRAPPER_DOUBLE_TYPE_SIMPLE_NAME);
    }

    private NumUtil() {
    }

}
