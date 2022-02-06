package com.hippocp.easy.code.exception.config;

import lombok.Data;

/**
 * 异常处理器属性
 *
 * @author ZhouYifan
 * @date 2021/11/22
 */
@Data
public class ExceptionHandlerProperties {

    /**
     * 全局异常处理器order值
     */
    private Integer globalExceptionHandlerOrder;

}
