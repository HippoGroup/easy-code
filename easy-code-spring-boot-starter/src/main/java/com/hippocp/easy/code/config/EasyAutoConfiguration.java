package com.hippocp.easy.code.config;

import com.hippocp.easy.code.exception.aspect.advice.ExceptionHandlerAdvice;
import com.hippocp.easy.code.exception.config.ExceptionHandlerProperties;
import com.hippocp.easy.code.exception.handler.GlobalExceptionHandler;
import com.hippocp.easy.code.monitor.aspect.HttpRequestMonitorLogAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * easy自动配置类
 *
 * @author ZhouYifan
 * @date 2021/11/22
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EasyProperties.class)
@ConditionalOnProperty(prefix = EasyPropertiesConstant.PREFIX,
        name = "enable",
        havingValue = "true",
        matchIfMissing = true)
public class EasyAutoConfiguration {

    @Resource
    private EasyProperties easyProperties;

    /**
     * 当{@link ExceptionHandlerProperties}实例不存在时，将{@link ExceptionHandlerProperties}的实例放入spring容器<br>
     * 未直接读取{@link EasyProperties}中的 globalExceptionHandlerOrder 属性<br>
     * 而是读取{@link ExceptionHandlerProperties } 中的 globalExceptionHandlerOrder 属性
     *
     * @return {@link ExceptionHandlerProperties}
     */
    @Bean
    @ConditionalOnMissingBean(ExceptionHandlerProperties.class)
    public ExceptionHandlerProperties setExceptionHandlerProperties() {
        ExceptionHandlerProperties properties = new ExceptionHandlerProperties();
        Integer order = easyProperties.getGlobalExceptionHandlerOrder();
        properties.setGlobalExceptionHandlerOrder(order);
        if (log.isInfoEnabled()) {
            log.info("实例化 ExceptionHandlerProperties 异常处理器属性类");
        }
        return properties;
    }

    /**
     * 当{@link GlobalExceptionHandler}实例不存在时，将{@link GlobalExceptionHandler}的实例放入spring容器
     *
     * @return {@link GlobalExceptionHandler}
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler setGlobalExceptionHandler() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        Integer order = easyProperties.getGlobalExceptionHandlerOrder();
        if (log.isInfoEnabled()) {
            log.info("已开启全局异常处理器order值为：{}，已实例化 GlobalExceptionHandler", order);
        }
        return globalExceptionHandler;
    }

    /**
     * 当{@link ExceptionHandlerAdvice}实例不存在时，将{@link ExceptionHandlerAdvice}的实例放入spring容器
     *
     * @return {@link ExceptionHandlerAdvice}
     */
    @Bean
    @ConditionalOnMissingBean(ExceptionHandlerAdvice.class)
    public ExceptionHandlerAdvice setExceptionHandlerAdvice() {
        ExceptionHandlerAdvice exceptionHandlerAdvice = new ExceptionHandlerAdvice();
        if (log.isInfoEnabled()) {
            log.info("已开启异常处理日志记录增强，已实例化 ExceptionHandlerAdvice");
        }
        return exceptionHandlerAdvice;
    }

    /**
     * 当配置<br>
     * easy.http-request-monitor-log-advice=true 或者未填写此配置<br>
     * 并且<br>
     * 当{@link HttpRequestMonitorLogAdvice}实例不存在时，将{@link HttpRequestMonitorLogAdvice}的实例放入spring容器
     *
     * @return {@link HttpRequestMonitorLogAdvice}
     */
    @Bean
    @ConditionalOnMissingBean(HttpRequestMonitorLogAdvice.class)
    @ConditionalOnProperty(prefix = EasyPropertiesConstant.PREFIX,
            name = "http-request-monitor-log-advice",
            havingValue = "true",
            matchIfMissing = true)
    public HttpRequestMonitorLogAdvice setHttpRequestMonitorLogAdvice() {
        HttpRequestMonitorLogAdvice httpRequestMonitorLogAdvice = new HttpRequestMonitorLogAdvice();
        if (log.isInfoEnabled()) {
            log.info("已开启Http请求监控日志增强，已实例化 HttpRequestMonitorLogAdvice");
        }
        return httpRequestMonitorLogAdvice;
    }

}
