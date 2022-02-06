package com.hippocp.easy.code.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * easy外部化配置属性
 *
 * @author ZhouYifan
 * @date 2021/11/22
 */
@Data
@ConfigurationProperties(prefix = "easy")
public class EasyProperties {

    /**
     * 是否开启全局异常处理增强能力，无此配置属性时视为开启全局异常处理
     */
    private Boolean enabled;

    /**
     * 全局异常处理器order值，数值越小优先级越高，最小值为Integer.MIN_VALUE，最大值为Integer.MAX_VALUE
     */
    private Integer globalExceptionHandlerOrder = Integer.MAX_VALUE;

    /**
     * 是否开启Http请求监控日志增强，无此配置属性时视为开启Http请求监控日志增强
     */
    private Boolean httpRequestMonitorLogAdvice;

}
