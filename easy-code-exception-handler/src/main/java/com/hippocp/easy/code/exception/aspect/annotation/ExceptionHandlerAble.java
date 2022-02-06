package com.hippocp.easy.code.exception.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异常信息处理能力注解<br>
 * 当被标注方法发生Exception异常并且未被捕捉时，会记录日志保护案发现场
 *
 * @author ZhouYifan
 * @date 2021/8/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionHandlerAble {

}
