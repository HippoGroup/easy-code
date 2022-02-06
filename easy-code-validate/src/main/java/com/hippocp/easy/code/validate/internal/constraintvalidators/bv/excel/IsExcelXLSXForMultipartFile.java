package com.hippocp.easy.code.validate.internal.constraintvalidators.bv.excel;


import com.hippocp.easy.code.util.excel.ExcelValidateUtil;
import com.hippocp.easy.code.validate.constraints.IsExcelXLSX;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@link IsExcelXLSX}注解 {@link MultipartFile}校验器
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
public class IsExcelXLSXForMultipartFile implements ConstraintValidator<IsExcelXLSX, MultipartFile> {

    /**
     * 可通过此方法取得注解中的参数值，并初始化本类中的属性以供使用
     *
     * @param parameters 注解中的参数对象
     */
    @Override
    public void initialize(IsExcelXLSX parameters) {

    }

    /**
     * multipartFile文件扩展名为.xlsx，则通过校验<br>
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
        // 文件扩展名等于 .xlsx 即通过校验
        return ExcelValidateUtil.isExcelXLSX(multipartFile);
    }

}
