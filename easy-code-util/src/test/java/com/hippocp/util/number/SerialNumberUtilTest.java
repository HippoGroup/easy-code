package com.hippocp.util.number;

import com.hippocp.easy.code.util.number.SerialNumberUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ZhouYifan
 * @date 2022/4/5
 */
public class SerialNumberUtilTest {

    @Test
    public void getTimeStampAndRandomNumberStrTest() {
        String str = SerialNumberUtil.getTimeStampAndRandomStr(6);
        int length = str.length();
        Assert.assertEquals(23, length);
    }

}
