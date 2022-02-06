package com.hippocp.easy.code.util.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * 自定义校验器返回结果类
 *
 * @author ZhouYifan
 * @date 2022/1/8
 */
@Getter
@ToString
@EqualsAndHashCode
public class CustomValidatorResult<T> implements Serializable {

    /**
     * 是否校验通过
     */
    private final boolean isValid;

    /**
     * 自定义校验器约束违反提示信息
     */
    private final Set<EasyConstraintViolationImpl<T>> constraintViolationSet;


    public CustomValidatorResult(boolean isValid, Set<EasyConstraintViolationImpl<T>> constraintViolationSet) {
        this.isValid = isValid;
        this.constraintViolationSet = constraintViolationSet;
    }


    /**
     * 通过校验
     *
     * @param <T> 被校验的实体类
     * @return {@link CustomValidatorResult}
     */
    public static <T> CustomValidatorResult<T> passValid() {
        return new CustomValidatorResult<>(true, null);
    }


    /**
     * 不通过校验
     *
     * @param constraintViolationSet 自定义校验器约束违反提示信息
     * @param <T>                    被校验的实体类
     * @return {@link CustomValidatorResult}
     */
    public static <T> CustomValidatorResult<T> notPassValid(Set<EasyConstraintViolationImpl<T>> constraintViolationSet) {
        return new CustomValidatorResult<>(false, constraintViolationSet);
    }


}
