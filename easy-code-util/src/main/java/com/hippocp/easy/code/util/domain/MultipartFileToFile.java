package com.hippocp.easy.code.util.domain;

import com.hippocp.easy.code.util.file.MultipartFileUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;

/**
 * MultipartFile 转换为 File
 * 包含源文件名
 *
 * @author ZhouYifan
 * @date 2022/1/6
 */
@Data
public class MultipartFileToFile implements Serializable {

    /**
     * 源文件名
     */
    private String originalFilename;

    /**
     * 本地临时文件
     */
    private File file;

    /**
     * 初始化方法
     *
     * @param multipartFile {@link MultipartFile}
     */
    public void init(MultipartFile multipartFile) {
        this.originalFilename = multipartFile.getOriginalFilename();
        this.file = MultipartFileUtil.multiFileToFile(multipartFile);
    }

}
