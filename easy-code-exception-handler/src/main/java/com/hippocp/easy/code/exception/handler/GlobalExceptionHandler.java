package com.hippocp.easy.code.exception.handler;

import cn.hutool.core.util.StrUtil;
import com.hippocp.easy.code.domain.response.CreateResponseWrapper;
import com.hippocp.easy.code.domain.response.ErrorCodeEnum;
import com.hippocp.easy.code.domain.response.ResponseWrapper;
import com.hippocp.easy.code.domain.validate.ConstraintError;
import com.hippocp.easy.code.exception.config.ExceptionHandlerProperties;
import com.hippocp.easy.code.util.parse.ConstraintErrorParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 全局异常处理器，可处理的异常如下：<br>
 * {@link HttpRequestMethodNotSupportedException}<br>
 * {@link HttpMessageNotReadableException}<br>
 * {@link HttpMediaTypeNotSupportedException}<br>
 * {@link MissingServletRequestParameterException}<br>
 * {@link NoHandlerFoundException}<br>
 * {@link BindException}<br>
 * {@link MethodArgumentNotValidException}<br>
 * {@link ConstraintViolationException}<br>
 * {@link Exception}<br>
 *
 * @author ZhouYifan
 * @date 2021/9/29
 */
@RestControllerAdvice
public class GlobalExceptionHandler implements Ordered {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ExceptionHandlerProperties exceptionHandlerProperties;

    /**
     * 请求方法不支持异常处理
     *
     * @param e {@link HttpRequestMethodNotSupportedException}
     * @return {@link ResponseWrapper}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseWrapper<String> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        String requestMethod = e.getMethod();
        String[] supportedMethods = e.getSupportedMethods();
        String message = StrUtil.format("请求方法不支持，当前请求方法为 {}，支持的请求方法如下：{}",
                requestMethod, Arrays.toString(supportedMethods));
        logger.warn(message, e);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessage(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, message);
    }

    /**
     * 缺少所需的请求体异常处理
     * Required request body is missing:
     *
     * @param e {@link HttpMessageNotReadableException}
     * @return {@link ResponseWrapper}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseWrapper<String> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        String message = "缺少所需的请求体";
        logger.warn(message, e);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessage(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, message);
    }

    /**
     * 请求参数类型不支持异常，当客户端 POST、Delete、PUT 或 PATCH 请求处理程序不支持的类型的内容时引发异常
     * Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
     *
     * @param e {@link HttpMediaTypeNotSupportedException}
     * @return {@link ResponseWrapper}
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseWrapper<String> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        MediaType contentType = e.getContentType();
        String typeStr = Objects.toString(contentType);
        String message = StrUtil.format("请求参数类型不支持，当前请求参数类型为 {}", typeStr);
        logger.warn(message, e);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessage(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, message);
    }

    /**
     * 处理参数缺少异常
     * Required String parameter 'abc' is not present
     *
     * @param e {@link MissingServletRequestParameterException}
     * @return {@link ResponseWrapper}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseWrapper<String> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        String parameterName = e.getParameterName();
        String parameterType = e.getParameterType();
        String message = StrUtil.format("必需的参数 {} 不存在（类型 {}）", parameterName, parameterType);
        logger.warn(message, e);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessage(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, message);
    }

    /**
     * 处理404异常
     *
     * @param e       NoHandlerFoundException
     * @param request HttpServletRequest
     * @return ResponseWrapper
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseWrapper<String> noHandlerFoundExceptionHandler(NoHandlerFoundException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String message = StrUtil.format("接口 [{}] 不存在", requestURI);
        logger.warn(message, e);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessage(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, message);
    }

    /**
     * 处理绑定异常
     * 出现场景:
     * bean中有字段验证, Validated Valid 注解指定要验证这个bean对象.
     * 当前端传过来一个表单格式(Content-Type: multipart/form-data)的数据, 后台通过需要验证的bean对象接收的时候.
     * 加入验证不通过, 则会报此异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseWrapper<List<ConstraintError>> bindExceptionHandler(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<ConstraintError> list = ConstraintErrorParseUtil.getConstraintError(bindingResult);
        // 日志信息
        String log = ConstraintErrorParseUtil.buildLogMessage(list);
        // 记录用户输入参数错误
        logger.warn("捕捉到 BindException 异常");
        logger.warn(log);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessageData(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, list);
    }

    /**
     * 处理请求对象属性不满足校验规则的异常信息
     * 请求体绑定异常
     * 与BindException类似, 不同的是因为什么触发, 当Controller接收的是一个json格式, @RequestBody接收参数时,
     * 验证失败会抛出此异常
     *
     * @param exception MethodArgumentNotValidException
     * @return ResponseWrapper
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseWrapper<List<ConstraintError>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<ConstraintError> list = ConstraintErrorParseUtil.getConstraintError(bindingResult);
        // 日志信息
        String log = ConstraintErrorParseUtil.buildLogMessage(list);
        // 记录用户输入参数错误
        logger.warn("捕捉到 MethodArgumentNotValidException 异常");
        logger.warn(log);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessageData(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, list);
    }

    /**
     * 处理请求单个参数不满足校验规则的异常信息
     * 触发场景
     * Controller中的参数校验失败会抛出此类异常. 类头部需要添加@Valited注解
     *
     * @param exception ConstraintViolationException
     * @return ResponseWrapper
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseWrapper<List<ConstraintError>> constraintViolationExceptionHandler(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        List<ConstraintError> list = ConstraintErrorParseUtil.getConstraintError(constraintViolations);
        // 日志信息
        String log = ConstraintErrorParseUtil.buildLogMessage(list);
        // 记录用户输入参数错误
        logger.warn("捕捉到 ConstraintViolationException 异常");
        logger.warn(log);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessageData(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR, list);
    }

    /**
     * 处理Exception，即未定义的其他异常信息
     *
     * @param exception Exception
     * @return ResponseWrapper
     */
    @ExceptionHandler(Exception.class)
    public ResponseWrapper<String> exceptionHandler(Exception exception,
                                                    HttpServletRequest request,
                                                    HandlerMethod handlerMethod) {
        String requestURI = request.getRequestURI();
        // 返回响应信息
        String message = StrUtil.format("接口 [{}] 内部错误，请联系管理员", requestURI);
        // 异常信息
        String exceptionMessage = exception.getMessage();
        // 日志信息
        String logMessage;
        if (handlerMethod != null) {
            String className = handlerMethod.getBean().getClass().getName();
            String methodName = handlerMethod.getMethod().getName();
            logMessage = StrUtil.format("接口 [{}] 出现异常，方法：{}.{}，异常摘要：{}",
                    requestURI, className, methodName, exceptionMessage);
        } else {
            logMessage = exceptionMessage;
        }
        logger.error(logMessage, exception);
        return CreateResponseWrapper.requestFailedReturnErrorCodeMessage(ErrorCodeEnum.SYSTEM_RUN_ERROR, message);
    }

    /**
     * 业务异常处理器，这是一个空方法，可继承该类后自定义实现
     */
    public void serviceExceptionHandler() {
    }

    @Override
    public int getOrder() {
        return exceptionHandlerProperties.getGlobalExceptionHandlerOrder();
    }
}
