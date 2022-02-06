package com.hippocp.easy.code.util.excel;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

/**
 * 自定义字符串格式化回调接口<br>
 * 如需自定义字符串格式化，请实现该接口中的方法<br>
 * 校验器将主动按入参主动调用，参考方法如下：<br>
 * {@link ExcelValidateUtil#readExcelDataAndValidate(MultipartFile, int, int, Map, Class, Set, CustomValidAssignmentStrHandler, CustomFormatStrHandler)}
 *
 * @author ZhouYifan
 * @date 2021/1/8
 */
@FunctionalInterface
public interface CustomFormatStrHandler {

    /**
     * 自定义字符串格式化回调方法
     *
     * @param str 字符串
     * @return 处理完成的字符串
     */
    String format(String str);

}
