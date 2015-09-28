package com.aspirecn.corpsocial.common.util;

/**
 * Created by yinghuihong on 15/6/1.
 */
public class ColorUtil {

    /**
     * 顏色值转换
     *
     * @param color e.g. #ffffff
     * @return ffffffff
     */
    public static int convert(String color) {
        String t = color.replace("#", color.length() < 8 ? "ff" : "");
        return (int) Long.parseLong(t, 16);
    }
}
