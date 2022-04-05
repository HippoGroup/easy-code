package com.hippocp.util.string;

import com.hippocp.easy.code.util.string.StringUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ZhouYifan
 * @date 2022/1/16
 */
public class StringUtilTest {

    @Test
    public void trimBlankCharacterTest() {
        String s = StringUtil.trimBlankCharacter("    \r宝贝\n\r，\n爸\t爸\r爱你\n\t    ");
        System.out.println("去除空白字符后的字符串：" + s);
        String e = "宝贝，爸爸爱你";
        Assert.assertEquals("预期相同代表去除空白字符成功", e, s);
    }

    @Test
    public void getOutsetContinuousZeroTest() {
        String s = StringUtil.getOutsetContinuousZero("00101");
        Assert.assertEquals("00", s);
    }

}
