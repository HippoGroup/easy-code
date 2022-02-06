package com.hippocp.easy.code.util.excel;

import com.hippocp.easy.code.util.domain.ExcelReaderTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * Excel表格校验选项类，通过此类定制各种校验选项
 *
 * @author ZhouYifan
 * @date 2022/1/12
 */
@Data
public class ExcelValidateOptions<T> implements Serializable {

    /**
     * 表格标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     */
    private final int headerRowIndex;
    /**
     * 表格起始行（包含，从0开始计数）
     */
    private final int startRowIndex;
    /**
     * 是否返回Excel表格数据
     */
    private final boolean isReturnExcelDataObj;
    /**
     * 是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
     */
    private final boolean isReadabilityRowNumJoin;
    /**
     * Excel表格读取器类型枚举类
     */
    private final ExcelReaderTypeEnum excelReaderTypeEnum;
    /**
     * 自定义校验与赋值处理器
     */
    private CustomValidAssignmentStrHandler customValidAssignmentStrHandler;
    /**
     * 自定义字符串格式化处理器
     */
    private final CustomFormatStrHandler customFormatStrHandler;
    /**
     * 自定义校验器列表
     */
    private final CustomValidator<T> customValidator;
    /**
     * 分组校验
     */
    private final Class<?>[] groups;

    /**
     * 创建默认校验选项
     *
     * @param excelReaderTypeEnum             Excel表格读取器类型枚举类
     * @param customValidator                 自定义校验器回调接口
     * @param customFormatStrHandler          自定义字符串格式化回调接口
     * @param customValidAssignmentStrHandler 自定义校验与赋值回调接口
     * @param groups                          分组校验
     * @param <T>                             JavaBean
     * @return {@link ExcelValidateOptions} Excel表格校验选项类
     */
    public static <T> ExcelValidateOptions<T> createDefault(
            ExcelReaderTypeEnum excelReaderTypeEnum,
            CustomValidAssignmentStrHandler customValidAssignmentStrHandler,
            CustomFormatStrHandler customFormatStrHandler,
            CustomValidator<T> customValidator,
            Class<?>... groups
    ) {
        return new ExcelValidateOptions<>(
                0,
                1,
                true,
                true,
                excelReaderTypeEnum,
                customValidAssignmentStrHandler,
                customFormatStrHandler,
                customValidator,
                groups
        );
    }


    /**
     * 创建完全自定义校验选项
     *
     * @param headerRowIndex                  标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex                   起始行（包含，从0开始计数）
     * @param isReturnExcelDataObj            是否返回Excel表格数据
     * @param isReadabilityRowNumJoin         是否在违反约束信息中拼接，违反约束数据所在的Excel表格行号
     * @param excelReaderTypeEnum             Excel表格读取器类型枚举类
     * @param customValidAssignmentStrHandler 自定义校验与赋值回调接口
     * @param customFormatStrHandler          自定义字符串格式化回调接口
     * @param customValidator                 自定义校验器回调接口
     * @param groups                          分组校验
     * @param <T>                             JavaBean
     * @return {@link ExcelValidateOptions} Excel表格校验选项类
     */
    public static <T> ExcelValidateOptions<T> createFull(
            int headerRowIndex,
            int startRowIndex,
            boolean isReturnExcelDataObj,
            boolean isReadabilityRowNumJoin,
            ExcelReaderTypeEnum excelReaderTypeEnum,
            CustomValidAssignmentStrHandler customValidAssignmentStrHandler,
            CustomFormatStrHandler customFormatStrHandler,
            CustomValidator<T> customValidator,
            Class<?>... groups
    ) {
        return new ExcelValidateOptions<>(
                headerRowIndex,
                startRowIndex,
                isReturnExcelDataObj,
                isReadabilityRowNumJoin,
                excelReaderTypeEnum,
                customValidAssignmentStrHandler,
                customFormatStrHandler,
                customValidator,
                groups
        );
    }

    public ExcelValidateOptions(int headerRowIndex, int startRowIndex, boolean isReturnExcelDataObj, boolean isReadabilityRowNumJoin, ExcelReaderTypeEnum excelReaderTypeEnum, CustomValidAssignmentStrHandler customValidAssignmentStrHandler, CustomFormatStrHandler customFormatStrHandler, CustomValidator<T> customValidator, Class<?>[] groups) {
        this.headerRowIndex = headerRowIndex;
        this.startRowIndex = startRowIndex;
        this.isReturnExcelDataObj = isReturnExcelDataObj;
        this.isReadabilityRowNumJoin = isReadabilityRowNumJoin;
        this.excelReaderTypeEnum = excelReaderTypeEnum;
        this.customValidAssignmentStrHandler = customValidAssignmentStrHandler;
        this.customFormatStrHandler = customFormatStrHandler;
        this.customValidator = customValidator;
        this.groups = groups;
    }
}
