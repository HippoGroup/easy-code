package com.hippocp.easy.code.util.excel.handler;

import cn.hutool.core.util.StrUtil;
import com.hippocp.easy.code.util.excel.CustomFormatStrHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个预设的自定义字符串格式化处理器<br>
 * 功能为除去字符串头尾部的空白，如果字符串是null，依然返回null。<br>
 * 可以除去英文字符集之外的其它空白，如中文空格。<br>
 *
 * @author ZhouYifan
 * @date 2022/1/13
 */
public class FormatStrDefaultHandler implements CustomFormatStrHandler {

    private static final Logger log = LoggerFactory.getLogger(FormatStrDefaultHandler.class);

    @Override
    public String format(String str) {
        if (log.isDebugEnabled()) {
            log.debug("----------执行自定义字符串格式化处理器----------");
        }
        String trim = StrUtil.trim(str);
        if (log.isDebugEnabled()) {
            log.debug("字符串 {} 转换为 {}", str, trim);
        }
        return trim;
    }

}
