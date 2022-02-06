package com.hippocp.easy.code.util.network;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.hippocp.easy.code.domain.response.HttpStatusCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTPUtil工具类，包含请求发送与响应接收
 *
 * @author ZhouYifan
 * @date 2021/8/3
 */
public class HTTPUtil {

    private static final Logger log = LoggerFactory.getLogger(HTTPUtil.class);

    /**
     * 发送delete请求返回响应，支持 header 参数设置，带重试机制，默认3次重试（包含初次请求），可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求url
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @param retryTime        重试次数 填写0即表示使用默认值
     * @return 响应对象
     */
    public static HttpResponse delete(String requestUrl,
                                      String headerParamName,
                                      String headerParamValue,
                                      int timeout, int retryTime) {
        // 检查重试次数
        retryTime = checkRetryTime(retryTime);
        HttpResponse httpResponse = null;
        // 在循环中请求
        for (int i = 0; i < retryTime; i++) {
            httpResponse = delete(requestUrl, headerParamName, headerParamValue, timeout);
            // 防止NPE，为空肯定请求失败
            if (httpResponse == null) {
                // 结束当次循环进入下次循环
                continue;
            }
            // httpResponse不为空正常执行
            // 获取状态码
            int statusCode = httpResponse.getStatus();
            // 定义请求成功状态码
            final int successStatusCode = HttpStatusCodeEnum.OK.getStatusCode();
            if (statusCode == successStatusCode) {
                // 如果状态码为200，结束循环不再次发起请求
                break;
            } else {
                // 否则继续循环，再次发起请求，直到retryTime次重试完成（retryTime默认为3）
                continue;
            }
        }
        requestLogRecord("DELETE", requestUrl, headerParamName, headerParamValue, timeout, retryTime);
        responseLogRecord(httpResponse);
        // 返回HttpResponse对象
        return httpResponse;
    }

    /**
     * 发送delete请求返回响应，支持 header 参数设置，可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求url
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @return 响应对象
     */
    protected static HttpResponse delete(String requestUrl,
                                         String headerParamName,
                                         String headerParamValue,
                                         int timeout) {
        timeout = checkTimeout(timeout);
        HttpRequest httpRequest = HttpRequest.delete(requestUrl).header(headerParamName, headerParamValue)
                .timeout(timeout);
        httpRequestObjectLogRecord(httpRequest);
        return httpRequest.execute();
    }

    /**
     * 发送put请求返回响应，支持 header 参数设置，带重试机制，默认3次重试（包含初次请求），可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求url
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @param retryTime        重试次数 填写0即表示使用默认值
     * @return 响应对象
     */
    public static HttpResponse put(String requestUrl,
                                   String headerParamName,
                                   String headerParamValue,
                                   int timeout, int retryTime) {
        // 检查重试次数
        retryTime = checkRetryTime(retryTime);
        HttpResponse httpResponse = null;
        // 在循环中请求
        for (int i = 0; i < retryTime; i++) {
            httpResponse = put(requestUrl, headerParamName, headerParamValue, timeout);
            // 防止NPE，为空肯定请求失败
            if (httpResponse == null) {
                // 结束当次循环进入下次循环
                continue;
            }
            // httpResponse不为空正常执行
            // 获取状态码
            int statusCode = httpResponse.getStatus();
            // 定义请求成功状态码
            final int successStatusCode = HttpStatusCodeEnum.OK.getStatusCode();
            if (statusCode == successStatusCode) {
                // 如果状态码为200，结束循环不再次发起请求
                break;
            } else {
                // 否则继续循环，再次发起请求，直到retryTime次重试完成（retryTime默认为3）
                continue;
            }
        }
        requestLogRecord("PUT", requestUrl, headerParamName, headerParamValue, timeout, retryTime);
        responseLogRecord(httpResponse);
        // 返回HttpResponse对象
        return httpResponse;
    }

    /**
     * 发送put请求返回响应，支持 header 参数设置，可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求url
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @return 响应对象
     */
    protected static HttpResponse put(String requestUrl,
                                      String headerParamName,
                                      String headerParamValue,
                                      int timeout) {
        timeout = checkTimeout(timeout);
        HttpRequest httpRequest = HttpRequest.put(requestUrl).header(headerParamName, headerParamValue)
                .timeout(timeout);
        httpRequestObjectLogRecord(httpRequest);
        return httpRequest.execute();
    }

    /**
     * 发送get请求返回响应，支持 header 参数设置，带重试机制，默认3次重试（包含初次请求），可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求url
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @param retryTime        重试次数 填写0即表示使用默认值
     * @return 响应对象
     */
    public static HttpResponse get(String requestUrl,
                                   String headerParamName,
                                   String headerParamValue,
                                   int timeout, int retryTime) {
        // 检查重试次数
        retryTime = checkRetryTime(retryTime);
        HttpResponse httpResponse = null;
        // 在循环中请求
        for (int i = 0; i < retryTime; i++) {
            httpResponse = get(requestUrl, headerParamName, headerParamValue, timeout);
            // 防止NPE，为空肯定请求失败
            if (httpResponse == null) {
                // 结束当次循环进入下次循环
                continue;
            }
            // httpResponse不为空正常执行
            // 获取状态码
            int statusCode = httpResponse.getStatus();
            // 定义请求成功状态码
            final int successStatusCode = HttpStatusCodeEnum.OK.getStatusCode();
            if (statusCode == successStatusCode) {
                // 如果状态码为200，结束循环不再次发起请求
                break;
            } else {
                // 否则继续循环，再次发起请求，直到retryTime次重试完成（retryTime默认为3）
                continue;
            }
        }
        requestLogRecord("GET", requestUrl, headerParamName, headerParamValue, timeout, retryTime);
        responseLogRecord(httpResponse);
        // 返回HttpResponse对象
        return httpResponse;
    }

