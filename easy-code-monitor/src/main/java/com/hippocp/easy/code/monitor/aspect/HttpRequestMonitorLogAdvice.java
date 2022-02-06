package com.hippocp.easy.code.monitor.aspect;

import com.hippocp.easy.code.monitor.log.MonitorLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Http请求监控日志切面增强类
 *
 * @author ZhouYifan
 * @date 2021/12/30
 */
@Component
@Aspect
public class HttpRequestMonitorLogAdvice {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(HttpRequestMonitorLogAdvice.class);

    /**
     * HttpRequestMonitorLogAble 切点
     */
    @Pointcut("@annotation(com.hippocp.easy.code.monitor.annotation.HttpRequestMonitorLogAble)")
    public void httpRequestMonitorLogAblePointCut() {
    }

    /**
     * RequestMapping 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingPointCut() {
    }

    /**
     * PostMapping 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingPointCut() {
    }

    /**
     * GetMapping 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMappingPointCut() {
    }

    /**
     * DeleteMapping 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMappingPointCut() {
    }

    /**
     * PutMapping 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMappingPointCut() {
    }

    /**
     * PatchMapping 切点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchMappingPointCut() {
    }

    /**
     * HttpRequestMonitorLogAble 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("httpRequestMonitorLogAblePointCut()")
    public Object httpRequestMonitorLogAblePointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * RequestMapping 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("requestMappingPointCut()")
    public Object requestMappingPointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * GetMapping 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("getMappingPointCut()")
    public Object getMappingPointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * PostMapping 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("postMappingPointCut()")
    public Object postMappingPointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * deleteMapping 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("deleteMappingPointCut()")
    public Object deleteMappingPointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * patchMapping 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("patchMappingPointCut()")
    public Object patchMappingPointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * putMapping 切点 织入方法
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    @Around("putMappingPointCut()")
    public Object putMappingPointCutMonitorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return httpRequestMonitorLogRecord(joinPoint);
    }

    /**
     * HTTP请求监控日志<br>
     * 切点由调用者决定<br>
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    public Object httpRequestMonitorLogRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        return MonitorLog.httpRequestMonitorLogRecord(joinPoint);
    }

}
