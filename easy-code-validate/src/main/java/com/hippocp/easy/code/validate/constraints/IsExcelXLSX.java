package com.hippocp.easy.code.validate.constraints;


import com.hippocp.easy.code.validate.internal.constraintvalidators.bv.excel.IsExcelXLSXForMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 校验{@link MultipartFile}文件是否为.xlsx格式<br>
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
@Documented
@Constraint(validatedBy = {IsExcelXLSXForMultipartFile.class})
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(IsExcelXLSX.List.class)
@ReportAsSingleViolation
public @interface IsExcelXLSX {

    String message() default "{easy.code.validation.constraints.IsExcelXLSX.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 在同一元素上定义多个@IsExcelXSLS注解。
     */
    @Target({FIELD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        IsExcelXLSX[] value();
    }

}
