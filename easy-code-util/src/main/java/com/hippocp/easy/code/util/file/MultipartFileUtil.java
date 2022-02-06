package com.hippocp.easy.code.util.file;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.hippocp.easy.code.util.excel.ExcelValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * MultipartFile工具类
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
public class MultipartFileUtil {

    /**
     * 日志
     */
    public static final Logger log = LoggerFactory.getLogger(MultipartFileUtil.class);

    /**
     * 空源文件名是空的或空白字符串<br>
     *
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @return 布尔值，true-是，false-否
     */
    public static boolean isBlankOriginalFilename(MultipartFile multipartFile) {
        return !isNotBlankOriginalFilename(multipartFile);
    }

    /**
     * 空源文件名不是空的或空白字符串<br>
     *
     * @param multipartFile 待校验Excel表格 {@link MultipartFile}
     * @return 布尔值，true-是，false-否
     */
    public static boolean isNotBlankOriginalFilename(MultipartFile multipartFile) {
        // 多文件对象为空，不通过校验
        if (multipartFile == null) {
            return false;
        }
        return isNotBlankOriginalFilenameBase(multipartFile);
    }

    /**
     * valid专用方法
     * 空源文件名不是空的或空白字符串<br>
     *
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @return 当multipartFile为null返回true<br>
     * 布尔值，true-是，false-否
     */
    public static boolean isNotBlankOriginalFilenameValid(MultipartFile multipartFile) {
        // 多文件对象为空，通过校验
        if (multipartFile == null) {
            return true;
        }
        return isNotBlankOriginalFilenameBase(multipartFile);
    }

    /**
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @return 布尔值
     * @see MultipartFileUtil#isNotBlankOriginalFilenameValid(MultipartFile)
     */
    public static boolean isBlankOriginalFilenameValid(MultipartFile multipartFile) {
        return !isNotBlankOriginalFilenameValid(multipartFile);
    }

    /**
     * 空源文件名不是空的或空白字符串<br>
     * 不判断参数是否为null
     *
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @return 当multipartFile为null返回true<br>
     * 布尔值，true-是，false-否
     */
    protected static boolean isNotBlankOriginalFilenameBase(MultipartFile multipartFile) {
        // 文件扩展名校验
        String originalFilename = multipartFile.getOriginalFilename();

        // 未读取到文件名，不通过校验
        if (StrUtil.isBlank(originalFilename)) {
            return false;
        }

        // 通过校验
        return true;
    }

    /**
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @return 布尔值
     * @see MultipartFileUtil#isNotBlankOriginalFilenameBase(MultipartFile)
     */
    protected static boolean isBlankOriginalFilenameBase(MultipartFile multipartFile) {
        return !isNotBlankOriginalFilenameBase(multipartFile);
    }


    /**
     * 是否为相同的文件名后缀，即文件扩展名与 suffix 相同
     *
     * @param multipartFile 待校验Excel表格 {@link MultipartFile}
     * @param suffix        数组-文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     */
    public static boolean isEqualsFileSuffix(MultipartFile multipartFile, String[] suffix) {
        // 多文件对象为空，通过校验
        if (MultipartFileUtil.isBlankOriginalFilename(multipartFile)) {
            return false;
        }
        return isEqualsFileSuffixBase(multipartFile, suffix);
    }


    /**
     * @param multipartFile 待校验Excel表格 {@link MultipartFile}
     * @param suffix        数组-文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     * @see MultipartFileUtil#isEqualsFileSuffix(MultipartFile, String[])
     */
    public static boolean isNotEqualsFileSuffix(MultipartFile multipartFile, String[] suffix) {
        return !isEqualsFileSuffix(multipartFile, suffix);
    }


    /**
     * 不判断参数是否为null，也不判断是否能获取到源文件名<br>
     * 是否为相同的文件名后缀，即文件扩展名与 suffix 相同
     *
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @param suffix        数组-文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     */
    protected static boolean isEqualsFileSuffixBase(MultipartFile multipartFile, String[] suffix) {
        // 参照字符串set
        Set<String> referenceSet = ExcelValidateUtil.getReferenceSet(suffix);
        // 取得文件扩展名
        String fileSuffix = getFileSuffix(multipartFile);
        // set中没有当前文件扩展名，则返回ture，代表文件扩展名匹配不成功，已对方法取反
        boolean isNotContains = !referenceSet.contains(fileSuffix);
        if (isNotContains) {
            return false;
        }

        // 通过校验
        return true;
    }


