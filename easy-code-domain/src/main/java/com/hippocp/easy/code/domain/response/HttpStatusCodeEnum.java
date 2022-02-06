package com.hippocp.easy.code.domain.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Http状态码枚举类
 *
 * @author ZhouYifan
 * @date 2021/8/4
 */
public enum HttpStatusCodeEnum {

    /**
     * 继续。客户端应继续其请求
     */
    CONTINUE(100, "继续。客户端应继续其请求"),
    /**
     *
     */
    SWITCHING_PROTOCOLS(101,
            "切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议"),
    /**
     * 请求成功。一般用于GET与POST请求
     */
    OK(200, "请求成功。一般用于GET与POST请求"),
    /**
     * 已创建。成功请求并创建了新的资源
     */
    CREATED(201, "已创建。成功请求并创建了新的资源"),
    /**
     * 已接受。已经接受请求，但未处理完成
     */
    ACCEPTED(202, "已接受。已经接受请求，但未处理完成"),
    /**
     * 非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本
     */
    NON_AUTHORITATIVE_INFORMATION(203,
            "非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本"),
    /**
     * 无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
     */
    NO_CONTENT(204,
            "无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档"),
    /**
     * 重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域
     */
    RESET_CONTENT(205,
            "重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域"),
    /**
     * 部分内容。服务器成功处理了部分GET请求
     */
    PARTIAL_CONTENT(206, "部分内容。服务器成功处理了部分GET请求"),
    /**
     * 多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择
     */
    MULTIPLE_CHOICES(300,
            "多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择"),
    /**
     * 永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
     */
    MOVED_PERMANENTLY(301,
            "永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替"),
    /**
     * 临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
     */
    FOUND(302, "临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI"),
    /**
     * 查看其它地址。与301类似。使用GET和POST请求查看
     */
    SEE_OTHER(303, "查看其它地址。与301类似。使用GET和POST请求查看"),
    /**
     * 未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
     */
    NOT_MODIFIED(304,
            "未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源"),
    /**
     * 使用代理。所请求的资源必须通过代理访问
     */
    USE_PROXY(305, "使用代理。所请求的资源必须通过代理访问"),
    /**
     * 已经被废弃的HTTP状态码
     */
    UNUSED(306, "已经被废弃的HTTP状态码"),
    /**
     * 临时重定向。与302类似。使用GET请求重定向
     */
    TEMPORARY_REDIRECT(307, "临时重定向。与302类似。使用GET请求重定向"),
    /**
     * 客户端请求的语法错误，服务器无法理解
     */
    BAD_REQUEST(400, "客户端请求的语法错误，服务器无法理解"),
    /**
     * 请求要求用户的身份认证
     */
    UNAUTHORIZED(401, "请求要求用户的身份认证"),
    /**
     * 保留，将来使用
     */
    PAYMENT_REQUIRED(402, "保留，将来使用"),
    /**
     * 服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    FORBIDDEN(403, "服务器理解请求客户端的请求，但是拒绝执行此请求"),
    /**
     * 服务器无法根据客户端的请求找到资源（网页）
     */
    NOT_FOUND(404, "服务器无法根据客户端的请求找到资源（网页）"),
    /**
     * 客户端请求中的方法被禁止
     */
    METHOD_NOT_Allowed(405, "客户端请求中的方法被禁止"),
    /**
     * 服务器无法根据客户端请求的内容特性完成请求
     */
    NOT_ACCEPTABLE(406, "服务器无法根据客户端请求的内容特性完成请求"),
    /**
     * 请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权"),
    /**
     * 服务器等待客户端发送的请求时间过长，超时
     */
    REQUEST_TIME_OUT(408, "服务器等待客户端发送的请求时间过长，超时"),
    /**
     * 服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突
     */
    CONFLICT(409, "服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突"),
    /**
     * 客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置
     */
    GONE(410,
            "客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置"),
    /**
     * 服务器无法处理客户端发送的不带Content-Length的请求信息
     */
    LENGTH_REQUIRED(411, "服务器无法处理客户端发送的不带Content-Length的请求信息"),
    /**
     * 客户端请求信息的先决条件错误
     */
    PRECONDITION_FAILED(412, "客户端请求信息的先决条件错误"),
    /**
     * 由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，
     * 服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息
     */
    REQUEST_ENTITY_TOO_LARGE(413,
            "由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息"),
    /**
     * 请求的URI过长（URI通常为网址），服务器无法处理
     */
    REQUEST_URI_TOO_LARGE(414, "请求的URI过长（URI通常为网址），服务器无法处理"),
    /**
     * 服务器无法处理请求附带的媒体格式
     */
    UNSUPPORTED_MEDIA_TYPE(415, "服务器无法处理请求附带的媒体格式"),
    /**
     * 客户端请求的范围无效
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "客户端请求的范围无效"),
    /**
     * 服务器无法满足Expect的请求头信息
     */
    EXPECTATION_FAILED(417, "服务器无法满足Expect的请求头信息"),
    /**
     * 服务器内部错误，无法完成请求
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误，无法完成请求"),
    /**
     * 服务器不支持请求的功能，无法完成请求
     */
    NOT_IMPLEMENTED(501, "服务器不支持请求的功能，无法完成请求"),
    /**
     * 作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应
     */
    BAD_GATEWAY(502, "作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应"),
    /**
     * 由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
     */
    SERVICE_UNAVAILABLE(503,
            "由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中"),
    /**
     * 充当网关或代理的服务器，未及时从远端服务器获取请求
     */
    GATEWAY_TIME_OUT(504, "充当网关或代理的服务器，未及时从远端服务器获取请求"),
    /**
     * 服务器不支持请求的HTTP协议的版本，无法完成处理
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "服务器不支持请求的HTTP协议的版本，无法完成处理");

    /**
     * Http状态码
     */
    private final Integer statusCode;

    /**
     * Http状态码描述
     */
    private final String description;

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }

    HttpStatusCodeEnum(Integer statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    /**
     * 将枚举类转换为Map集合，statusCode为key，value为description
     *
     * @return {@link Map}集合
     */
    public static Map<Integer, String> getMap() {
        Map<Integer, String> map = new HashMap<>(16);
        HttpStatusCodeEnum[] values = HttpStatusCodeEnum.values();
        for (HttpStatusCodeEnum value : values) {
            Integer statusCode = value.getStatusCode();
            String description = value.getDescription();
            map.put(statusCode, description);
        }
        return map;
    }

}
