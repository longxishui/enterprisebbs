/**
 *
 */
package com.aspirecn.corpsocial.common.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class <code>StringUtils</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public abstract class StringUtils {

    /**
     * Checks if a String is empty ("") or null. <br/>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Checks if a String is not empty ("") and not null. <br/>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * Format str to unpeek string. <br/>
     * eg: HelloWorld --> hello_world.
     *
     * @param str
     * @return
     */
    public static String unpeak(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer pstr = new StringBuffer(strLen);
        char[] chars = str.toCharArray();
        // jump the first char.
        pstr.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < chars.length; i++) {
            char originalChar = chars[i];
            pstr.append(Character.isLowerCase(originalChar) ? originalChar
                    : "_" + Character.toLowerCase(chars[i]));
        }
        return pstr.toString();
    }

    public static String getString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否为手机号码
     *
     * @param number
     * @return
     */
    public static boolean isMobileNumber(String mobileNumber) {
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6-8]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    public static String listToString(List<String> list) {
        String str = "";
        for (String item : list) {
            if (str != "")
                str = str + "," + item;
            else
                str = item;
        }
        return str;
    }
    public static boolean isBlank(String str){
        if(str==null||str.equals("")){
            return true;
        }else{
            return false;
        }
    }
}
