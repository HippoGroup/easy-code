package com.hippocp.easy.code.domain.response;

/**
 * 错误码
 * 参考自《Java开发手册》泰山版
 *
 * @author ZhouYiFan
 * @date 2021/04/16
 */
public enum ErrorCodeEnum {
    /**
     * 00000 请求成功一切正常 正确执行后的返回
     */
    SUCCESS("00000", "请求成功一切ok", "正确执行后的返回"),
    /**
     * A0001 用户端错误 一级宏观错误码
     */
    CLIENT_ERROR("A0001", "用户端错误", "一级宏观错误码"),
    /**
     * A0400 用户请求参数错误 二级宏观错误码
     */
    USER_REQUEST_PARAMETER_ERROR("A0400", "用户请求参数错误", "二级宏观错误码"),
    /**
     * A0410 请求必填参数为空
     */
    REQUEST_REQUIRED_PARAMETER_IS_NULL_ERROR("A0401", "请求必填参数为空", ""),
    /**
     * A0201 用户账户不存在
     */
    USER_ACCOUNT_NOT_EXISET_ERROR("A0201", "用户账户不存在", ""),
    /**
     * A0210 用户密码错误
     */
    USERNAME_PWD_ERROR("A0210", "用户密码错误", ""),
    /**
     * A0100 用户注册错误 二级宏观错误码
     */
    USER_REGISTER_ERROR("A0100", "用户注册错误", "二级宏观错误码"),
    /**
     * A0230 用户登陆已过期
     */
    USER_LOGIN_EXPIRE("A0230", "用户登陆已过期", ""),
    /**
     * B0001 系统执行出错 一级宏观错误码
     */
    SYSTEM_RUN_ERROR("B0001", "系统执行出错", "一级宏观错误码");

    /**
     * 错误码
     */
    private final String errorCode;
    /**
     * 描述
     */
    private final String describe;
    /**
     * 说明
     */
    private final String explain;

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescribe() {
        return describe;
    }

    public String getExplain() {
        return explain;
    }

    ErrorCodeEnum(String errorCode, String describe, String explain) {
        this.errorCode = errorCode;
        this.describe = describe;
        this.explain = explain;
    }
}