    /**
     * 发送get请求返回响应，可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求url
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @return 响应对象
     */
    protected static HttpResponse get(String requestUrl,
                                      String headerParamName,
                                      String headerParamValue,
                                      int timeout) {
        timeout = checkTimeout(timeout);
        HttpRequest httpRequest = HttpRequest.get(requestUrl).header(headerParamName, headerParamValue)
                .timeout(timeout);
        httpRequestObjectLogRecord(httpRequest);
        return httpRequest.execute();
    }

    /**
     * 发送POST请求返回响应体，支持 header 参数设置，带重试机制，默认3次重试（包含初次请求），可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求地址url
     * @param jsonParam        JSON参数 支持的对象：String: 转换为相应的对象 Array Collection：转换为JSONArray Bean对象：转为JSONObject
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间 填写0即表示使用默认值
     * @param retryTime        重试次数 填写0即表示使用默认值
     * @return 响应对象
     */
    public static HttpResponse post(String requestUrl, Object jsonParam,
                                    String headerParamName, String headerParamValue,
                                    int timeout, int retryTime) {
        // 检查重试次数
        retryTime = checkRetryTime(retryTime);
        HttpResponse httpResponse = null;
        // 在循环中请求
        for (int i = 0; i < retryTime; i++) {
            httpResponse = post(requestUrl, jsonParam, headerParamName, headerParamValue, timeout);
            // 防止NPE，为空肯定请求失败
            if (httpResponse == null) {
                // 结束当次循环进入下次循环
                continue;
            }
            // httpResponse不为空正常执行
            // 获取状态码
            int statusCode = httpResponse.getStatus();
            // 定义请求成功状态码
            final int successStatusCode = HttpStatusCodeEnum.OK.getStatusCode();
            if (statusCode == successStatusCode) {
                // 如果状态码为200，结束循环不再次发起请求
                break;
            } else {
                // 否则继续循环，再次发起请求，直到retryTime次重试完成（retryTime默认为3）
                continue;
            }
        }
        postRequestLogRecord("POST", requestUrl, jsonParam, headerParamName, headerParamValue,
                timeout, retryTime);
        responseLogRecord(httpResponse);
        // 返回HttpResponse对象
        return httpResponse;
    }

    /**
     * 发送POST请求返回响应体，支持 header 参数设置，可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl       请求地址url
     * @param jsonParam        JSON参数 支持的对象：String: 转换为相应的对象 Array Collection：转换为JSONArray Bean对象：转为JSONObject
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求头参数值
     * @param timeout          超时时间
     * @return 响应对象
     */
    protected static HttpResponse post(String requestUrl, Object jsonParam,
                                       String headerParamName, String headerParamValue,
                                       int timeout) {
        timeout = checkTimeout(timeout);
        // 参数转为JSON字符串
        String param = JSONUtil.toJsonStr(jsonParam);
        // 发送请求，获得响应对象
        HttpRequest httpRequest = HttpRequest.post(requestUrl).header(headerParamName, headerParamValue)
                .timeout(timeout).body(param);
        httpRequestObjectLogRecord(httpRequest);
        return httpRequest.execute();
    }

    /**
     * 发送POST请求返回响应体，支持 header 参数设置，带重试机制，默认3次重试（包含初次请求），可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl 请求地址url
     * @param jsonParam  JSON参数 支持的对象：String: 转换为相应的对象 Array Collection：转换为JSONArray Bean对象：转为JSONObject
     * @param timeout    超时时间，单位：毫秒，填写0即表示使用默认值
     * @param retryTime  重试次数 填写0即表示使用默认值
     * @return 响应对象
     */
    public static HttpResponse post(String requestUrl, Object jsonParam,
                                    int timeout, int retryTime) {
        // 检查重试次数
        retryTime = checkRetryTime(retryTime);
        HttpResponse httpResponse = null;
        // 在循环中请求
        for (int i = 0; i < retryTime; i++) {
            httpResponse = post(requestUrl, jsonParam, timeout);
            // 防止NPE，为空肯定请求失败
            if (httpResponse == null) {
                // 结束当次循环进入下次循环
                continue;
            }
            // httpResponse不为空正常执行
            // 获取状态码
            int statusCode = httpResponse.getStatus();
            // 定义请求成功状态码
            final int successStatusCode = HttpStatusCodeEnum.OK.getStatusCode();
            if (statusCode == successStatusCode) {
                // 如果状态码为200，结束循环不再次发起请求
                break;
            } else {
                // 否则继续循环，再次发起请求，直到retryTime次重试完成（retryTime默认为3）
                continue;
            }
        }
        final String msg = "该请求不设置";
        postRequestLogRecord("POST", requestUrl, jsonParam, msg, msg, timeout, retryTime);
        responseLogRecord(httpResponse);
        // 返回HttpResponse对象
        return httpResponse;

    }

