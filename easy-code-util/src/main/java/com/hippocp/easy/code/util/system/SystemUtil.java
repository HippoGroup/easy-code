package com.hippocp.easy.code.util.system;

import com.hippocp.easy.code.util.string.StringUtil;

/**
 * 系统工具类
 *
 * @author ZhouYifan
 * @date 2021/11/30
 */
public class SystemUtil {

    /**
     * 判断当前系统是否为Windows系统
     *
     * @return true-是 false-否
     */
    public static boolean isWindows() {
        // 获取当前系统名称
        String osName = System.getProperty("os.name");
        // 是否Windows系统
        boolean isWindows = osName.toLowerCase().contains("windows");
        return isWindows;
    }

    /**
     * @see StringUtil#presentOsLineBreak()
     */
    public static String presentOsLineBreak() {
        return StringUtil.presentOsLineBreak();
    }

    private SystemUtil() {
    }

}
