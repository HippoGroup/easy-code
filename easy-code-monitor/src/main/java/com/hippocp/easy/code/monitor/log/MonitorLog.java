package com.hippocp.easy.code.monitor.log;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.hippocp.easy.code.util.network.IpAddressUtil;
import com.hippocp.easy.code.util.string.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 监控日志类
 *
 * @author ZhouYifan
 * @date 2022/1/5
 */
public class MonitorLog {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(MonitorLog.class);

    /**
     * HTTP请求监控日志<br>
     * 切点由调用者决定<br>
     *
     * @param joinPoint 连接点
     * @throws Throwable 所有异常和错误基类
     */
    public static Object httpRequestMonitorLogRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        // 切面方法开始执行时间
        long aspectStartExecuteTime = System.currentTimeMillis();

        StringBuffer logMsg = new StringBuffer();
        // 获取当前系统换行符
        String lineBreak = StringUtil.presentOsLineBreak();
        // 日志添加
        logMsg.append("----------------监控到controller层HTTP接口方法执行----------------");
        logMsg.append(lineBreak);

        // 获取 ServletRequestAttributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {

            logMsg.append("ServletRequestAttributes对象为null");
            logMsg.append(lineBreak);
            logMsg.append("可能情况如下：当前被监控方法可能被");
            logMsg.append(lineBreak);
            logMsg.append("1.定时任务调用");
            logMsg.append(lineBreak);
            logMsg.append("2.普通调用");
            logMsg.append(lineBreak);

        } else {
            // 获取 HttpServletRequest
            HttpServletRequest request = attributes.getRequest();

            // 记录请求的 IP地址
            String ipAddress = IpAddressUtil.getIpAddress(request);
            logMsg.append("request side IP address : ").append(ipAddress);
            logMsg.append(lineBreak);

            // 记录请求 url
            String url = request.getRequestURL().toString();
            logMsg.append("request URL             : ").append(url);
            logMsg.append(lineBreak);

            // 记录Http method
            String httpMethod = request.getMethod();
            logMsg.append("HTTP Method             : ").append(httpMethod);
            logMsg.append(lineBreak);


            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 类全限定名
            String className = methodSignature.getDeclaringTypeName();
            // 方法名
            String methodName = methodSignature.getName();

            // 记录被调用 controller 层方法类全限定名以及执行方法
            logMsg.append("Class Method            : ").append(className).append(".").append(methodName);
            logMsg.append(lineBreak);

            // 记录请求入参
            logMsg.append("Request Args            : ");
            logMsg.append(lineBreak);
            // 参数值
            Object[] args = joinPoint.getArgs();
            // 参数名
            String[] argNames = methodSignature.getParameterNames();
            //
            boolean isNotEmptyArgs = ArrayUtil.isNotEmpty(args);
            boolean isNotEmptyArgNames = ArrayUtil.isNotEmpty(argNames);
            // 参数值和参数名都不为空时
            if (isNotEmptyArgs && isNotEmptyArgNames) {
                // 循环拼接记录参数
                for (int i = 0; i < args.length; i++) {
                    logMsg.append("参数名：").append(argNames[i]).append("，")
                            .append("参数值：").append(args[i]).append("。").append(lineBreak);
                }
            }

        }

        // 被监控方法开始执行时间
        long startExecuteTime = System.currentTimeMillis();
        // 执行业务逻辑
        Object result = joinPoint.proceed();
        // 被监控方法结束执行时间
        long endExecuteTime = System.currentTimeMillis();

        String resultJson = "";
        // 被监控方法运行结果不为空，则转换为json字符串以此查看响应结果
        if (result != null) {
            resultJson = JSONUtil.toJsonStr(result);
        }

        // 记录方法返回结果
        logMsg.append("Method return value     : ").append(resultJson);
        logMsg.append(lineBreak);

        // 被监控方法执行时长
        long countExecuteTime = endExecuteTime - startExecuteTime;
        // 记录被监控方法执行时长
        logMsg.append("被监控方法执行时长         : ").append(countExecuteTime).append(" ms");
        logMsg.append(lineBreak);

        // 切面方法结束执行时间
        long aspectEndExecuteTime = System.currentTimeMillis();
        // 记录切面方法与被监控方法总执行时长
        long aspectCountExecuteTime = aspectEndExecuteTime - aspectStartExecuteTime;
        logMsg.append("切面方法与被监控方法总执行时长   : ").append(aspectCountExecuteTime).append(" ms");
        logMsg.append(lineBreak);

        logMsg.append("----------------本次监控信息记录完毕----------------");

        // 输出日志信息
        if (log.isInfoEnabled()) {
            log.info(logMsg.toString());
        }

        // 返回被监控方法执行结果
        return result;

    }

}
