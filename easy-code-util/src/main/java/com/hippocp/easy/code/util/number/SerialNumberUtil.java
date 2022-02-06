package com.hippocp.easy.code.util.number;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

/**
 * 序号生成工具类
 *
 * @author ZhouYifan
 * @date 2021/7/30
 */
public class SerialNumberUtil {

    /**
     * 生成序号，时间戳 + UUID，时间戳格式：yyyyMMddHHmmssSSS
     *
     * @return 序号字符串
     */
    public static String getTimeStampAndUuidStr() {
        // 生成时间戳
        String timeStr = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        // 生成UUID
        String UuidStr = IdUtil.fastSimpleUUID();
        StringBuffer timeStampAndUuidStr = new StringBuffer(timeStr);
        timeStampAndUuidStr.append(UuidStr);
        return timeStampAndUuidStr.toString();
    }

    /**
     * 生成编号，时间戳 + 随机字符串包含数字，时间戳格式：yyyyMMddHHmmssSSS，随机数：长度可以指定
     *
     * @param length 随机字符串长度 指定为15即可生成32位字符串
     * @return 编号
     */
    public static String getTimeStampAndRandomStr(int length) {
        // 生成时间戳
        String timeStr = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        // 生成随机字符串
        String randomStr = RandomUtil.randomString(length);
        StringBuffer timeStrAndRandomStr = new StringBuffer(timeStr);
        timeStrAndRandomStr.append(randomStr);
        return timeStrAndRandomStr.toString();
    }

    private SerialNumberUtil() {
    }

}
