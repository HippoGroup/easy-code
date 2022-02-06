package com.hippocp.easy.code.util.excel;

import com.hippocp.easy.code.util.domain.CustomValidatorResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 自定义校验与赋值回调接口<br>
 * 如需自定义字符串数据校验与重新赋值，请实现该接口中的方法<br>
 * 校验器将主动按一下方法入参主动调用<br>
 * {@link ExcelValidateUtil#cellValid(MultipartFile, Map, Class, ExcelValidateOptions)}
 *
 * @author ZhouYifan
 * @date 2021/1/8
 */
@FunctionalInterface
public interface CustomValidAssignmentStrHandler {

    /**
     * 字符串校验与重新赋值
     *
     * @param fieldType         属性类型
     * @param fieldName         JavaBean属性名称
     * @param columnValue       列值，即单元格内数据值
     * @param columnName        列名，即表格标题
     * @param readabilityRowNum 可读性行号
     * @param tempStorageMap    临时存储Map，存放自定义的新值，避免直接修改原Map发送并发修改异常
     * @return {@link CustomValidatorResult}
     */
    CustomValidatorResult<?> validAssignmentStr(
            Class<?> fieldType,
            String fieldName,
            String columnValue,
            String columnName,
            int readabilityRowNum,
            Map<String, Object> tempStorageMap
    );

}