    /**
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @param suffix        数组-文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     * @see MultipartFileUtil#isEqualsFileSuffixBase(MultipartFile, String[])
     */
    protected static boolean isNotEqualsFileSuffixBase(MultipartFile multipartFile, String[] suffix) {
        return !isEqualsFileSuffixBase(multipartFile, suffix);
    }


    /**
     * 是否为相同的文件名后缀，即文件扩展名与 suffix 相同
     *
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @param suffix        文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     */
    public static boolean isEqualsFileSuffix(MultipartFile multipartFile, String suffix) {
        // 多文件对象为空，不通过校验
        if (MultipartFileUtil.isBlankOriginalFilename(multipartFile)) {
            return false;
        }
        return isEqualsFileSuffixBase(multipartFile, suffix);
    }


    /**
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @param suffix        文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     * @see MultipartFileUtil#isEqualsFileSuffix(MultipartFile, String)
     */
    public static boolean isNotEqualsFileSuffix(MultipartFile multipartFile, String suffix) {
        return !isEqualsFileSuffix(multipartFile, suffix);
    }


    /**
     * 是否为相同的文件名后缀，即文件扩展名与 suffix 相同<br>
     * 不判断参数是否为null，也不判断是否能获取到源文件名
     *
     * @param multipartFile 待校验Excel表格 {@link MultipartFile}
     * @param suffix        文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     */
    protected static boolean isEqualsFileSuffixBase(MultipartFile multipartFile, String suffix) {
        // 取得文件扩展名
        String fileSuffix = getFileSuffix(multipartFile);

        // 文件扩展名不等于 suffix 即非法，不通过校验
        boolean isIllegalityFilename = !suffix.equals(fileSuffix);
        if (isIllegalityFilename) {
            return false;
        }
        // 通过校验
        return true;
    }

    /**
     * @param multipartFile 待校验的 {@link MultipartFile}
     * @param suffix        文件名后缀，即文件扩展名
     * @return 布尔值，true-是，false-否
     * @see MultipartFileUtil#isNotEqualsFileSuffixBase(MultipartFile, String)
     */
    protected static boolean isNotEqualsFileSuffixBase(MultipartFile multipartFile, String suffix) {
        return !isEqualsFileSuffixBase(multipartFile, suffix);
    }

    /**
     * 获取文件扩展名
     *
     * @param multipartFile {@link MultipartFile}
     * @return 若多文件对象为空，返回空字符串
     */
    public static String getFileSuffix(MultipartFile multipartFile) {
        // 多文件对象为空，返回空字符串
        if (MultipartFileUtil.isBlankOriginalFilename(multipartFile)) {
            return "";
        }
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    /**
     * 将MultipartFile转换为File临时文件
     *
     * @param multipartFile {@link MultipartFile}
     * @return {@link File}
     */
    public static File multiFileToFile(MultipartFile multipartFile) {
        // Excel表格空源文件名是空的或空白字符串
        if (isBlankOriginalFilename(multipartFile)) {
            return null;
        }
        // 存放临时文件
        File tempFile = null;
        // 文件扩展名
        String originalFilename = multipartFile.getOriginalFilename();

        // 不带中划线的UUID
        String simpleUUID = IdUtil.simpleUUID();
        // 时间格式化
        String format = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        final String strikethrough = "-";
        try {
            tempFile = File.createTempFile("tempFile", strikethrough + originalFilename + strikethrough
                    + format + strikethrough + simpleUUID);
        } catch (IOException e) {
            log.error("创建临时文件时发生错误", e);
        }

        // 未生成临时文件，返回null
        if (tempFile == null) {
            return null;
        }

        try {
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error("将MultipartFile转换为File临时文件是发生错误", e);
        }

        // 返回临时文件
        return tempFile;
    }

    private MultipartFileUtil() {
    }

}
