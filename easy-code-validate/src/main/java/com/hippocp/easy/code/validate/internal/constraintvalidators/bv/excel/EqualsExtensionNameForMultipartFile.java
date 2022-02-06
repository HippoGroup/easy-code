package com.hippocp.easy.code.validate.internal.constraintvalidators.bv.excel;


import cn.hutool.core.util.ArrayUtil;
import com.hippocp.easy.code.util.file.MultipartFileUtil;
import com.hippocp.easy.code.validate.constraints.EqualsExtensionName;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@link EqualsExtensionName}注解 {@link MultipartFile}校验器
 * multipartFile源文件扩展名与extensionNames数组中的任意一个扩展名匹配则通过校验
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
public class EqualsExtensionNameForMultipartFile implements ConstraintValidator<EqualsExtensionName, MultipartFile> {

    private String[] extensionNames;

    /**
     * 可通过此方法取得注解中的参数值，并初始化本类中的属性以供使用
     *
     * @param parameters 注解中的参数对象
     */
    @Override
    public void initialize(EqualsExtensionName parameters) {
        this.extensionNames = parameters.extensionNames();
    }

    /**
     * multipartFile文件扩展名与extensionNames数组中的任意一个扩展名匹配，则通过校验<br>
     * multipartFile为null，则通过校验
     *
     * @param multipartFile 待校验{@link MultipartFile}
     * @param context       约束校验上下文，可提供上下文数据和操作
     * @return 布尔值是否通过校验，true-通过，false-不通过
     */
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        // 多部分文件为空则不进行校验，返回true
        if (multipartFile == null) {
            return true;
        }
        // multipartFile源文件扩展名与extensionNames数组中的任意一个扩展名匹配则通过校验
        boolean equalsFileSuffix = MultipartFileUtil.isEqualsFileSuffix(multipartFile, extensionNames);

        if (!equalsFileSuffix) {
            // 自定义ConstraintValidatorContext允许为插值设置额外的消息参数。
            HibernateConstraintValidatorContext hibernateContext =
                    context.unwrap(HibernateConstraintValidatorContext.class);
            // 数组转换为字符串
            String arrayJoin = ArrayUtil.join(extensionNames, " ");
            // 不禁用默认违反约束提示消息，仅仅添加一个插值属性
            // 通过使用表达式变量，Hibernate Validator 可以正确处理转义并且不会执行 EL 表达式。
            hibernateContext.addExpressionVariable("extensionNameRightHint", arrayJoin);
        }

        return equalsFileSuffix;
    }

}
