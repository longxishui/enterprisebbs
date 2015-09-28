package com.aspirecn.corpsocial.common.util;

public class TextUtil {
    public static String removeAllSpace(String str) {
        String tmpstr = str.replace(" ", "");
        return tmpstr;
    }

    /**
     * 截取某一段字符串
     *
     * @param str   原始字符串
     * @param start 起始字符位置
     * @param end   结束字符位置
     * @param right start右移
     * @return 截取后的字符串
     */
    public static String textInterception(String str, String start, String end,
                                          int right) {
        String result = null;
        if (start != null) {
            int a = str.indexOf(start);
            if (a != -1 && end != null) {
                int b = str.lastIndexOf(end);
                if (b != -1) {
                    result = str.substring(a + right, b);
                }
            } else if (a != -1 && end == null) {
                result = str.substring(a);

            }
        } else {
            int b = str.lastIndexOf(end);
            if (b != -1) {
                result = str.substring(0, b);
            }
        }
        return result;
    }

}
