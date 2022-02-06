package com.hippocp.easy.code.util.date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date转换工具类，包含转换方法和判断类型方法
 *
 * @author ZhouYifan
 * @date 2022/1/11
 */
public class DateParseUtil {


    /**
     * 日志
     */
    public static final Logger log = LoggerFactory.getLogger(DateParseUtil.class);


    /**
     * 时间格式校验正则，目标yyyy/M/d（1位或2位月份、日期） HH:mm:ss，利用预编译功能，加快正则匹配速度
     */
    private static final Pattern PATTERN =
            Pattern.compile("(\\d{4}/(\\d|\\d{2})/(\\d|\\d{2})\\s(\\d|\\d{2}):\\d{2}:\\d{2})");


    /**
     * 字符串转换为Date<br>
     * 特殊处理格式 yyyy/M/d HH:mm:ss 为匹配Excel表格日期格式<br>
     * 若不匹配上述日期格式则使用通用转换，可应对16种时间格式，见parse方法注释<br>
     *
     * @param dateStr 日期字符串
     * @return {@link Date}
     * @see DateUtil#parse(CharSequence)
     */
    public static Date parseDate(String dateStr) {
        Date date = null;
        Matcher matcher = PATTERN.matcher(dateStr);
        try {
            if (matcher.matches()) {
                // 匹配正则，使用特殊格式转换
                date = DateUtil.parse(dateStr, "yyyy/M/d HH:mm:ss");
            } else {
                // 不匹配正则，使用通用转换，可应对16种时间格式，见parse方法注释
                date = DateUtil.parse(dateStr);
            }
        } catch (Exception e) {
            String msg = StrUtil.format("字符串 -》转换为 java.util.Date 错误，参数值：{}", dateStr);
            log.warn(msg, e);
        }
        return date;
    }


    /**
     * 是 java.util.Date 类型或其子类
     *
     * @param fieldType {@link Class}属性类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isDateType(Class<T> fieldType) {
        return fieldType.isAssignableFrom(Date.class);
    }


    /**
     * 不是 java.util.Date 类型或其子类
     *
     * @param fieldType {@link Class}属性类型
     * @param <T>       JavaBean
     * @return true-是 false-否
     */
    public static <T> boolean isNotDateType(Class<T> fieldType) {
        return !isDateType(fieldType);
    }


    private DateParseUtil() {
    }


}
