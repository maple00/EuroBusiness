package com.rainwood.eurobusiness.utils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2019/12/13 10:06
 * @Desc: List管理工具类
 */
public final class ListUtils {

    public static int getSize(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
        return (sourceList != null && !sourceList.contains(entry)) && sourceList.add(entry);
    }
}
