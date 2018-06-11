package com.huangshihe.ecommerce.common.kits;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数组工具类.
 * <p>
 * Create Date: 2018-04-03 19:53
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class ArrayKit {

    /**
     * 合并数组，并去重.
     * eg: ArrayKit.mergeWithNoSame(result, new Interceptor[]{interceptor});
     * eg: ArrayKit.mergeWithNoSame(arr1, arr2);
     *
     * @param arr1 arr1
     * @param arr2 arr2
     * @param <T>  类类型
     * @return 合并后的数组
     */
    public static <T> T[] mergeWithNoSame(T[] arr1, T[] arr2) {
        if (arr1 == null) {
            return clone(arr2);
        } else if (arr2 == null) {
            return clone(arr1);
        }
        // 去重
        Set<T> set = new HashSet<T>(arr1.length + arr2.length);
        Collections.addAll(set, arr1);
        Collections.addAll(set, arr2);
        // 这里不能强转 // return (T[]) set.toArray();
        return (T[]) Arrays.copyOf(set.toArray(), set.size(), arr1.getClass());
//        T[] result = (T[]) Array.newInstance(arr1.getClass().getComponentType(), set.size());
//        System.arraycopy(set.toArray(), 0, result, 0, set.size());
    }

    /**
     * 追加元素到数组，并去重.
     * eg: ArrayKit.addWithNoSame(result, interceptor, Interceptor[].class);
     * eg: ArrayKit.addWithNoSame(result, interceptor, result.getClass()); // 但这种方法一定要确保result不为null
     *
     * @param arr  数组
     * @param item 元素
     * @param type 数组类型
     * @param <T>  类类型
     * @return 数组
     */
    public static <T> T[] addWithNoSame(T[] arr, T item, Class<? extends T[]> type) {
        if (arr == null) {
            return toArray(item, type);
        } else if (item == null) {
            return clone(arr);
        }
        // 去重
        Set<T> set = new HashSet<T>(arr.length + 1);
        Collections.addAll(set, arr);
        set.add(item);
        return Arrays.copyOf(set.toArray(), set.size(), type);
//        T[] result = (T[]) Array.newInstance(arr.getClass().getComponentType(), set.size());
//        System.arraycopy(set.toArray(), 0, result, 0, set.size());
    }


    /**
     * 元素转为数组，当元素为null，则返回长度为0的数组.
     *
     * @param item 元素
     * @param type 元素数组类型
     * @param <T>  类类型
     * @return 数组
     */
    public static <T> T[] toArray(T item, Class<? extends T[]> type) {
        if (item != null) {
            T[] arr = (T[]) Array.newInstance(type.getComponentType(), 1);
            arr[0] = item;
            return arr;
        } else {
            return (T[]) Array.newInstance(type.getComponentType(), 0);
        }
    }

    /**
     * 数组是否不为空.
     *
     * @param arr 数组
     * @return 空数组返回false，否则返回true
     */
    public static boolean isNotEmpty(Object[] arr) {
        return !isEmpty(arr);
    }

    /**
     * 数组是否为空.
     *
     * @param arr 数组
     * @return 空数组返回true，否则返回false
     */
    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    /**
     * 复制数组.
     *
     * @param array 待复制数组.
     * @param <T>   数组类型
     * @return 复制结果
     */
    public static <T> T[] clone(T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * 将list转为String.
     *
     * @param list list
     * @return string
     */
    public static String toString(List<Object> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return Arrays.toString(list.toArray());
    }
}
