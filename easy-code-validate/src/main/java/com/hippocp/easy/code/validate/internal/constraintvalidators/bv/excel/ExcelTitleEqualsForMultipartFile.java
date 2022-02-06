package com.hippocp.easy.code.validate.internal.constraintvalidators.bv.excel;

import cn.hutool.core.util.ArrayUtil;
import com.hippocp.easy.code.util.excel.ExcelValidateUtil;
import com.hippocp.easy.code.validate.constraints.EqualsExcelTitle;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@link EqualsExcelTitle}注解 {@link MultipartFile}校验器
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
public class ExcelTitleEqualsForMultipartFile implements ConstraintValidator<EqualsExcelTitle, MultipartFile> {

    /**
     * 表格标题
     */
    private String[] excelTitles;

    /**
     * 可通过此方法取得注解中的参数值，并初始化本类中的属性以供使用
     *
     * @param parameters 注解中的参数对象
     */
    @Override
    public void initialize(EqualsExcelTitle parameters) {
        this.excelTitles = parameters.excelTitles();
    }

    /**
     * 校验Excel表格是否具体给定表头<br>
     * multipartFile为null则通过校验
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

        // Excel表格标题与参照标题相同，则通过校验，只校验表格标题是否存在，而不在乎其顺序
        boolean excelTitleEquals = ExcelValidateUtil.excelTitleEquals(multipartFile, excelTitles);

        if (!excelTitleEquals) {
            // 自定义ConstraintValidatorContext允许为插值设置额外的消息参数。
            HibernateConstraintValidatorContext hibernateContext =
                    context.unwrap(HibernateConstraintValidatorContext.class);
            // 禁用默认违反约束提示消息
            hibernateContext.disableDefaultConstraintViolation();
            // 数组转换为字符串
            String arrayJoin = ArrayUtil.join(excelTitles, "，");
            // 信息模板
            String msgTemplate = "Excel表格第{excelTitleRowNum}行标题不符合要求，" +
                    "请注意检查标题是否包含空白字符例如：空格、制表符、换行符等，正确表格标题如下：${excelTitleRightHint}";
            // 通过使用表达式变量，Hibernate Validator 可以正确处理转义并且不会执行 EL 表达式。
            hibernateContext.addExpressionVariable("excelTitleRightHint", arrayJoin)
                    // 原本消息模板加上新插值消息
                    .buildConstraintViolationWithTemplate(msgTemplate)
                    .addConstraintViolation();
        }

        return excelTitleEquals;
    }

}
