package com.hippocp.easy.code.exception.aspect.advice;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolationException;

/**
 * Exception异常处理增强类
 *
 * @author ZhouYifan
 * @date 2021/8/24
 */
@Slf4j
@Aspect
@Component
public class ExceptionHandlerAdvice {

    @Pointcut("@annotation(com.hippocp.easy.code.exception.aspect.annotation.ExceptionHandlerAble)")
    public void exceptionHandlerAblePointCut() {
    }

    @AfterThrowing(value = "exceptionHandlerAblePointCut()", throwing = "e")
    public void exceptionHandler(JoinPoint joinPoint, Exception e) {
        // 获取当前系统名称
        String osName = System.getProperty("os.name");
        // 是否Windows系统
        boolean isWindowsOs = osName.toLowerCase().contains("windows");
        String newLine = "\n";
        // 是Windows系统，使用Windows系统换行符
        if (isWindowsOs) {
            newLine = "\r\n";
        }

        // 获取异常栈首个元素
        StackTraceElement firstElement = e.getStackTrace()[0];
        String className = firstElement.getClassName();
        String methodName = firstElement.getMethodName();
        String exceptionLog = StrUtil.format("类 [{}] 方法 [{}] 出现异常，异常摘要：{}",
                className, methodName, e.getMessage());

        // 参数消息日志
        StringBuilder argsMessage = new StringBuilder();
        // 取参数名和参数值
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        // 循环拼接参数日志
        for (int i = 0; i < args.length; i++) {
            String argLog = StrUtil.format("参数名：{}，参数值：{}。{}", argNames[i], args[i], newLine);
            argsMessage.append(argLog);
        }
        // 因为MethodArgumentNotValidException是BindException子类，所以判断实例是否为BindException即可
        boolean isArgsValidateException = e instanceof BindException || e instanceof ConstraintViolationException;
        if (isArgsValidateException) {
            // 参数校验异常，记录warn级别日志
            log.warn(exceptionLog);
            log.warn(argsMessage.toString());
        } else {
            // 其它异常，记录error级别日志
            log.error(exceptionLog);
            log.error(argsMessage.toString());
        }

    }

}
