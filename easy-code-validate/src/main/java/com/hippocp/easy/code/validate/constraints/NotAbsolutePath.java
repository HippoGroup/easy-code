package com.hippocp.easy.code.validate.constraints;


import com.hippocp.easy.code.validate.internal.constraintvalidators.bv.string.NotAbsolutePathForString;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 校验是否为绝对路径<br>
 *
 * @author ZhouYifan
 * @date 2022/2/6
 */
@Documented
@Constraint(validatedBy = {NotAbsolutePathForString.class})
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NotAbsolutePath.List.class)
@ReportAsSingleViolation
public @interface NotAbsolutePath {

    String message() default "{easy.code.validation.constraints.NotAbsolutePath.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 在同一元素上定义多个注解。
     */
    @Target({FIELD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NotAbsolutePath[] value();
    }

}
