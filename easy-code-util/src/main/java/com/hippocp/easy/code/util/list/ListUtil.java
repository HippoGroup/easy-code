package com.hippocp.easy.code.util.list;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link List} 列表工具类
 *
 * @author ZhouYifan
 * @date 2021/9/15
 */
public class ListUtil {

    /**
     * 判断Long包装类list与Long包装类数组，进行自然排序后，内容是否相同
     *
     * @param longList  Long包装类list
     * @param longArray Long包装类数组
     * @return 布尔值，内容相同返回true，内容不同返回false
     */
    public static boolean isContentEquals(List<Long> longList, Long[] longArray) {
        // 数组转集合
        List<Long> asList = Arrays.asList(longArray);
        // 排序2个集合
        Collections.sort(asList);
        Collections.sort(longList);
        String asListStr = asList.toString();
        String longListStr = longList.toString();
        return asListStr.equals(longListStr);
    }

    /**
     * 按参数groupCount分割列表，若参数groupCount为null或者为0，
     * 则使用默认值1000进行分割
     *
     * @param needSplitList 需要分割的列表
     * @param groupCount    按一组多少数量分割
     * @param <E>           列表中存储的元素类型
     * @return 列表嵌套列表
     */
    public static <E> List<List<E>> splitList(List<E> needSplitList, Integer groupCount) {
        if (CollUtil.isEmpty(needSplitList)) {
            return new ArrayList<>();
        }
        final int maxSend;
        if (groupCount == null || groupCount == 0) {
            // 如果为空，按每1000个一组分割
            maxSend = 1000;
        } else {
            // 否则正常使用参数
            maxSend = groupCount;
        }
        // 计算分割次数
        int limit = (needSplitList.size() + maxSend - 1) / maxSend;
        // 存放分段元素列表
        List<List<E>> sendList = new ArrayList<>();
        // 将元素列表分段存入
        Stream.iterate(0, n -> n + 1)
                .limit(limit)
                .forEach(
                        i -> sendList.add(needSplitList.stream().skip((long) i * maxSend).limit(maxSend).collect(Collectors.toList()))
                );

        return sendList;

    }

    private ListUtil() {
    }

}
