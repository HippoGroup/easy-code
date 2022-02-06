package com.hippocp.easy.code.util.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 正确格式，用作提示信息
 *
 * @author ZhouYifan
 * @date 2022/1/11
 */
@Data
@AllArgsConstructor
public class RightFormatMessage implements Serializable {

    /**
     * 整数校验正确格式，用作提示信息
     */
    private String integerValidRightFormat;


    /**
     * 整数校验正确格式，用作提示信息
     */
    private String doubleValidRightFormat;


    /**
     * 整数校验正确格式，用作提示信息
     */
    private String dateValidRightFormat;

}
