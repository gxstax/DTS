package com.ant.dts.util.collections;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * List工具类
 * </p>
 *
 * @author Ant
 * @since 2021/1/7 11:24 上午
 */
public class ListUtil {

    /**
     * <p>
     * List 转换为 Map
     * </p>
     *
     * @param targetObjList
     * @param field
     * @return Map<field, List<T>>
     */
    public static <T, F> Map<F, T> extractMap(List<T> targetObjList, Function<T, F> field) {
        if (CollectionUtils.isEmpty(targetObjList)) {
            return new HashMap<>();
        }
        return targetObjList.stream().collect(Collectors.toMap(field, obj -> obj, (key1, key2) -> key2));
    }

    /**
     * <p>
     * List 转换为 Map
     * </p>
     *
     * @param targetObjList
     * @param keyField map Key
     * @param valueField map Value
     * @return Map<keyField, valueField>
     */
    public static <T, F> Map<F, F> extractMap(List<T> targetObjList, Function<T, F> keyField, Function<T, F> valueField) {
        if (CollectionUtils.isEmpty(targetObjList)) {
            return new HashMap<>();
        }
        return targetObjList.stream().collect(Collectors.toMap(keyField, valueField, (key1, key2) -> key1));
    }


}
