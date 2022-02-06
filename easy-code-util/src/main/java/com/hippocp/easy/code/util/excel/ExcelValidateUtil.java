package com.hippocp.easy.code.util.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.hippocp.easy.code.util.domain.*;
import com.hippocp.easy.code.util.excel.exception.ExcelValidateException;
import com.hippocp.easy.code.util.file.MultipartFileUtil;
import com.hippocp.easy.code.util.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Excel表格工具类，包含校验方法
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
public class ExcelValidateUtil {

    /**
     * 从工厂中获取校验器供该类使用。<br>
     * 生成的ValidatorFactory和Validator实例是线程安全的并且可以缓存。<br>
     * 由于 Hibernate Validator 使用工厂作为缓存约束元数据的上下文，因此建议在应用程序中使用一个工厂实例。<br>
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ExcelValidateUtil.class);

    /**
     * Excel2007以下版本扩展名（不包含2007版本）
     */
    public static final String XLS_SUFFIX = ".xls";

    /**
     * Excel2007以上版本扩展名（包含2007版本）
     */
    public static final String XLSX_SUFFIX = ".xlsx";

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
     * 正确格式，用作提示信息
     */
    public static final RightFormatMessage RIGHT_FORMAT_MESSAGE = new RightFormatMessage(
            INTEGER_RIGHT_FORMAT,
            DOUBLE_RIGHT_FORMAT,
            DATE_RIGHT_FORMAT
    );

    /**
     * 判断Excel文件扩展名是否为 .xlsx
     *
     * @param multipartFile 待校验Excel表格 {@link MultipartFile}
     * @return 布尔值，true-是，false-否
     */
    public static boolean isExcelXLSX(MultipartFile multipartFile) {
        return MultipartFileUtil.isEqualsFileSuffix(multipartFile, XLSX_SUFFIX);
    }

    /**
     * @see ExcelValidateUtil#isExcelXLSX(MultipartFile)
     */
    public static boolean isNotExcelXLSX(MultipartFile multipartFile) {
        return !MultipartFileUtil.isEqualsFileSuffix(multipartFile, XLSX_SUFFIX);
    }

    /**
     * 判断Excel文件扩展名是否为 .xls
     *
     * @param multipartFile 待校验Excel表格 {@link MultipartFile}
     * @return 布尔值，true-是，false-否
     */
    public static boolean isExcelXLS(MultipartFile multipartFile) {
        return MultipartFileUtil.isEqualsFileSuffix(multipartFile, XLS_SUFFIX);
    }

    /**
     * @see ExcelValidateUtil#isExcelXLS(MultipartFile)
     */
    public static boolean isNotExcelXLS(MultipartFile multipartFile) {
        return !MultipartFileUtil.isEqualsFileSuffix(multipartFile, XLS_SUFFIX);
    }

    /**
     * 校验Excel表格标题与参照是否相同，只校验表格标题是否存在，而不在乎其顺序<br>
     * 默认取Excel表格的第一行数据作为表格标题
     *
     * @param xlsFile     {@link MultipartFile}
     * @param excelTitles String数组
     * @return 布尔值，true-是，false-否
     */
    public static boolean excelTitleEquals(MultipartFile xlsFile, String[] excelTitles) {
        return excelTitleEquals(xlsFile, excelTitles, 0);
    }

    /**
     * 校验Excel表格标题与参照是否相同，只校验表格标题是否存在，而不在乎其顺序<br>
     *
     * @param xlsFile     {@link MultipartFile}
     * @param excelTitles String数组
     * @param titleRowNum 标识表格标题行号，从0开始，0为Excel表格第一行
     * @return 布尔值，true-是，false-否
     */
    public static boolean excelTitleEquals(MultipartFile xlsFile, String[] excelTitles, int titleRowNum) {
        try (
                // 获取文件输入流 自动关闭输入流
                InputStream fis = xlsFile.getInputStream();
                // 获取Excel读取器 自动关闭hutool工具Excel读取器
                ExcelReader reader = ExcelUtil.getReader(fis)
        ) {

            // Object类型列标题
            List<Object> objectColumnTitleList = reader.readRow(titleRowNum);
            // Excel表格列标题校验，true-通过校验 false未通过校验
            boolean isPassValidate = columnTitleValidate(objectColumnTitleList, excelTitles);
            return isPassValidate;
        } catch (IOException e) {
            log.error("无法读取表格数据：", e);
            // 校验不通过
            return false;
        }

    }

