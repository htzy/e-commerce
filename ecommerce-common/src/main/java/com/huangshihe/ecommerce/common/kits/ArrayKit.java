package com.huangshihe.ecommerce.common.kits;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    public static <T> T[] mergeWithNoSame(T[] arr1, T[] arr2) {
        if (arr1 == null) {
            return clone(arr2);
        } else if (arr2 == null) {
            return clone(arr1);
        }
        // 去重
        Set<T> set = new HashSet<T>();
        Collections.addAll(set, arr1);
        Collections.addAll(set, arr2);
//        return (T[]) set.toArray();
        T[] result = (T[]) Array.newInstance(arr1.getClass().getComponentType(), set.size());
        int i = 0;
        for (T item : set) {
            result[i++] = item;
        }
        return result;
    }

//    public static Object[] mergeWithNoSame(Object[] arr1, Object[] arr2) {
//        if (arr1 == null) {
//            return clone(arr2);
//        } else if (arr2 == null) {
//            return clone(arr1);
//        }
//        ArrayList<Object> list = new ArrayList<Object>(arr1.length + arr2.length);
//        HashSet<Object> set = new HashSet<>(list);
//        return set.toArray();
//    }


    public static Object[] addWithNoSame(Object[] arr, Object item) {
        if (arr == null) {

//            return toArray(item);
        } else if (item == null) {
            return clone(arr);
        }
//        Set<T> set

//        boolean same = false;
//        for (T i : arr) {
//            if (i == item) {
//                same = true;
//                break;
//            }
//        }
//
//        // 如果有相同的
//        if (same) {
//            return clone(arr);
//        } else {
//            T[] result = Arrays.copyOf(arr, arr.length + 1);
//            result[arr.length] = item;
//            return result;
//        }
        return null;
    }

    public static <T> T[] toArray(T item) {
        if (item == null) {
            return null;
        }
        T[] tmp = (T[]) new Object[]{item};

        return (T[]) Array.newInstance(tmp.getClass().getComponentType(), 1);
//
//        ArrayList<T> list = new ArrayList<T>();
//        list.add(item);
//        list.toArray();
////        return (T[]) Array.newInstance(T[].class.getComponentType(), 1);
//        return (T[]) new Object[]{item};
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
}
