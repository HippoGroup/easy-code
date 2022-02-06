package com.hippocp.easy.code.util.parse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.hippocp.easy.code.domain.validate.ConstraintError;
import com.hippocp.easy.code.util.domain.EasyConstraintViolationImpl;
import com.hippocp.easy.code.util.string.StringUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 违反约束错误解析工具类<br>
 * 可以将{@link ConstraintViolation}或者{@link BindingResult}解析为{@link ConstraintError}<br>
 * 包含 ConstraintError 构建日志信息方法
 *
 * @author ZhouYifan
 * @date 2022/1/8
 */
public class ConstraintErrorParseUtil {

    /**
     * 获得违反约束错误对象列表
     *
     * @param result BindingResult
     * @return 约束错误对象列表，否则返回null
     */
    public static List<ConstraintError> getConstraintError(BindingResult result) {

        if (result.hasErrors()) {
            // 获取与字段相关的所有错误。
            List<FieldError> list = result.getFieldErrors();
            // 自定义约束错误对象列表，用于提供违反约束参数名、参数值、消息
            List<ConstraintError> constraintErrorList = new ArrayList<>();
            // 遍历解析 FieldError 对象
            for (FieldError error : list) {
                // 参数名
                String argsName = error.getField();
                // 参数值
                Object argsValue = error.getRejectedValue();
                // 违反约束信息
                String message = error.getDefaultMessage();
                // 如果参数值是数组则使用ArrayUtil.toString()
                if (argsValue instanceof Object[]) {
                    argsValue = ArrayUtil.toString(argsValue);
                }
                // 如果是多部分文件，则不要转json
                if (argsValue instanceof MultipartFile) {
                    argsValue = "MultipartFile";
                }
                // 实例化自定义约束错误对象
                ConstraintError constraint = new ConstraintError(argsName, argsValue, message);
                // 加入列表
                constraintErrorList.add(constraint);
            }

            return constraintErrorList;

        }

        return null;

    }


    /**
     * 获得违反约束错误对象
     *
     * @param violation 描述约束违规。 此对象公开约束违规上下文以及描述违规的消息。
     * @return 约束错误对象 提供违反约束参数名、参数值、消息
     */
    public static ConstraintError getConstraintError(ConstraintViolation<?> violation) {
        // 获取参数名
        String argsName;
        // 是自定义的 EasyConstraintViolationImpl
        if (violation instanceof EasyConstraintViolationImpl) {
            argsName = ((EasyConstraintViolationImpl<?>) violation).getArgName();
        } else {
            // 从rootBean到值的属性路径
            Path propertyPath = violation.getPropertyPath();
            String pathStr = propertyPath.toString();
            // 参数名
            argsName = pathStr.substring(pathStr.indexOf(".") + 1);
        }

        // 违反约束信息
        String message = violation.getMessage();
        // 参数值
        Object argsValue = violation.getInvalidValue();
        // 如果参数值是数组则使用ArrayUtil.toString()
        if (argsValue instanceof Object[]) {
            argsValue = ArrayUtil.toString(argsValue);
        }
        // 如果是多部分文件，则不要转json
        if (argsValue instanceof MultipartFile) {
            argsValue = "MultipartFile";
        }
        // 实例化自定义约束错误对象
        ConstraintError constraintError = new ConstraintError(argsName, argsValue, message);
        return constraintError;
    }


    /**
     * 获得违反约束错误对象列表
     *
     * @param violationSet ConstraintViolation Set
     * @return 约束错误对象列表，否则返回null
     */
    public static List<ConstraintError> getConstraintError(Set<ConstraintViolation<?>> violationSet) {
        // 空集合返回null
        if (CollUtil.isEmpty(violationSet)) {
            return null;
        }
        // 正常解析
        List<ConstraintError> list = new ArrayList<>();
        for (ConstraintViolation<?> violation : violationSet) {
            // 获得违反约束错误对象
            ConstraintError constraintError = getConstraintError(violation);
            // 加入列表
            list.add(constraintError);
        }

        return list;
    }


    /**
     * 获得违反约束错误对象列表
     * 参数适配 T，以应对二义性
     *
     * @param violationSet ConstraintViolation Set
     * @param <T>          JavaBean
     * @return 约束错误对象列表，否则返回null
     */
    public static <T> List<ConstraintError> getConstraintErrorForType(Set<ConstraintViolation<T>> violationSet) {
        // 空集合返回null
        if (CollUtil.isEmpty(violationSet)) {
            return null;
        }
        // 正常解析
        List<ConstraintError> list = new ArrayList<>();
        for (ConstraintViolation<T> violation : violationSet) {
            // 获得违反约束错误对象
            ConstraintError constraintError = getConstraintError(violation);
            // 加入列表
            list.add(constraintError);
        }

        return list;
    }


    /**
     * ConstraintError 构建日志信息
     *
     * @param list ConstraintError列表
     * @return 日志信息
     */
    public static String buildLogMessage(List<ConstraintError> list) {
        // 如果为null，返回空字符串
        if (CollUtil.isEmpty(list)) {

            return "";

        }

        StringBuffer log = new StringBuffer();
        // 获取当前系统换行符
        String newLine = StringUtil.presentOsLineBreak();
        for (ConstraintError error : list) {
            // 一行日志信息
            String logMessage = StrUtil.format("参数名：{}，参数值：{}，违反约束信息：{}。{}",
                    error.getArgsName(), error.getArgsValue(), error.getMessage(), newLine);
            // 加入整体日志中
            log.append(logMessage);
        }

        return log.toString();

    }

    private ConstraintErrorParseUtil() {
    }

}