    /**
     * Excel表格列标题校验
     *
     * @param objectColumnTitleList Object类型列标题 List 集合
     * @param excelTitles           参照表头数组
     * @return 布尔值，true-是，false-否
     */
    public static boolean columnTitleValidate(List<Object> objectColumnTitleList, String[] excelTitles) {
        // 列标题List为空，代表表格列标题没填写，校验不通过
        if (CollUtil.isEmpty(objectColumnTitleList)) {
            return false;
        }

        // 获取参照表头 Set 集合，原材料为 excelTitles
        Set<String> referenceSet = getReferenceSet(excelTitles);
        // 获取初始参照Set大小
        int referenceSetSize = referenceSet.size();
        Set<String> stringSet = new HashSet<>();
        // 遍历objectColumnTitleList，取出元素，存入stringSet
        objectColumnTitleList.forEach(objectTitle -> {
            if (objectTitle instanceof String) {
                stringSet.add((String) objectTitle);
            }
        });

        // 实际Set大小
        int stringSetSize = stringSet.size();
        // 参照Set与实际Set大小不相同，代表表格列标题数量不对，校验不通过
        if (referenceSetSize != stringSetSize) {
            return false;
        }

        // 合并去重
        referenceSet.addAll(stringSet);
        // 合并前参照Set与合并后参照Set大小不相同，代表表格列标题内容不一致，校验不通过
        if (referenceSetSize != referenceSet.size()) {
            return false;
        }

        // 校验通过
        return true;
    }

    /**
     * 获取参照表头 Set 集合，原材料为 excelTitles
     *
     * @param referenceStringArray 参照字符串数组
     * @return {@link Set} 参照表头，用于与Excel文件中的表头进行对比
     */
    public static Set<String> getReferenceSet(String[] referenceStringArray) {
        // 如果参照字符串数组，也返回对象，防止NPE
        if (referenceStringArray == null) {
            return new HashSet<>();
        }
        // 正常返回
        return new HashSet<>(Arrays.asList(referenceStringArray));
    }

    /**
     * 单元格校验
     *
     * @param xlsxFile         Excel表格文件
     * @param columnToFieldMap 表格列映射map
     * @param beanType         需要校验的JavaBean，其应描述Excel表格中的数据
     * @param options          Excel表格校验选项
     * @param <T>              JavaBean
     * @return {@link ValidatorResult} 校验器校验结果
     */
    public static <T> ValidatorResult<T> cellValid(
            MultipartFile xlsxFile,
            Map<String, String> columnToFieldMap,
            Class<T> beanType,
            ExcelValidateOptions<T> options) {
        // 读取options数据
        // 表格标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
        int headerRowIndex = options.getHeaderRowIndex();
        // 表格起始行（包含，从0开始计数）
        int startRowIndex = options.getStartRowIndex();
        // 是否返回Excel表格数据
        boolean isReturnExcelDataObj = options.isReturnExcelDataObj();
        // 是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
        boolean isReadabilityRowNumJoin = options.isReadabilityRowNumJoin();
        // 自定义校验与赋值处理器
        CustomValidAssignmentStrHandler customValidAssignmentStrHandler = options.getCustomValidAssignmentStrHandler();
        // 自定义字符串格式化处理器
        CustomFormatStrHandler customFormatStrHandler = options.getCustomFormatStrHandler();
        // 自定义校验器列表
        CustomValidator<T> customValidator = options.getCustomValidator();
        // 分组校验
        Class<?>[] groups = options.getGroups();

        // Excel表格读取器类型
        ExcelReaderTypeEnum excelReaderTypeEnum = options.getExcelReaderTypeEnum();

        boolean isNoReadValid = ExcelReaderTypeEnum.NOTVALID.getCode().equals(excelReaderTypeEnum.getCode());
        // 不需要读取数据时校验
        if (isNoReadValid) {
            // 执行
            return cellValid(
                    xlsxFile,
                    headerRowIndex,
                    startRowIndex,
                    columnToFieldMap,
                    isReturnExcelDataObj,
                    isReadabilityRowNumJoin,
                    beanType,
                    customValidator,
                    groups);
        }

        // 默认执行，读取数据时校验
        return cellValid(
                xlsxFile,
                headerRowIndex,
                startRowIndex,
                columnToFieldMap,
                isReturnExcelDataObj,
                isReadabilityRowNumJoin,
                beanType,
                customValidAssignmentStrHandler,
                customFormatStrHandler,
                customValidator,
                groups);
    }


