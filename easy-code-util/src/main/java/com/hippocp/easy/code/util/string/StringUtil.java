package com.hippocp.easy.code.util.string;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.hippocp.easy.code.util.date.DateParseUtil;
import com.hippocp.easy.code.util.domain.EasyConstraintViolationImpl;
import com.hippocp.easy.code.util.domain.RightFormatMessage;
import com.hippocp.easy.code.util.excel.ExcelValidateUtil;
import com.hippocp.easy.code.util.number.NumUtil;
import com.hippocp.easy.code.util.system.SystemUtil;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类，包含约束违反信息模板、正确格式提示消息常量与工具方法
 *
 * @author ZhouYifan
 * @date 2022/1/11
 */
public class StringUtil {

    /**
     * Excel表格内容未填写约束违反信息模板<br>
     * 示例：@NotBlank(message = "机构唯一编码" + NOT_BLANK_MSG_TEMPLATE)
     */
    public static final String NOT_BLANK_MSG_TEMPLATE = "未填写，错误内容: ${validatedValue != null ? validateValue : '无内容'}";

    /**
     * 整数校验正确格式提示消息
     */
    public static final String INTEGER_RIGHT_FORMAT = "必须填写整数";

    /**
     * 小数校验正确格式提示消息
     */
    public static final String DOUBLE_RIGHT_FORMAT = "必须填写小数";

    /**
     * 日期校验正确格式提示消息
     */
    public static final String DATE_RIGHT_FORMAT =
            "正确示例（时间24小时制）示例一：2021/9/5 14:30:00 示例二：2021/09/05 14:30:00 示例三：2021年09月05日 14时30分00秒";


    /**
     * 拼接类 get 方法名称
     *
     * @param fieldName 属性名称
     * @return get方法名称
     */
    public static String jointGetMethodName(String fieldName) {
        final String get = "get";
        String initial = fieldName.substring(0, 1);
        String uppercaseInitial = initial.toUpperCase();
        String surplusLetter = fieldName.substring(1);
        // 方法名
        return StrUtil.format("{}{}{}", get, uppercaseInitial, surplusLetter);
    }

    /**
     * 获取当前系统换行符
     *
     * @return Windows系统换行符 "\r\n"<br>
     * Linux macOS 系统换行符 "\n"
     */
    public static String presentOsLineBreak() {
        // 是否为Windows系统
        boolean isWindowsOs = SystemUtil.isWindows();
        // 是Windows系统，使用Windows系统换行符
        if (isWindowsOs) {
            return "\r\n";
        } else {
            return "\n";
        }
    }

    /**
     * 获取字符串的开头零字符串<br>
     * 例如 00101 得到 00，0101 得到 0<br>
     *
     * @param text 待获取开头零的字符串
     * @return {@link String} 零字符串
     */
    public static String getOutsetContinuousZero(String text) {
        char[] chars = text.toCharArray();
        StringBuffer zeroStr = new StringBuffer();

        // 第一个字符不是零
        boolean firstCharIsNotZero = chars[0] != '0';
        if (firstCharIsNotZero) {

            // 结束方法
            return zeroStr.toString();

        }

        // 遍历字符数组
        for (int i = 0; i < chars.length; i++) {

            // 是第一个字符，下标是0代表是第一个字符
            boolean isFirstChar = i == 0;
            // 是第一个字符并且当前字符是零
            if (isFirstChar) {

                // 直接加入StringBuffer
                zeroStr.append(chars[i]);

            } else {
                // 是第二个以及后续字符
                // 前一个字符索引
                int lastCharIndex = i - 1;
                // 前一个字符是零
                boolean lastCharIsZero = chars[lastCharIndex] == '0';
                // 当前字符是零
                boolean isZero = chars[i] == '0';
                // 如果前一位字符是零
                if (lastCharIsZero) {

                    // 并且当前字符是零
                    if (isZero) {
                        // 加入StringBuffer
                        zeroStr.append(chars[i]);
                    } else {
                        // 当前字符不是零，结束方法
                        break;
                    }

                } else {
                    // 前一位字符不是零，结束方法
                    break;
                }
                // if else 结束
            }
            // 循环结束
        }

        return zeroStr.toString();

    }

