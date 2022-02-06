package com.hippocp.easy.code.util.domain;

/**
 * Excel表格读取器类型枚举类
 *
 * @author ZhouYifan
 * @date 2022/1/12
 */
public enum ExcelReaderTypeEnum {

    /**
     * 数据读取类型器 1，带校验
     * 读取数据时就开始校验，将进行的校验如下：
     * 字符串 -》数字（整数） 使用默认整数校验正确格式提示消息 "必须填写整数"<br>
     * 字符串 -》数字（浮点数）使用小数校验正确格式提示消息 “必须填写小数”<br>
     * 字符串 -》日期 使用日期校验正确格式提示消息<br>
     * “正确示例（时间24小时制）示例一：2021/9/5 14:30:00 示例二：2021/09/05 14:30:00 示例三：2021年09月05日 14时30分00秒”<br>
     */
    VALID(1, "带校验器，在数据读取过程中附加校验"),

    /**
     * 数据读取类型器 2，不带校验
     */
    NOTVALID(2, "不带校验器，不在数据读取过程中附加校验");

    /**
     * 编号
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    ExcelReaderTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExcelReaderTypeEnum{");
        sb.append("code=").append(code);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
