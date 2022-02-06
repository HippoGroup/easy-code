package com.hippocp.easy.code.validate.internal.constraintvalidators.bv.string;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.hippocp.easy.code.validate.constraints.NotAbsolutePath;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@link NotAbsolutePath}注解 {@link String}校验器
 *
 * @author ZhouYifan
 * @date 2022/2/6
 */
public class NotAbsolutePathForString implements ConstraintValidator<NotAbsolutePath, String> {

    /**
     * 可通过此方法取得注解中的参数值，并初始化本类中的属性以供使用
     *
     * @param parameters 注解中的参数对象
     */
    @Override
    public void initialize(NotAbsolutePath parameters) {

    }

    /**
     * 是绝对路径，则通过校验<br>
     * path为null或空字符串，则通过校验
     *
     * @param path    待校验{@link String}
     * @param context 约束校验上下文，可提供上下文数据和操作
     * @return 布尔值是否通过校验，true-通过，false-不通过
     */
    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        // path为null或空字符串，则通过校验
        if (StrUtil.isBlank(path)) {
            return true;
        }
        // 是绝对路径，则通过校验
        return FileUtil.isAbsolutePath(path);
    }

}
