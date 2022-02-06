package com.hippocp.easy.code.domain.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 约束错误对象<br>
 * 提供违反约束参数名、参数值、消息<br>
 *
 * @author ZhouYifan
 * @date 2021/8/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintError {

    /**
     * 违反约束参数名
     */
    private String argsName;

    /**
     * 违反约束参数值
     */
    private Object argsValue;

    /**
     * 违反约束消息
     */
    private String message;

}