    /**
     * 发送POST请求返回响应体，可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl 请求地址url
     * @param jsonParam  JSON参数 支持的对象：String: 转换为相应的对象 Array Collection：转换为JSONArray Bean对象：转为JSONObject
     * @param timeout    超时时间，单位：毫秒
     * @return 响应对象
     */
    protected static HttpResponse post(String requestUrl, Object jsonParam, int timeout) {
        timeout = checkTimeout(timeout);
        // 参数转为JSON字符串
        String param = JSONUtil.toJsonStr(jsonParam);
        // 发送请求，获得响应对象
        HttpRequest httpRequest = HttpRequest.post(requestUrl).timeout(timeout).body(param);
        httpRequestObjectLogRecord(httpRequest);
        return httpRequest.execute();
    }

    /**
     * 发送POST请求返回响应主体信息，带重试机制，默认3次重试（包含初次请求），可设置请求超时时间，默认5000毫秒
     *
     * @param requestUrl 请求地址url
     * @param jsonParam  JSON参数 支持的对象：String: 转换为相应的对象 Array Collection：转换为JSONArray Bean对象：转为JSONObject
     * @param timeout    超时时间，单位：毫秒
     * @param retryTime  重试次数
     * @return 响应主体JSON字符串
     */
    public static String postReturnBody(String requestUrl, Object jsonParam, int timeout, int retryTime) {
        HttpResponse httpResponse = post(requestUrl, jsonParam, timeout, retryTime);
        return httpResponse.body();
    }

    /**
     * 检查超时时间 超时时间小于等于0 设为默认值5000毫秒
     *
     * @param timeout 超时时间 毫秒
     * @return 默认超时时间
     */
    protected static int checkTimeout(int timeout) {
        // 超时时间小于等于0
        if (timeout < 1) {
            timeout = 5000;
        }
        return timeout;
    }

    /**
     * 检查重试次数 重试次数小于等于0 设为默认值3次
     *
     * @param retryTime 重试次数
     * @return 默认重试次数
     */
    protected static int checkRetryTime(int retryTime) {
        // 重试次数小于等于0
        if (retryTime < 1) {
            retryTime = 3;
        }
        return retryTime;
    }

    /**
     * 响应日志记录
     *
     * @param httpResponse 响应对象
     */
    protected static void responseLogRecord(HttpResponse httpResponse) {
        // 如果开启Debug日志输出，执行日志记录
        if (log.isDebugEnabled()) {
            log.debug("http请求响应对象信息：{}", httpResponse);
        }
    }

    /**
     * POST请求日志记录
     *
     * @param requestType      请求类型
     * @param jsonParam        json参数 一般是对象
     * @param requestUrl       请求参数
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求参数值
     * @param timeout          超时时间
     * @param retryTime        重试次数
     */
    protected static void postRequestLogRecord(String requestType,
                                               String requestUrl,
                                               Object jsonParam,
                                               String headerParamName,
                                               String headerParamValue,
                                               int timeout, int retryTime) {
        // 如果开启Debug日志输出，执行日志记录
        if (log.isDebugEnabled()) {
            log.debug("请求类型：{}，请求url：{}，请求参数：{}，请求头参数名：{}、参数值：{}，超时设置（单位：毫秒，0代表默认值5000毫秒）：{}，重试次数：{}",
                    requestType, requestUrl, jsonParam, headerParamName, headerParamValue,
                    timeout, retryTime);
        }
    }

    /**
     * GET/PUT/DELETE请求日志记录
     *
     * @param requestType      请求类型
     * @param requestUrl       请求参数
     * @param headerParamName  请求头参数名
     * @param headerParamValue 请求参数值
     * @param timeout          超时时间
     * @param retryTime        重试次数
     */
    protected static void requestLogRecord(String requestType,
                                           String requestUrl,
                                           String headerParamName,
                                           String headerParamValue,
                                           int timeout, int retryTime) {
        // 如果开启Debug日志输出，执行日志记录
        if (log.isDebugEnabled()) {
            log.debug("请求类型：{}，请求url与参数：{}，请求头参数名：{}、参数值：{}，超时设置（单位：毫秒，0代表默认值5000毫秒）：{}，重试次数：{}",
                    requestType, requestUrl, headerParamName, headerParamValue, timeout, retryTime);
        }
    }

    /**
     * http请求对象日志记录
     *
     * @param httpRequest http请求对象
     */
    protected static void httpRequestObjectLogRecord(HttpRequest httpRequest) {
        // 如果开启Debug日志输出，执行日志记录
        if (log.isDebugEnabled()) {
            log.debug("http请求对象信息：{}", httpRequest);
        }
    }

    private HTTPUtil() {
    }

}