    /**
     * Excel表格列业务相关校验器，不会在读取数据时就开始校验，缺少的校验如下：<br>
     * 字符串 -》数字（整数）
     * 字符串 -》数字（浮点数）
     * 字符串 -》日期
     *
     * @param xlsxFile                Excel表格文件
     * @param headerRowIndex          标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex           起始行（包含，从0开始计数）
     * @param columnToFieldMap        表格列映射map
     * @param isReturnExcelDataObj    是否返回Excel表格数据
     * @param isReadabilityRowNumJoin 是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
     * @param beanType                需要校验的JavaBean，其应描述Excel表格中的数据
     * @param customValidator         自定义校验器列表
     * @param groups                  分组校验
     * @param <T>                     JavaBean
     * @return {@link ValidatorResult} 校验器校验结果类
     */
    public static <T> ValidatorResult<T> cellValid(MultipartFile xlsxFile,
                                                   int headerRowIndex,
                                                   int startRowIndex,
                                                   Map<String, String> columnToFieldMap,
                                                   boolean isReturnExcelDataObj,
                                                   boolean isReadabilityRowNumJoin,
                                                   Class<T> beanType,
                                                   CustomValidator<T> customValidator,
                                                   Class<?>... groups) {
        // 存放表格数据
        List<T> excelDataList;
        try {
            // 读表格数据
            excelDataList = readExcelData(xlsxFile, headerRowIndex, startRowIndex, columnToFieldMap, beanType);
        } catch (IOException e) {
            log.error("无法读取Excel表格数据：", e);
            // 则校验不通过
            return new ValidatorResult<>(false);
        }
        // Excel表格列业务相关校验器
        return cellValidaBase(
                headerRowIndex,
                startRowIndex,
                isReturnExcelDataObj,
                isReadabilityRowNumJoin,
                customValidator,
                excelDataList,
                groups
        );
    }


    /**
     * Excel表格列业务相关校验器，读取数据时就开始校验，将进行的校验如下：<br>
     * 字符串 -》数字（整数）<br>
     * 字符串 -》数字（浮点数）<br>
     * 字符串 -》日期<br>
     *
     * @param xlsxFile                        Excel表格文件
     * @param headerRowIndex                  标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex                   起始行（包含，从0开始计数）
     * @param columnToFieldMap                表格列映射map
     * @param isReturnExcelDataObj            是否返回Excel表格数据
     * @param isReadabilityRowNumJoin         是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
     * @param beanType                        需要校验的JavaBean，其应描述Excel表格中的数据
     * @param customValidAssignmentStrHandler 自定义校验与赋值回调接口
     * @param customFormatStrHandler          自定义字符串格式化回调接口
     * @param customValidator                 自定义校验器列表
     * @param groups                          分组校验
     * @param <T>                             JavaBean
     * @return {@link ValidatorResult} 校验器校验结果类
     */
    public static <T> ValidatorResult<T> cellValid(MultipartFile xlsxFile,
                                                   int headerRowIndex,
                                                   int startRowIndex,
                                                   Map<String, String> columnToFieldMap,
                                                   boolean isReturnExcelDataObj,
                                                   boolean isReadabilityRowNumJoin,
                                                   Class<T> beanType,
                                                   CustomValidAssignmentStrHandler customValidAssignmentStrHandler,
                                                   CustomFormatStrHandler customFormatStrHandler,
                                                   CustomValidator<T> customValidator,
                                                   Class<?>... groups) {
        // 存放表格数据
        List<T> excelDataList;
        Set<ConstraintViolation<?>> violationObjSet = new HashSet<>();
        try {
            // 读表格数据
            excelDataList = readExcelDataAndValidate(
                    xlsxFile,
                    headerRowIndex,
                    startRowIndex,
                    columnToFieldMap,
                    beanType,
                    violationObjSet,
                    customValidAssignmentStrHandler,
                    customFormatStrHandler
            );
        } catch (IOException e) {
            log.error("无法读取Excel表格数据：", e);
            // 则校验不通过
            return new ValidatorResult<>(false);
        }

        // 校验不通过返回
        if (CollUtil.isNotEmpty(violationObjSet)) {
            ValidatorResult<T> result = new ValidatorResult<>(false, excelDataList, null, violationObjSet);
            result.generateConstraintError();
            // 日志
            if (log.isInfoEnabled()) {
                log.info("表格读取过程中的校验是否通过：不通过");
            }
            return result;
        }

        // Excel表格列业务相关校验器
        return cellValidaBase(
                headerRowIndex,
                startRowIndex,
                isReturnExcelDataObj,
                isReadabilityRowNumJoin,
                customValidator,
                excelDataList,
                groups
        );
    }