    /**
     * 初始化正确格式提示消息
     *
     * @return {@link RightFormatMessage}
     */
    public static RightFormatMessage createRightFormatMessage() {
        return new RightFormatMessage(
                INTEGER_RIGHT_FORMAT,
                DOUBLE_RIGHT_FORMAT,
                DATE_RIGHT_FORMAT
        );
    }

    /**
     * 正则目标，制表符、windows与unix换行符，利用预编译功能，加快正则匹配速度
     */
    private static final Pattern PATTERN = Pattern.compile("(\t|\r|\n|\r\n|\n\r)");

    /**
     * 去除字符串前后的英文空格，以及字符串内容中的制表符、windows与unix换行符
     *
     * @param str 原字符串
     * @return 处理完毕字符串
     */
    public static String trimBlankCharacter(String str) {
        // 去除前后空格，包括中文空格
        String trim = StrUtil.trim(str);
        // 去除制表符、windows与unix换行符
        Matcher matcher = PATTERN.matcher(trim);
        return matcher.replaceAll("");
    }

    /**
     * 是{@link String}类型
     *
     * @param fieldType {@link Class}类类型
     * @return true-是 false-否
     */
    public static boolean isStringType(Class<?> fieldType) {
        return fieldType.isAssignableFrom(String.class);
    }

    /**
     * 不是{@link String}类型
     *
     * @param fieldType {@link Class}类类型
     * @return true-是 false-否
     */
    public static boolean isNotStringType(Class<?> fieldType) {
        return !isStringType(fieldType);
    }

    /**
     * 字符串 -》数字（整数）<br>
     * 可以转换为整数则什么都不做<br>
     * 不可以转换则返回约束违反信息，是否包含可读性行号由参数 isReadabilityRowNumJoin 决定
     *
     * @param fieldType               属性类型
     * @param fieldName               属性名
     * @param dataStr                 表格数据字符串，即Map中value
     * @param title                   表格标题，即Map中key
     * @param isReadabilityRowNumJoin 是否在违反约束信息中拼接，违反约束数据所在的Excel表格行号
     * @param readabilityRowNum       可读性行号
     * @param <T>                     JavaBean
     * @return {@link EasyConstraintViolationImpl}
     */
    public static <T> EasyConstraintViolationImpl<T> stringToInteger(Class<?> fieldType,
                                                                     String fieldName,
                                                                     String dataStr,
                                                                     String title,
                                                                     boolean isReadabilityRowNumJoin,
                                                                     int readabilityRowNum) {
        // 字符串 -》数字（整数）
        // 目标属性类型不是整数类型
        boolean isNotIntegerType = NumUtil.isNotIntegerType(fieldType);
        if (isNotIntegerType) {
            return null;
        }
        // 属性值已经是整数
        boolean isIntegerArgValue = NumberUtil.isInteger(dataStr);
        if (isIntegerArgValue) {
            return null;
        }
        // 目标属性类型是整数类型 并且 属性值不是整数，不区分原始类型包装类型，byte short int long 都算作整数类型
        // 拼接约束违反信息
        String constraintViolationMsg = ExcelValidateUtil.constraintViolationMsgBuild(isReadabilityRowNumJoin, readabilityRowNum,
                title, dataStr, INTEGER_RIGHT_FORMAT);
        // 不通过校验才会返回自定义的ConstraintViolation
        return EasyConstraintViolationImpl.forBeanValidation(constraintViolationMsg, fieldName, dataStr);
    }


