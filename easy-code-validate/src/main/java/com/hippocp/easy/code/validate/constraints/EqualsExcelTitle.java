package com.hippocp.easy.code.validate.constraints;


import com.hippocp.easy.code.validate.internal.constraintvalidators.bv.excel.ExcelTitleEqualsForMultipartFile;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 校验Excel表格.xlsx文件标题是否与给定标题相同<br>
 * 已包含校验注解 {@link NotNull} 和 {@link IsExcelXLSX}
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
@Documented
@Constraint(validatedBy = {ExcelTitleEqualsForMultipartFile.class})
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EqualsExcelTitle.List.class)
@NotNull
@IsExcelXLSX
@ReportAsSingleViolation
public @interface EqualsExcelTitle {

    /**
     * Excel表格标题
     *
     * @return {@link String}数组
     */
    String[] excelTitles() default {};

    /**
     * Excel表格标题所在行号，默认值为0
     *
     * @return int
     */
    int excelTitleRowNum() default 0;

    String message() default "{easy.code.validation.constraints.EqualsExcelTitle.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 可以在同一元素上定义多个@EqualsExcelTitle注解。
     */
    @Target({FIELD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EqualsExcelTitle[] value();
    }

}