    /**
     * 基础Excel表格列业务相关校验器
     *
     * @param headerRowIndex          标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex           起始行（包含，从0开始计数）
     * @param isReturnExcelDataObj    是否返回Excel表格数据
     * @param isReadabilityRowNumJoin 是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
     * @param customValidator         自定义校验器列表
     * @param excelDataList           表格数据列表
     * @param groups                  分组校验
     * @param <T>                     JavaBean
     * @return {@link ValidatorResult} 校验器校验结果类
     */
    public static <T> ValidatorResult<T> cellValidaBase(int headerRowIndex,
                                                        int startRowIndex,
                                                        boolean isReturnExcelDataObj,
                                                        boolean isReadabilityRowNumJoin,
                                                        CustomValidator<T> customValidator,
                                                        List<T> excelDataList,
                                                        Class<?>... groups) {

        // JavaBean约束违反信息
        Set<ConstraintViolation<T>> violationSet = new HashSet<>();
        // 带可读性行号的单纯的束违反信息字符串列表
        List<String> readabilityRowNumMsgList = new ArrayList<>();

        // 执行JavaBean验证器
        executeJavaBeanValidator(excelDataList, headerRowIndex, startRowIndex, isReadabilityRowNumJoin,
                violationSet, readabilityRowNumMsgList, groups);

        // 校验不通过返回
        if (CollUtil.isNotEmpty(violationSet)) {
            ValidatorResult<T> result = new ValidatorResult<>(false, excelDataList, violationSet, null);
            result.generateConstraintError();
            // 日志
            if (log.isInfoEnabled()) {
                log.info("JavaBean验证器校验是否通过：不通过");
            }
            return result;
        }

        // 存在自定义校验器
        Set<EasyConstraintViolationImpl<T>> customViolationSet;
        if (customValidator != null) {
            // 执行自定义校验器，获取用户自定义约束违反信息Set
            customViolationSet = executeCustomValidator(excelDataList, customValidator);
            // 不为空
            if (CollUtil.isNotEmpty(customViolationSet)) {
                // 将自定义校验器返回的约束违反信息Set 合并到 JavaBean约束违反信息Set
                violationSet.addAll(customViolationSet);
            }
        }


        // 判断校验是否通过
        boolean isValid = isValid(violationSet);

        // 日志
        if (log.isInfoEnabled()) {
            log.info("自定义校验器校验是否通过：{}", isValid ? "通过" : "不通过");
        }

        // 构建校验器结果对象
        return buildValidatorResult(isValid, isReturnExcelDataObj, excelDataList, violationSet);
    }


    /**
     * 构建校验器结果对象，参数是否有值取决于调用者的参数
     *
     * @param isValid              校验是否通过
     * @param isReturnExcelDataObj 是否带回Excel表格对象列表
     * @param excelDataList        Excel表格数据
     * @param violationSet         约束违反信息Set
     * @param <T>                  JavaBean
     * @return {@link ValidatorResult} 构建校验器结果对象
     */
    protected static <T> ValidatorResult<T> buildValidatorResult(boolean isValid,
                                                                 boolean isReturnExcelDataObj,
                                                                 List<T> excelDataList,
                                                                 Set<ConstraintViolation<T>> violationSet) {
        // 校验完成，构建结果数据，参数是否有值取决于调用者的参数
        ValidatorResult<T> resultData;
        if (isReturnExcelDataObj) {
            // 要带回Excel数据对象
            resultData = new ValidatorResult<>(isValid, excelDataList, violationSet);
        } else {
            // 不要带回Excel数据对象
            resultData = new ValidatorResult<>(isValid, null, violationSet);
        }
        // 生成 constraintError属性
        resultData.generateConstraintError();
        return resultData;
    }


    /**
     * 读取Excel表格数据
     *
     * @param xlsxFile         Excel表格文件
     * @param headerRowIndex   标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex    起始行（包含，从0开始计数）
     * @param columnToFieldMap 表格列映射map
     * @param beanType         需要校验的JavaBean，其应描述Excel表格中的数据
     * @param <T>              JavaBean
     * @return {@link List} 表格数据
     * @throws IOException 无法读取Excel表格数据，getInputStream时发生异常
     */
    public static <T> List<T> readExcelData(
            MultipartFile xlsxFile,
            int headerRowIndex,
            int startRowIndex,
            Map<String, String> columnToFieldMap,
            Class<T> beanType
    ) throws IOException {
        // 存放表格数据
        List<T> excelDataList;
        // 读表格数据
        try (
                // 获取文件输入流 自动关闭输入流
                InputStream fis = xlsxFile.getInputStream();
                // 获取Excel读取器 自动关闭hutool工具Excel读取器
                ExcelReader reader = ExcelUtil.getReader(fis)
        ) {

            // 根据map映射关系为JavaBean赋值
            // 遍历map将key作为header，将value作为alias，调用reader#addHeaderAlias方法
            columnToFieldMap.forEach(reader::addHeaderAlias);
            // 读取Excel为Map的列表，读取所有行，默认第一行做为标题，数据从第二行开始 Map表示一行，标题为key，单元格内容为value
            excelDataList = reader.read(headerRowIndex, startRowIndex, Integer.MAX_VALUE, beanType);
            // 日志输出
            if (log.isInfoEnabled()) {
                String lineBreak = StringUtil.presentOsLineBreak();
                log.info("Excel表格校验工具读取到的表格数据如下：{}{}", lineBreak, excelDataList);
            }
        }
        return excelDataList;
    }

