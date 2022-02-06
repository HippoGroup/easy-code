package com.hippocp.easy.code.domain.response;

/**
 * 响应信息包装类
 *
 * @author ZhouYiFan
 * @date 2021/04/16
 */
public class ResponseWrapper<T> {

    /**
     * 请求状态 布尔值
     */
    private boolean status;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public ResponseWrapper(boolean status, ErrorCodeEnum errorCodeEnum) {
        this.status = status;
        this.errorCode = errorCodeEnum.getErrorCode();
        this.message = errorCodeEnum.getDescribe();
    }

    public ResponseWrapper(boolean status, ErrorCodeEnum errorCodeEnum, T data) {
        this.status = status;
        this.errorCode = errorCodeEnum.getErrorCode();
        this.message = errorCodeEnum.getDescribe();
        this.data = data;
    }

    public ResponseWrapper(boolean status, ErrorCodeEnum errorCodeEnum, String message) {
        this.status = status;
        this.errorCode = errorCodeEnum.getErrorCode();
        this.message = message;
    }

    public ResponseWrapper(boolean status, ErrorCodeEnum errorCodeEnum, String message, T data) {
        this.status = status;
        this.errorCode = errorCodeEnum.getErrorCode();
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ResponseWrapper{");
        sb.append("status=").append(status);
        sb.append(", errorCode='").append(errorCode).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
