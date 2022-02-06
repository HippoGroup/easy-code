package com.hippocp.util.file;

import com.hippocp.easy.code.util.file.MultipartFileUtil;
import com.hippocp.util.excel.ExcelValidateUtilTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * @author ZhouYifan
 * @date 2022/1/8
 */
public class MultipartFileUtilTest {

    public static MultipartFile initMultipartFile() {
        URL url = ExcelValidateUtilTest.class.getClassLoader().getResource("testTemplate.xlsx");
        File file = new File(url.getFile());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    "application/octet-stream", fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    @Test
    public void multipartFileUtilTest() {

        MultipartFile multipartFile = initMultipartFile();

        boolean isEqualsFileSuffix = MultipartFileUtil.isEqualsFileSuffix(multipartFile,
                new String[]{".xlsx", ".xls"});
        Assert.assertTrue("应是相同文件扩展名，预期为 ture，现在却提示false", isEqualsFileSuffix);

        boolean isNotEqualsFileSuffix = MultipartFileUtil.isNotEqualsFileSuffix(multipartFile,
                new String[]{".xml", ".html"});
        Assert.assertTrue("应是不相同文件扩展名，预期为 true，现在却提示false", isNotEqualsFileSuffix);
    }

}