    /**
     * @param xlsxFile               Excel表格文件
     * @param headerRowIndex         标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex          起始行（包含，从0开始计数）
     * @param columnToFieldMap       表格列映射map
     * @param beanType               需要校验的JavaBean，其应描述Excel表格中的数据
     * @param formatViolationSet     针对 字符串 -》数字 字符串 -》日期校验 约束违反提示信息是否附加行号根据方法参数决定
     * @param customFormatStrHandler 自定义字符串格式化回调接口
     * @param <T>                    JavaBean
     * @return {@link List} 表格数据
     * @throws IOException 无法读取Excel表格数据，getInputStream时发生异常
     */
    public static <T> List<T> readExcelDataAndValidate(
            MultipartFile xlsxFile,
            int headerRowIndex,
            int startRowIndex,
            Map<String, String> columnToFieldMap,
            Class<T> beanType,
            Set<ConstraintViolation<?>> formatViolationSet,
            CustomValidAssignmentStrHandler customValidAssignmentStrHandler,
            CustomFormatStrHandler customFormatStrHandler
    ) throws IOException {

        // 读表格数据
        try (
                // 获取文件输入流 自动关闭输入流
                InputStream fis = xlsxFile.getInputStream();
                // 获取Excel读取器 自动关闭hutool工具Excel读取器
                ExcelReader reader = ExcelUtil.getReader(fis)
        ) {

            // 读取结束行（包含，从0开始计数）
            int endRowIndex = Integer.MAX_VALUE;
            // 根据map映射关系为JavaBean赋值
            // 遍历map将key作为header，将value作为alias，调用reader#addHeaderAlias方法
            columnToFieldMap.forEach(reader::addHeaderAlias);
            // 获取表格数据列表
            List<Map<String, Object>> excelDataList = reader.read(headerRowIndex, startRowIndex, endRowIndex);

            // beanType为Map
            if (Map.class.isAssignableFrom(beanType)) {

                // 强转后返回数据
                return (List<T>) excelDataList;

            }

            // JavaBean属性名称-key，表格列标题-value
            Map<String, String> fieldToColumnMap = MapUtil.reverse(columnToFieldMap);
            // 行号索引
            int rowIndex = startRowIndex;
            // 可读性行号并不一定是1，从哪里开始由用户指定参数加1
            int readabilityRowNum = startRowIndex + 1;
            // 使用fori遍历表格数据行列表，防止出现并发修改异常
            for (int i = 0; i < excelDataList.size(); i++) {

                // 如果该行是标题行则跳过
                if (rowIndex == headerRowIndex) {
                    continue;
                }

                // 当前行对象即Map，包含标题（此处标题已经为JavaBean属性名称）与单元格数据
                Map<String, Object> rowData = excelDataList.get(i);
                // 获取列标题对应的JavaBean属性
                Set<Map.Entry<String, Object>> titleAndDataSet = rowData.entrySet();
                // 暂存Map
                Map<String, Object> tempStorageMap = new HashMap<>(16);

                // 通过迭代器遍历表格列标题和数据Map，防止出现并发修改异常
                Iterator<Map.Entry<String, Object>> iterator = titleAndDataSet.iterator();
                while (iterator.hasNext()) {
                    // 迭代器中下一个元素
                    Map.Entry<String, Object> titleAndData = iterator.next();
                    // JavaBean 属性名称
                    String fieldName = titleAndData.getKey();
                    // 表格标题
                    String title = fieldToColumnMap.get(fieldName);
                    // 属性
                    Field field;
                    try {
                        // 根据属性名称获取
                        field = beanType.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                        // 捕捉异常，而非抛出
                        String msg = StrUtil.format("在类 [ {} ] 中没有找到名称为 [ {} ] 的字段",
                                beanType.getName(), fieldName);
                        log.error(msg, e);
                        // 发生异常，未能获取到属性，结束当次循环，进入下次循环
                        continue;
                    }

                    // 获取属性类型
                    Class<?> fieldType = field.getType();
                    // 表格列数据
                    Object dataObj = titleAndData.getValue();
                    // 表格数据转为字符串
                    String dataStr = String.valueOf(dataObj);

                    // 存在自定义校验与赋值处理器
                    if (customValidAssignmentStrHandler != null) {
                        // 执行自定义校验与赋值处理器
                        executeCustomValidAssignmentStrHandler(
                                fieldType,
                                fieldName,
                                dataStr,
                                title,
                                readabilityRowNum,
                                tempStorageMap,
                                formatViolationSet,
                                customValidAssignmentStrHandler
                        );
                        //if结束
                    }

                    boolean isNotNull = customFormatStrHandler != null;
                    boolean isEmpty = CollUtil.isEmpty(formatViolationSet);
                    // 自定义字符串格式化处理器不是空的 并且 约束违反信息Set是空的
                    if (isNotNull && isEmpty) {
                        // 执行自定义字符串格式化处理器
                        Map<String, String> waitMergeMap = executeCustomFormatStrHandler(fieldType, dataStr,
                                fieldName, customFormatStrHandler);
                        // 不为空
                        if (waitMergeMap != null) {
                            // 合并到暂存Map
                            tempStorageMap.putAll(waitMergeMap);
                        }
                    }

                    // 内层循环结束
                }

                // 一行数据遍历结束，合并暂存Map
                rowData.putAll(tempStorageMap);
                // 行号索引计数加一
                rowIndex++;
                // 可读性行号计数加一
                readabilityRowNum++;

                // 外层循环结束
            }

            // formatViolations列表不为空，代表校验不通过
            if (CollUtil.isNotEmpty(formatViolationSet)) {

                // 快速失败，不浪费内存
                return null;

            }

            // 校验通过，Map转Bean
            final List<T> excelDataBeanList = new ArrayList<>(excelDataList.size());
            final CopyOptions copyOptions = CopyOptions.create().setIgnoreError(true);
            for (Map<String, Object> map : excelDataList) {
                excelDataBeanList.add(BeanUtil.toBean(map, beanType, copyOptions));
            }
            // 返回表格数据JavaBean列表
            return excelDataBeanList;
            // try 代码块结束
        }
    }


