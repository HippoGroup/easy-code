package com.hippocp.easy.code.util.excel;

import com.hippocp.easy.code.util.domain.CustomValidatorResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 自定义校验器回调接口<br>
 * 如需自定义校验器，请实现该接口中的方法<br>
 * 校验器将主动按入参列表顺序，调用自定义校验器进行校验<br>
 * {@link ExcelValidateUtil#cellValid(MultipartFile, Map, Class, ExcelValidateOptions)}
 *
 * @author ZhouYifan
 * @date 2021/1/8
 */
@FunctionalInterface
public interface CustomValidator<T> {

    /**
     * 自定义校验回调方法
     *
     * @param beanList JavaBean 需要校验的对象列表<br>
     *                 例如：Excel表格数据对象列表<br>
     *                 注意：请不要在方法内修改参数 beanList 的值或其中元素值，因为这将直接修改下一个自定义校验器收到的参数 beanList
     *                 还将直接修改方法 {@link ExcelValidateUtil}
     *                 返回的Excel表格数据，除非你不想要返回的Excel表格数据
     * @return {@link CustomValidator}自定义校验器结果数据
     */
    CustomValidatorResult<T> isValid(List<T> beanList);

}
