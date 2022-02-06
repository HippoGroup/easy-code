package com.hippocp.util.excel;

import cn.hutool.core.collection.CollUtil;
import com.hippocp.easy.code.util.domain.CustomValidatorResult;
import com.hippocp.easy.code.util.domain.EasyConstraintViolationImpl;
import com.hippocp.easy.code.util.domain.ExcelReaderTypeEnum;
import com.hippocp.easy.code.util.domain.ValidatorResult;
import com.hippocp.easy.code.util.excel.ExcelValidateOptions;
import com.hippocp.easy.code.util.excel.ExcelValidateUtil;
import com.hippocp.easy.code.util.number.NumUtil;
import com.hippocp.easy.code.util.string.StringUtil;
import com.hippocp.util.entity.ExcelDTO;
import com.hippocp.util.entity.NumberTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author ZhouYifan
 * @date 2022/1/6
 */
public class ExcelValidateUtilTest {

    public static MultipartFile initMultipartFile() {
        URL url = ExcelValidateUtilTest.class.getClassLoader().getResource("testTemplate.xlsx");
        File file = new File(url.getFile());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    "application/octet-stream", fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    @Test
    public void excelValidateUtilTest() {

        MultipartFile multipartFile = initMultipartFile();

        boolean isNotExcelXLS = ExcelValidateUtil.isNotExcelXLS(multipartFile);
        Assert.assertTrue("不应是.xls文件，预期为 true，现在却提示false", isNotExcelXLS);

        boolean isExcelXLS = ExcelValidateUtil.isExcelXLS(multipartFile);
        Assert.assertFalse("不应是.xls文件，预期为 false，现在却提示ture", isExcelXLS);

        boolean isNotExcelXLSX = ExcelValidateUtil.isNotExcelXLSX(multipartFile);
        Assert.assertFalse("是.xlsx文件，预期为 false，现在却提示ture", isNotExcelXLSX);

        boolean isExcelXLSX = ExcelValidateUtil.isExcelXLSX(multipartFile);
        Assert.assertTrue("是.xlsx文件，预期为 ture，现在却提示false", isExcelXLSX);

    }


    @Test
    public void excelTitleEqualsTest() {
        MultipartFile multipartFile = initMultipartFile();
        boolean excelTitleNotEquals = ExcelValidateUtil.excelTitleEquals(multipartFile, new String[]{"测试标题01"});
        Assert.assertFalse("预期表格标题不相同，现在却提示相同", excelTitleNotEquals);

        boolean excelTitleEquals = ExcelValidateUtil.excelTitleEquals(multipartFile, new String[]{
                "数据编号", "姓名", "年龄"});
        Assert.assertTrue("预期表格标题相同，现在却提示不相同", excelTitleEquals);

    }

    @Test
    public void cellPassValidateTest() {
        MultipartFile multipartFile = initMultipartFile();
        Map<String, String> columnMap = new HashMap<>(4);
        columnMap.put("数据编号", "dataCode");
        columnMap.put("姓名", "name");
        columnMap.put("年龄", "age");

        // 通过反射给 validatorFactory 属性赋值，来模拟 Spring 依赖注入，防止NPE
        try {
            Field field = ExcelValidateUtil.class.getDeclaredField("validatorFactory");
            field.setAccessible(true);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            try {
                field.set(factory, factory);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        ExcelValidateOptions<ExcelDTO> options = ExcelValidateOptions.createDefault(
                ExcelReaderTypeEnum.VALID,
                (fieldType, fieldName, columnValue, columnName, readabilityRowNum, tempMap) -> {
                    System.out.println("--------------第" + readabilityRowNum + "行自定义字符串校验与赋值处理器，做些校验------------------");
                    Set<EasyConstraintViolationImpl<Object>> set = new HashSet<>();
                    EasyConstraintViolationImpl<Object> v1 = StringUtil.stringToInteger(fieldType, fieldName, columnValue,
                            columnName, true, readabilityRowNum);
                    EasyConstraintViolationImpl<Object> v2 = StringUtil.stringToDouble(fieldType, fieldName, columnValue,
                            columnName, true, readabilityRowNum);
                    EasyConstraintViolationImpl<Object> v3 = StringUtil.stringToDate(fieldType, fieldName, columnValue,
                            columnName, true, readabilityRowNum, tempMap);
                    if (v1 != null) {
                        set.add(v1);
                    }
                    if (v2 != null) {
                        set.add(v2);
                    }
                    if (v3 != null) {
                        set.add(v3);
                    }
                    if (CollUtil.isEmpty(set)) {
                        return CustomValidatorResult.passValid();
                    }

                    return CustomValidatorResult.notPassValid(set);
                },
                (str) -> {
                    System.out.println("---------------自定义字符串格式化处理器-----------------------");
                    return StringUtil.trimBlankCharacter(str);
                },
                (dataList) -> {
                    System.out.println("--------------自定义校验器，做些校验------------------");
                    ExcelDTO dto = dataList.get(0);
                    return CustomValidatorResult.passValid();
                }
        );

        ValidatorResult<ExcelDTO> validatorResult =
                ExcelValidateUtil.cellValid(multipartFile, columnMap, ExcelDTO.class, options);


        boolean isValid = validatorResult.isValid();
        Assert.assertTrue("预期能够通过校验，现在却提示不通过", isValid);

        List<ExcelDTO> data = validatorResult.getData();
        Assert.assertNotNull("预期有数据", data);
        System.out.println("---------控制台打印从Excel表格中获取到的数据----------");
        System.out.println(validatorResult);
        System.out.println(data);

    }

    @Test
    public void cellNotPassValidateTest() {
        MultipartFile multipartFile = initMultipartFile();
        Map<String, String> columnMap = new HashMap<>(4);
        columnMap.put("数据编号", "dataCode");
        columnMap.put("姓名", "name");
        columnMap.put("年龄", "age");

        // 通过反射给 validatorFactory 属性赋值，来模拟 Spring 依赖注入，防止NPE
        try {
            Field field = ExcelValidateUtil.class.getDeclaredField("validatorFactory");
            field.setAccessible(true);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            try {
                field.set(factory, factory);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        ExcelValidateOptions<ExcelDTO> options = ExcelValidateOptions.createDefault(
                ExcelReaderTypeEnum.VALID,
                (fieldType, fieldName, columnValue, columnName, readabilityRowNum, tempMap) -> {
                    System.out.println("--------------第" + readabilityRowNum + "行自定义字符串校验与赋值处理器，做些校验------------------");
                    Set<EasyConstraintViolationImpl<Object>> set = new HashSet<>();
                    EasyConstraintViolationImpl<Object> v1 = StringUtil.stringToInteger(fieldType, fieldName, columnValue,
                            columnName, true, readabilityRowNum);
                    EasyConstraintViolationImpl<Object> v2 = StringUtil.stringToDouble(fieldType, fieldName, columnValue,
                            columnName, true, readabilityRowNum);
                    EasyConstraintViolationImpl<Object> v3 = StringUtil.stringToDate(fieldType, fieldName, columnValue,
                            columnName, true, readabilityRowNum, tempMap);
                    if (v1 != null) {
                        set.add(v1);
                    }
                    if (v2 != null) {
                        set.add(v2);
                    }
                    if (v3 != null) {
                        set.add(v3);
                    }
                    if (CollUtil.isEmpty(set)) {
                        return CustomValidatorResult.passValid();
                    }

                    return CustomValidatorResult.notPassValid(set);
                },
                (str) -> {
                    System.out.println("---------------自定义字符串格式化处理器-----------------------");
                    return StringUtil.trimBlankCharacter(str);
                },
                (dataList) -> {
                    System.out.println("--------------自定义校验器，做些校验------------------");
                    ExcelDTO dto = dataList.get(0);
                    String argName = "name";
                    String value = dto.getName();
                    EasyConstraintViolationImpl<ExcelDTO> violation =
                            EasyConstraintViolationImpl.forBeanValidation("Excel表格某列不通过校验", argName, value);
                    Set<EasyConstraintViolationImpl<ExcelDTO>> set = new HashSet<>();
                    set.add(violation);
                    return CustomValidatorResult.notPassValid(set);
                }
        );

        ValidatorResult<ExcelDTO> validatorResult =
                ExcelValidateUtil.cellValid(multipartFile, columnMap, ExcelDTO.class, options);


        boolean isValid = validatorResult.isValid();
        Assert.assertFalse("预期不能够通过校验，现在却提示通过", isValid);

        List<ExcelDTO> data = validatorResult.getData();
        Assert.assertNotNull("预期有数据", data);
        System.out.println("---------控制台打印从Excel表格中获取到的数据----------");
        System.out.println(validatorResult);
        System.out.println(data);

    }

    @Test
    public void readExcelTypeTransitionTest() {
        // 测试将类型无法转换的参数，set到属性中会发生什么异常
        // java.lang.IllegalArgumentException: argument type mismatch
        Class<?> clazz = ExcelDTO.class;
        Field cpEname = null;
        try {
            cpEname = clazz.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Object newInstance = null;
        try {
            newInstance = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Class<?> cpEnameType = cpEname.getType();
        Method method = null;
        try {
            method = clazz.getMethod("setName", cpEnameType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(newInstance, "哈哈");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void typeTest() {
        Class<NumberTest> numberTestClass = NumberTest.class;
        for (Field field : numberTestClass.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            boolean baseIntegerType = NumUtil.isPrimitiveIntegerType(fieldType);
            System.out.println("原始整数类型" + baseIntegerType);
            boolean baseDoubleType = NumUtil.isPrimitiveDoubleType(fieldType);
            System.out.println("原始小数类型" + baseDoubleType);
            boolean wrapperIntegerType = NumUtil.isWrapperIntegerType(fieldType);
            System.out.println("包装整数类型" + wrapperIntegerType);
            boolean wrapperDoubleType = NumUtil.isWrapperDoubleType(fieldType);
            System.out.println("包装小数类型" + wrapperDoubleType);
        }
    }

}