    /**
     * 执行自定义校验与赋值回调接口<br>
     *
     * @param fieldType                       属性类型
     * @param fieldName                       JavaBean属性名称，即Map中key
     * @param dataStr                         表格数据字符串，即Map中value
     * @param title                           表格标题
     * @param readabilityRowNum               可读性行号
     * @param tempStorageMap                  临时存储Map，存放自定义的新值，避免直接修改原Map发送并发修改异常
     * @param formatViolationSet              针对 字符串 -》数字 字符串 -》日期校验 约束违反提示信息是否附加行号根据方法参数决定
     * @param customValidAssignmentStrHandler 自定义校验与赋值回调接口
     */
    protected static void executeCustomValidAssignmentStrHandler(
            Class<?> fieldType,
            String fieldName,
            String dataStr,
            String title,
            int readabilityRowNum,
            Map<String, Object> tempStorageMap,
            Set<ConstraintViolation<?>> formatViolationSet,
            CustomValidAssignmentStrHandler customValidAssignmentStrHandler
    ) {
        // 定义回调接口，实现调用者自定义表格数据转换为Bean列表过程中的单元格数据编辑赋值、校验

        // 不存在自定义校验与赋值处理器
        if (customValidAssignmentStrHandler == null) {
            // 直接返回结束方法
            return;
        }

        // 正常执行
        CustomValidatorResult<?> result =
                customValidAssignmentStrHandler.validAssignmentStr(fieldType, fieldName,
                        dataStr, title, readabilityRowNum, tempStorageMap);
        boolean isValid = result.isValid();
        // 校验通过
        if (isValid) {
            // 结束方法
            return;
        }

        // 不通过自定义校验与赋值处理
        Set<? extends EasyConstraintViolationImpl<?>> violationSet = result.getConstraintViolationSet();
        // 自定义校验不通过，并且约束违反信息为空
        if (CollUtil.isEmpty(violationSet)) {
            // 抛出异常
            throw new ExcelValidateException("自定义校验与赋值处理器校验不通过时，约束违反提示信息Set不能为空");
        }

        // 不为空合并约束违反信息
        formatViolationSet.addAll(violationSet);
    }


