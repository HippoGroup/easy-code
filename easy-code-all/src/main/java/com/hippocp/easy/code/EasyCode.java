package com.hippocp.easy.code;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.ConsoleTable;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

/**
 * @author ZhouYifan
 * @date 2022/2/3
 */
public class EasyCode {

    public static final String AUTHOR = "ZhouYifan";

    /**
     * 显示 Easy Code 所有的工具类
     *
     * @return 工具类名集合
     * @since 0.3.0
     */
    public static Set<Class<?>> getAllUtils() {
        return ClassUtil.scanPackage("com.hippocp.easy.code",
                (clazz) -> (!clazz.isInterface()) && StrUtil.endWith(clazz.getSimpleName(), "Util"));
    }

    /**
     * 控制台打印 Easy Code 所有的工具类 简单版
     *
     * @since 0.3.0
     */
    public static void printAllUtilSimple() {
        final Set<Class<?>> allUtils = getAllUtils();

        if (CollUtil.isEmpty(allUtils)) {
            System.out.println("未找到任何工具类");
            return;
        }

        System.out.println("找到如下工具类");
        for (Class<?> aClass : allUtils) {
            System.out.println(aClass);
        }

    }

    /**
     * 控制台打印 Easy Code 所有工具类<br>
     * 适用于依赖 HuTool5.4.4 以上版本的项目使用
     *
     * @since 0.3.0
     */
    public static void printAllUtils() {
        final Set<Class<?>> allUtils = getAllUtils();
        final ConsoleTable consoleTable = ConsoleTable.create().addHeader("工具类名", "所在包");
        for (Class<?> clazz : allUtils) {
            consoleTable.addBody(clazz.getSimpleName(), clazz.getPackage().getName());
        }
        consoleTable.print();
    }

    private EasyCode() {
    }


}
