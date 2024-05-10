package com.example.util;

import java.util.function.Function;
import java.util.List;

public class StringUtils {

    public static <T> int findLongestStringLength(List<T> list, Function<T, String> function) {
        int maxLength = 0;
        for (T item : list) {
            String str = function.apply(item);
            int length = str.length();
            if (length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }
}

