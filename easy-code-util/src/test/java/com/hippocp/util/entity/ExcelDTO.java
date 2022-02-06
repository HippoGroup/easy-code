package com.hippocp.util.entity;

import com.hippocp.easy.code.util.string.StringUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Excel表格测试实体类
 */
@Data
public class ExcelDTO implements Serializable {

    /**
     * 未加校验注解不进行校验
     */
    private Long id;

    /**
     * 校验器来加上Excel表格行信息，附带一个开关，可加可不加
     */
    @NotNull(message = "数据编号" + StringUtil.NOT_BLANK_MSG_TEMPLATE)
    private Long dataCode;

    @NotBlank(message = "姓名" + StringUtil.NOT_BLANK_MSG_TEMPLATE)
    private String name;

    @NotNull(message = "年龄" + StringUtil.NOT_BLANK_MSG_TEMPLATE)
    private Short age;

}
