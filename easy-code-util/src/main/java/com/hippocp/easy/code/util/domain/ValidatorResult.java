package com.hippocp.easy.code.util.domain;

import cn.hutool.core.collection.CollUtil;
import com.hippocp.easy.code.domain.validate.ConstraintError;
import com.hippocp.easy.code.util.parse.ConstraintErrorParseUtil;
import lombok.Data;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 校验器校验结果类<br>
 * 由ExcelValidateUtil类中方法返回值取得对象时不会为null<br>
 * 包含属性 isValid 校验是否通过<br>
 * data 校验解析出的数据 例如：校验Excel表格后解析出数据<br>
 * violationSet T 约束违反信息<br>
 * readViolationSet ? 约束违反信息<br>
 * constraintError 约束错误对象使用 violationSet 与 readViolationSet 中数据创建<br>
 *
 * @author ZhouYifan
 * @date 2022/1/7
 */
@Data
public class ValidatorResult<T> implements Serializable {

    /**
     * 是否通过校验，true-通过，false-不通过
     */
    private boolean isValid;

    /**
     * 数据
     */
    private List<T> data;

    /**
     * 描述约束违规。此对象公开约束违规上下文以及描述违规的消息
     */
    private Set<ConstraintViolation<T>> violationSet;

    /**
     * 数据读取过程中产生的校验约束违反信息
     */
    private Set<ConstraintViolation<?>> readViolationSet;

    /**
     * 约束错误对象，提供违反约束参数名、参数值、消息<br>
     * 注意：该属性会由校验器工具类调用以下方法自动生成<br>
     *
     * @see ValidatorResult#generateConstraintError()
     */
    private List<ConstraintError> constraintError;

    /**
     * 生成属性 constraintError，校验器会主动调用该方法，无需手动调用
     */
    public void generateConstraintError() {
        List<ConstraintError> errors = ConstraintErrorParseUtil.getConstraintErrorForType(this.violationSet);
        List<ConstraintError> readErrors = ConstraintErrorParseUtil.getConstraintError(this.readViolationSet);
        List<ConstraintError> list = new ArrayList<>();
        boolean isNotEmptyErrors = CollUtil.isNotEmpty(errors);
        if (isNotEmptyErrors) {
            list.addAll(errors);
        }
        boolean isNotEmptyReadErrors = CollUtil.isNotEmpty(readErrors);
        if (isNotEmptyReadErrors) {
            list.addAll(readErrors);
        }
        this.constraintError = list;
    }

    public ValidatorResult(Boolean isValid) {
        this.isValid = isValid;
    }

    public ValidatorResult(boolean isValid, List<T> data, Set<ConstraintViolation<T>> violationSet) {
        this.isValid = isValid;
        this.data = data;
        this.violationSet = violationSet;
    }

    public ValidatorResult(boolean isValid, List<T> data, Set<ConstraintViolation<T>> violationSet, Set<ConstraintViolation<?>> readViolationSet) {
        this.isValid = isValid;
        this.data = data;
        this.violationSet = violationSet;
        this.readViolationSet = readViolationSet;
    }

    private ValidatorResult() {
    }

}
