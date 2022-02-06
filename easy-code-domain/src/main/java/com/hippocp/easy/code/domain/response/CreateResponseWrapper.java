package com.hippocp.easy.code.domain.response;

/**
 * 创建响应信息包装类的工具类
 *
 * @author ZhouYiFan
 * @date 2021/4/17
 */
public class CreateResponseWrapper {

    /**
     * 请求成功
     *
     * @return ResponseWrapper
     */
    public static ResponseWrapper<String> requestSuccess() {
        return new ResponseWrapper<>(true, ErrorCodeEnum.SUCCESS);
    }

    /**
     * 请求成功 返回数据
     *
     * @param returnData 返回数据
     * @param <T>        响应数据类型
     * @return ResponseWrapper
     */
    public static <T> ResponseWrapper<T> requestSuccessReturnData(T returnData) {
        return new ResponseWrapper<>(true, ErrorCodeEnum.SUCCESS, returnData);
    }

    /**
     * 请求失败 返回错误码枚举类的错误码、默认错误信息
     *
     * @param errorCodeEnum 错误码
     * @return ResponseWrapper
     */
    public static ResponseWrapper<String> requestFailedReturnErrorCodeMessage(ErrorCodeEnum errorCodeEnum) {
        return new ResponseWrapper<>(false, errorCodeEnum);
    }

    /**
     * 请求失败 返回错误码、错误信息
     *
     * @param errorCodeEnum 错误码
     * @param message       错误信息
     * @return ResponseWrapper
     */
    public static ResponseWrapper<String> requestFailedReturnErrorCodeMessage(ErrorCodeEnum errorCodeEnum,
                                                                              String message) {
        return new ResponseWrapper<>(false, errorCodeEnum, message);
    }

    /**
     * 请求失败 返回错误码、错误信息、数据
     *
     * @param errorCodeEnum 错误码
     * @param data          数据
     * @param <T>           响应数据类型
     * @return ResponseWrapper
     */
    public static <T> ResponseWrapper<T> requestFailedReturnErrorCodeMessageData(ErrorCodeEnum errorCodeEnum,
                                                                                 T data) {
        return new ResponseWrapper<>(false, errorCodeEnum, data);
    }

    /**
     * 请求失败 返回错误码、错误信息、数据
     *
     * @param errorCodeEnum 错误码
     * @param message       错误信息
     * @param data          数据
     * @param <T>           响应数据类型
     * @return ResponseWrapper
     */
    public static <T> ResponseWrapper<T> requestFailedReturnErrorCodeMessageData(ErrorCodeEnum errorCodeEnum,
                                                                                 String message,
                                                                                 T data) {
        return new ResponseWrapper<>(false, errorCodeEnum, message, data);
    }

}