    /**
     * 执行自定义字符串格式化回调接口<br>
     *
     * @param fieldType              属性类型
     * @param dataStr                表格数据字符串，即Map中value
     * @param fieldName              JavaBean属性名称，即Map中key
     * @param customFormatStrHandler 自定义字符串格式化回调接口
     */
    protected static Map<String, String> executeCustomFormatStrHandler(Class<?> fieldType,
                                                                       String dataStr,
                                                                       String fieldName,
                                                                       CustomFormatStrHandler customFormatStrHandler) {

        // 不存在自定义字符串格式化处理器，直接返回
        if (customFormatStrHandler == null) {
            return null;
        }

        // 目标属性不是字符串类型
        boolean isNotStringType = StringUtil.isNotStringType(fieldType);
        if (isNotStringType) {
            return null;
        }

        // 自定义字符串格式化处理器不为空 并且 目标属性是字符串类型 执行自定义格式化
        // 针对String类型的JavaBean属性，执行自定义的字符串格式化规则，例如去除制表符、windows与unix换行符
        String formatStr = customFormatStrHandler.format(dataStr);
        HashMap<String, String> waitMergeMap = new HashMap<>(2);
        // 格式化完成，返回一个等待合并的Map
        waitMergeMap.put(fieldName, formatStr);
        return waitMergeMap;
    }


    /**
     * 执行JavaBean验证器，具体实现来自校验框架<br>
     * 例如：hibernate validator<br>
     *
     * @param excelDataList            表格数据列表
     * @param headerRowIndex           标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
     * @param startRowIndex            起始行（包含，从0开始计数）
     * @param isReadabilityRowNumJoin  是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
     * @param violationSet             JavaBean约束违反信息
     * @param readabilityRowNumMsgList 带可读性行号的单纯的束违反信息字符串列表
     * @param groups                   分组校验
     * @param <T>                      JavaBean
     */
    protected static <T> void executeJavaBeanValidator(List<T> excelDataList,
                                                       int headerRowIndex,
                                                       int startRowIndex,
                                                       boolean isReadabilityRowNumJoin,
                                                       Set<ConstraintViolation<T>> violationSet,
                                                       List<String> readabilityRowNumMsgList,
                                                       Class<?>... groups) {

        // 校验实例
        Validator validator = validatorFactory.getValidator();
        // 可读性行号并不一定是1，从哪里开始由用户指定参数加1
        int readabilityRowNum = startRowIndex + 1;
        // 遍历beanList，校验JavaBean，例如进行非空校验、数字最小值校验，可以进行分组校验
        for (T bean : excelDataList) {
            // 单个JavaBean对象的校验，例如Excel表格对应一行数据
            Set<ConstraintViolation<T>> singleConstraintViolations;

            // 是否开启分组校验
            if (groups == null) {
                // groups可变参为空执行，普通校验
                singleConstraintViolations = validator.validate(bean);
            } else {
                // groups可变参不为空执行，分组校验
                singleConstraintViolations = validator.validate(bean, groups);
            }

            // 单个JavaBean对象约束违反信息为null
            if (CollectionUtil.isEmpty(singleConstraintViolations)) {
                // 结束当次循环，进入下次循环
                continue;
            }

            // 开启约束违反信息行号拼接，则执行，否则跳过
            if (isReadabilityRowNumJoin) {
                // 当单个JavaBean对象约束违反信息不为null时，遍历Set获取约束违反信息
                // 内循环，遍历
                for (ConstraintViolation<T> violation : singleConstraintViolations) {
                    // @Pattern(regexp = "3|6|12", message = "{年龄群体列格式有误，错误内容: ${validatedValue}，正确格式：只能填写3、6、12，其中之一}")
                    // 注解中的message
                    String msg = violation.getMessage();
                    // 拼接完成的约束违反信息
                    String joinCompleteMsg = StrUtil.format("第{}行{}", readabilityRowNum, msg);
                    readabilityRowNumMsgList.add(joinCompleteMsg);
                    // 内循环结束
                }
            }

            // 将约束违反信息合并到 violationSet
            violationSet.addAll(singleConstraintViolations);
            // 可读性表格标题行号
            int readabilityHeaderRowIndex = headerRowIndex + 1;
            // 如果当前行是Excel表格标题
            if (readabilityHeaderRowIndex == readabilityRowNum) {
                // 结束当次循环，进入下次循环，不加可读性行号
                continue;
            }

            // 加可读性行号
            readabilityRowNum++;
            // 外循环结束
        }
    }