    /**
     * 字符串 -》数字（浮点数）<br>
     * 可以转换为浮点数则什么都不做<br>
     * 不可以转换则返回约束违反信息，是否包含可读性行号由参数 isReadabilityRowNumJoin 决定
     *
     * @param fieldType               属性类型
     * @param fieldName               属性名
     * @param dataStr                 表格数据字符串，即Map中value
     * @param title                   表格标题，即Map中key
     * @param isReadabilityRowNumJoin 是否在违反约束信息中拼接，违反约束数据所在的Excel表格行号
     * @param readabilityRowNum       可读性行号
     * @param <T>                     JavaBean
     * @return {@link EasyConstraintViolationImpl}
     */
    public static <T> EasyConstraintViolationImpl<T> stringToDouble(Class<?> fieldType,
                                                                    String fieldName,
                                                                    String dataStr,
                                                                    String title,
                                                                    boolean isReadabilityRowNumJoin,
                                                                    int readabilityRowNum) {
        // 字符串 -》数字（浮点数）
        // 目标属性类型不是浮点数类型
        boolean isNotDoubleType = NumUtil.isNotDoubleType(fieldType);
        if (isNotDoubleType) {
            return null;
        }
        // 属性值已经是浮点数
        boolean isDoubleArgValue = NumberUtil.isDouble(dataStr);
        if (isDoubleArgValue) {
            return null;
        }
        // 目标属性类型是浮点数类型 并且 属性值不是浮点数，不区分原始类型包装类型，float double 都算作浮点数类型
        // 拼接约束违反信息
        String constraintViolationMsg = ExcelValidateUtil.constraintViolationMsgBuild(isReadabilityRowNumJoin, readabilityRowNum,
                title, dataStr, DOUBLE_RIGHT_FORMAT);
        // 不通过校验才会返回自定义的ConstraintViolation
        return EasyConstraintViolationImpl.forBeanValidation(constraintViolationMsg, fieldName, dataStr);
    }


    /**
     * 字符串 -》日期<br>
     * 可以转换为 java.util.Date 则进行转换并且更换Map中的数据，put同一个key更换新value<br>
     * 不可以转换则返回约束违反信息，是否包含可读性行号由参数 isReadabilityRowNumJoin 决定<br>
     *
     * @param fieldType               属性类型
     * @param fieldName               属性名
     * @param dataStr                 表格数据字符串，即Map中value
     * @param title                   表格标题，即Map中key
     * @param isReadabilityRowNumJoin 是否在违反约束信息中拼接，违反约束数据所在的Excel表格行号
     * @param readabilityRowNum       可读性行号
     * @param tempStorageMap          表格数据行列表
     * @param <T>                     JavaBean
     * @return {@link EasyConstraintViolationImpl}
     */
    public static <T> EasyConstraintViolationImpl<T> stringToDate(Class<?> fieldType,
                                                                  String fieldName,
                                                                  String dataStr,
                                                                  String title,
                                                                  boolean isReadabilityRowNumJoin,
                                                                  int readabilityRowNum,
                                                                  Map<String, Object> tempStorageMap) {
        // 字符串 -》日期
        boolean isNotDateType = DateParseUtil.isNotDateType(fieldType);
        // 目标类不是非Date类型
        if (isNotDateType) {
            // 直接返回
            return null;
        }

        // 目标属性类型是 java.util.Date 或其子类
        // 字符串转换为Date
        Date parseDate = DateParseUtil.parseDate(dataStr);
        // 代表能转换
        boolean isParse = parseDate != null;
        if (isParse) {
            // 可以转换则将转换后的日期放入暂存Map，使用同一个key，等待校验器进行合并
            tempStorageMap.put(title, parseDate);
            return null;
        }

        // 属性值不能由字符串转换为 java.util.Date，返回提示消息
        // 拼接约束违反信息
        String constraintViolationMsg = ExcelValidateUtil.constraintViolationMsgBuild(isReadabilityRowNumJoin, readabilityRowNum,
                title, dataStr, DATE_RIGHT_FORMAT);
        // 不通过校验才会返回自定义的ConstraintViolation
        return EasyConstraintViolationImpl.forBeanValidation(constraintViolationMsg, fieldName, dataStr);

    }

    private StringUtil() {
    }
}
