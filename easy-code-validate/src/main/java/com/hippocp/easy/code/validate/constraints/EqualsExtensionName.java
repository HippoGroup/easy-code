package com.hippocp.easy.code.validate.constraints;


import com.hippocp.easy.code.validate.internal.constraintvalidators.bv.excel.EqualsExtensionNameForMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 校验{@link MultipartFile}文件是否为给定扩展名<br>
 * 如：.xlsx<br>
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
@Documented
@Constraint(validatedBy = {EqualsExtensionNameForMultipartFile.class})
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EqualsExtensionName.List.class)
@ReportAsSingleViolation
public @interface EqualsExtensionName {

    String message() default "{easy.code.validation.constraints.EqualsExtensionName.message}";

    /**
     * 想要匹配的文件扩展名，校验规则为满足其一文件扩展名即可<br>
     * 若想要仅匹配一个文件扩展名，请仅填写一个文件扩展名
     *
     * @return String数组
     */
    String[] extensionNames() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 在同一元素上定义多个@EqualsExtensionName注解。
     */
    @Target({FIELD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EqualsExtensionName[] value();
    }

}
