package com.hippocp.easy.code.util.excel.handler;

import cn.hutool.core.collection.CollUtil;
import com.hippocp.easy.code.util.domain.CustomValidatorResult;
import com.hippocp.easy.code.util.domain.EasyConstraintViolationImpl;
import com.hippocp.easy.code.util.excel.CustomValidAssignmentStrHandler;
import com.hippocp.easy.code.util.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 一个预设的自定义校验与赋值处理器，其中有3个子处理器<br>
 *
 * @author ZhouYifan
 * @date 2022/1/13
 */
public class ValidAssignmentStrDefaultHandler implements CustomValidAssignmentStrHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidAssignmentStrDefaultHandler.class);

    @Override
    public CustomValidatorResult<?> validAssignmentStr(Class<?> fieldType,
                                                       String fieldName,
                                                       String columnValue,
                                                       String columnName,
                                                       int readabilityRowNum,
                                                       Map<String, Object> tempStorageMap) {

        if (log.isDebugEnabled()) {
            log.debug("------第{}行自定义字符串校验与赋值处理器------", readabilityRowNum);
        }
        // 存放约束违反信息Set
        Set<EasyConstraintViolationImpl<Object>> set = new HashSet<>();
        // 第一个自定义校验与赋值处理器
        EasyConstraintViolationImpl<Object> v1 = StringUtil.stringToInteger(fieldType, fieldName, columnValue,
                columnName, true, readabilityRowNum);

        // 第二个自定义校验与赋值处理器
        EasyConstraintViolationImpl<Object> v2 = StringUtil.stringToDouble(fieldType, fieldName, columnValue,
                columnName, true, readabilityRowNum);

        // 第三个自定义校验与赋值处理器
        EasyConstraintViolationImpl<Object> v3 = StringUtil.stringToDate(fieldType, fieldName, columnValue,
                columnName, true, readabilityRowNum, tempStorageMap);

        // 第一个校验规则约束违反信息不为空，添加到Set
        if (v1 != null) {
            set.add(v1);
        }
        // 第二个约束违反信息不为空，添加到Set
        if (v2 != null) {
            set.add(v2);
        }
        // 第三个约束违反信息不为空，添加到Set
        if (v3 != null) {
            set.add(v3);
        }

        // 存放约束违反信息Set为空，代表校验通过
        if (CollUtil.isEmpty(set)) {

            return CustomValidatorResult.passValid();

        }

        // 否则代表校验失败
        return CustomValidatorResult.notPassValid(set);

    }

}
