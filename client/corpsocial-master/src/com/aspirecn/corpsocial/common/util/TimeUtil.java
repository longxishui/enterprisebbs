package com.aspirecn.corpsocial.common.util;

import java.util.Date;

public class TimeUtil {

    public static String convert(long l) {
        if (l == 0l) {
            return null;
        }
        String result = null;
        int seconds = (int) ((System.currentTimeMillis() - l) / 1000);
        if (seconds < 120) {
            result = "1分钟前更新";
        } else if (seconds < 3600) {
            result = seconds / 60 + "分钟前更新";
        } else if (seconds < 3600 * 24) {
            result = seconds / 3600 + "小时前更新";
        } else {
            result = DateUtils.getWeiXinTime(new Date(l)) + "更新";
        }
        return result;
    }
}
