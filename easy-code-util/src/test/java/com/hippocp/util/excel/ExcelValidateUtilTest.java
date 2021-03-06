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
        Assert.assertTrue("?????????.xls?????????????????? true??????????????????false", isNotExcelXLS);

        boolean isExcelXLS = ExcelValidateUtil.isExcelXLS(multipartFile);
        Assert.assertFalse("?????????.xls?????????????????? false??????????????????ture", isExcelXLS);

        boolean isNotExcelXLSX = ExcelValidateUtil.isNotExcelXLSX(multipartFile);
        Assert.assertFalse("???.xlsx?????????????????? false??????????????????ture", isNotExcelXLSX);

        boolean isExcelXLSX = ExcelValidateUtil.isExcelXLSX(multipartFile);
        Assert.assertTrue("???.xlsx?????????????????? ture??????????????????false", isExcelXLSX);

    }


    @Test
    public void excelTitleEqualsTest() {
        MultipartFile multipartFile = initMultipartFile();
        boolean excelTitleNotEquals = ExcelValidateUtil.excelTitleEquals(multipartFile, new String[]{"????????????01"});
        Assert.assertFalse("???????????????????????????????????????????????????", excelTitleNotEquals);

        boolean excelTitleEquals = ExcelValidateUtil.excelTitleEquals(multipartFile, new String[]{
                "????????????", "??????", "??????"});
        Assert.assertTrue("???????????????????????????????????????????????????", excelTitleEquals);

    }

    @Test
    public void cellPassValidateTest() {
        MultipartFile multipartFile = initMultipartFile();
        Map<String, String> columnMap = new HashMap<>(4);
        columnMap.put("????????????", "dataCode");
        columnMap.put("??????", "name");
        columnMap.put("??????", "age");

        // ??????????????? validatorFactory ???????????????????????? Spring ?????????????????????NPE
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
                    System.out.println("--------------???" + readabilityRowNum + "????????????????????????????????????????????????????????????------------------");
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
                    System.out.println("---------------????????????????????????????????????-----------------------");
                    return StringUtil.trimBlankCharacter(str);
                },
                (dataList) -> {
                    System.out.println("--------------?????????????????????????????????------------------");
                    ExcelDTO dto = dataList.get(0);
                    return CustomValidatorResult.passValid();
                }
        );

        ValidatorResult<ExcelDTO> validatorResult =
                ExcelValidateUtil.cellValid(multipartFile, columnMap, ExcelDTO.class, options);


        boolean isValid = validatorResult.isValid();
        Assert.assertTrue("???????????????????????????????????????????????????", isValid);

        List<ExcelDTO> data = validatorResult.getData();
        Assert.assertNotNull("???????????????", data);
        System.out.println("---------??????????????????Excel???????????????????????????----------");
        System.out.println(validatorResult);
        System.out.println(data);

    }

    @Test
    public void cellNotPassValidateTest() {
        MultipartFile multipartFile = initMultipartFile();
        Map<String, String> columnMap = new HashMap<>(4);
        columnMap.put("????????????", "dataCode");
        columnMap.put("??????", "name");
        columnMap.put("??????", "age");

        // ??????????????? validatorFactory ???????????????????????? Spring ?????????????????????NPE
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
                    System.out.println("--------------???" + readabilityRowNum + "????????????????????????????????????????????????????????????------------------");
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
                    System.out.println("---------------????????????????????????????????????-----------------------");
                    return StringUtil.trimBlankCharacter(str);
                },
                (dataList) -> {
                    System.out.println("--------------?????????????????????????????????------------------");
                    ExcelDTO dto = dataList.get(0);
                    String argName = "name";
                    String value = dto.getName();
                    EasyConstraintViolationImpl<ExcelDTO> violation =
                            EasyConstraintViolationImpl.forBeanValidation("Excel???????????????????????????", argName, value);
                    Set<EasyConstraintViolationImpl<ExcelDTO>> set = new HashSet<>();
                    set.add(violation);
                    return CustomValidatorResult.notPassValid(set);
                }
        );

        ValidatorResult<ExcelDTO> validatorResult =
                ExcelValidateUtil.cellValid(multipartFile, columnMap, ExcelDTO.class, options);


        boolean isValid = validatorResult.isValid();
        Assert.assertFalse("???????????????????????????????????????????????????", isValid);

        List<ExcelDTO> data = validatorResult.getData();
        Assert.assertNotNull("???????????????", data);
        System.out.println("---------??????????????????Excel???????????????????????????----------");
        System.out.println(validatorResult);
        System.out.println(data);

    }

    @Test
    public void readExcelTypeTransitionTest() {
        // ???????????????????????????????????????set?????????????????????????????????
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
            method.invoke(newInstance, "??????");
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
            System.out.println("??????????????????" + baseIntegerType);
            boolean baseDoubleType = NumUtil.isPrimitiveDoubleType(fieldType);
            System.out.println("??????????????????" + baseDoubleType);
            boolean wrapperIntegerType = NumUtil.isWrapperIntegerType(fieldType);
            System.out.println("??????????????????" + wrapperIntegerType);
            boolean wrapperDoubleType = NumUtil.isWrapperDoubleType(fieldType);
            System.out.println("??????????????????" + wrapperDoubleType);
        }
    }

}