    /**
     * 执行自定义校验器回调接口<br>
     *
     * @param excelDataList   表格数据列表
     * @param customValidator 自定义校验器列表
     * @param <T>             JavaBean
     * @return {@link List}用户自定义约束违反信息列表
     */
    protected static <T> Set<EasyConstraintViolationImpl<T>> executeCustomValidator(List<T> excelDataList,
                                                                                    CustomValidator<T> customValidator) {
        // 用户自定义的校验规则为空，则返回null
        if (customValidator == null) {
            return null;
        }

        // 执行自定义校验器
        CustomValidatorResult<T> customValidatorResult = customValidator.isValid(excelDataList);
        // 校验是否通过
        boolean isValid = customValidatorResult.isValid();

        // 校验通过
        if (isValid) {

            // 不需要取出约束违反信息，直接返回null
            return null;

        }

        // 校验不通过
        // 取出单个校验器中信息
        Set<EasyConstraintViolationImpl<T>> set = customValidatorResult.getConstraintViolationSet();
        if (CollUtil.isEmpty(set)) {
            throw new ExcelValidateException("自定义校验器校验不通过时，约束违反提示信息Set不能为空");
        }
        // 返回自定义校验器的约束违反信息
        return set;
    }


    /**
     * 判断校验是否通过
     *
     * @param violationSet JavaBean约束违反信息
     * @param <T>          JavaBean
     * @return 布尔值，true-通过 false-不通过
     */
    protected static <T> boolean isValid(Set<ConstraintViolation<T>> violationSet) {
        boolean isValid = true;
        // 判断校验是否通过
        // 校验框架JavaBean一般性校验，约束违反信息不为空，代表校验不通过
        boolean isNotEmptyViolationSet = CollUtil.isNotEmpty(violationSet);
        // 其一不为空，则代表校验不通过
        if (isNotEmptyViolationSet) {
            // 标识出校验不通过
            isValid = false;

            // 日志
            if (log.isInfoEnabled()) {
                log.info("Excel表格校验，约束违反信息：{}", violationSet);
            }

            // 外层if结束
        }
        return isValid;
    }


    /**
     * 构建约束违反信息工具方法
     *
     * @param isReadabilityRowNumJoin 是否在JavaBean违反约束错误信息中拼接，违反约束数据所在的Excel表格行号
     * @param readabilityRowNum       可读性行号，而不是从0开始的索引
     * @param title                   表格列标题，用作提示信息
     * @param errorValue              错误内容 即参数值
     * @param rightFormat             正确格式
     * @return {@link String} 拼接完成的约束违反信息
     */
    public static String constraintViolationMsgBuild(boolean isReadabilityRowNumJoin, int readabilityRowNum,
                                                     String title, String errorValue, String rightFormat) {
        // 约束违反信息
        String constraintViolationMsg;
        // 是否开启可读性行号拼接
        if (isReadabilityRowNumJoin) {
            // 开启
            constraintViolationMsg = errorHintMessageTemplate(readabilityRowNum, title, errorValue, rightFormat);
        } else {
            // 关闭
            constraintViolationMsg = errorHintMessageTemplate(title, errorValue, rightFormat);
        }
        return constraintViolationMsg;
    }


    /**
     * 表格数据校验，为Null提示消息模板 模板：第{}行{}未填写
     *
     * @param readabilityRowNum 表格行号
     * @param title             表格列头标题
     * @return {@link String}提示消息
     */
    public static String isNullMessageTemplate(int readabilityRowNum, String title) {
        return StrUtil.format("第{}行{}未填写", readabilityRowNum, title);
    }

    /**
     * 表格数据校验，不带可读性行号非法参数提示消息模板 模板：{}格式有误，错误内容: {}，正确格式：{}
     *
     * @param title       表格列头标题
     * @param errorValue  错误内容 即参数值
     * @param rightFormat 正确格式
     * @return {@link String}提示消息
     */
    public static String errorHintMessageTemplate(String title, String errorValue, String rightFormat) {
        return StrUtil.format("{}格式有误，错误内容: {}，正确格式：{}", title, errorValue, rightFormat);
    }

    /**
     * 表格数据校验，非法参数提示消息模板 模板：第{}行{}格式有误，错误内容: {}，正确格式：{}
     *
     * @param readabilityRowNum 表格行号
     * @param title             表格列头标题
     * @param errorValue        错误内容 即参数值
     * @param rightFormat       正确格式
     * @return {@link String}提示消息
     */
    public static String errorHintMessageTemplate(int readabilityRowNum, String title, String errorValue, String rightFormat) {
        return StrUtil.format("第{}行{}", readabilityRowNum, errorHintMessageTemplate(title, errorValue, rightFormat));
    }

    private ExcelValidateUtil() {
    }
}
