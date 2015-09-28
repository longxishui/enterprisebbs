package com.aspirecn.corpsocial.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用于日期处理的工具类
 *
 * @author mxs
 */
public final class DateUtils {

    /**
     * 自定义时间格式
     *
     * @param millis
     * @return
     */
    public static String getWeiXinTime(long millis) {
        return getWeiXinTime(new Date(millis));
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return
     */
    public static String getWeiXinTime(Date date) {
        return getWeiXinTime(new Date(), date);
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return
     */
    public static String getWeiXinTimeB(Date date) {
        return getWeiXinTimeB(new Date(), date);
    }

    public static String getWeiXinTimeB(long millis) {
        return getWeiXinTimeB(new Date(millis));
    }


    /**
     * 时间格式
     * <p>后年：后年
     * <p>明年：明年
     * <p>后天：后天 17:12
     * <p>明天：明天 17:12
     * <p>当天：17:12
     * <p>昨天：昨天 17:12
     * <p>前天：前天 17:12
     * <p>今年：8月13日 17:12
     * <p>去年：去年
     * <p>前年：前年
     * <p>其他：2010年
     *
     * @param currentDate
     * @param appointDate
     * @return
     */
    private static String getWeiXinTime(Date currentDate, Date appointDate) {
        if(appointDate==null){
            return "";
        }
        if (appointDate.getTime() < 0) {
            return "";
        }

        long day = getDaysBetween(currentDate, appointDate);
        // 当天
        if (day == 0) {
            return new SimpleDateFormat("HH:mm").format(appointDate);
        }
        //昨天
        if (day == 1) {
            return "昨天 " + new SimpleDateFormat("HH:mm").format(appointDate);
        }
        //前天
        if (day == 2) {
            return "前天 " + new SimpleDateFormat("HH:mm").format(appointDate);
        }
        //前天
        if (day == -1) {
            return "明天 " + new SimpleDateFormat("HH:mm").format(appointDate);
        }
        //前天
        if (day == -2) {
            return "后天 " + new SimpleDateFormat("HH:mm").format(appointDate);
        }

        int year = getYearBetween(currentDate, appointDate);
        //今年
        if (year == 0) {
            return new SimpleDateFormat("MM月dd日 HH:mm").format(appointDate);
        }
        //去年
        if (year == 1) {
            return "去年";
        }
        //前年
        if (year == 2) {
            return "前年";
        }
        //去年
        if (year == -1) {
            return "明年";
        }
        //前年
        if (year == -2) {
            return "后年";
        }

        return new SimpleDateFormat("yyyy年").format(appointDate);
    }

    /**
     * 时间格式
     * <p>后年：后年 07月12日
     * <p>明年：明年 07月12日
     * <p>当年：07月12日
     * <p>去年：去年 07月12日
     * <p>前年：前年 07月12日
     * <p>其他：2010年07月12日
     *
     * @param currentDate
     * @param appointDate
     * @return
     */
    private static String getWeiXinTimeB(Date currentDate, Date appointDate) {
        if (appointDate.getTime() < 0) {
            return "";
        }

        int year = getYearBetween(currentDate, appointDate);
        //今年
        if (year == 0) {
            long day = getDaysBetween(currentDate, appointDate);
            // 当天
            if (day == 0) {
                long hour = getHoursBetween(currentDate, appointDate);
                if (hour == 0) {
                    return "刚刚";
                } else
                    return hour + "小时前";
            }
            //昨天
            if (day == 1) {
                return "昨天 ";
            }
            //前天
            if (day == 2) {
                return "前天 ";
            }
            //前天
            if (day == -1) {
                return "明天 ";
            }
            //前天
            if (day == -2) {
                return "后天 ";
            }

            return new SimpleDateFormat("MM月dd日").format(appointDate);
        }
        //去年
        if (year == 1) {
            return "去年" + new SimpleDateFormat("MM月dd日").format(appointDate);
        }
        //前年
        if (year == 2) {
            return "前年" + new SimpleDateFormat("MM月dd日").format(appointDate);
        }
        //去年
        if (year == -1) {
            return "明年" + new SimpleDateFormat("MM月dd日").format(appointDate);
        }
        //前年
        if (year == -2) {
            return "后年" + new SimpleDateFormat("MM月dd日").format(appointDate);
        }

        return new SimpleDateFormat("yyyy年MM月dd日").format(appointDate);
    }

    /**
     * 返回日期相差天数
     *
     * @param currentDate
     * @param appointDate
     * @return
     */
    public static long getDaysBetween(Date currentDate, Date appointDate) {
        Calendar current = Calendar.getInstance();
        current.setTime(currentDate);
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        Calendar appoint = Calendar.getInstance();
        appoint.setTime(appointDate);
        appoint.set(Calendar.HOUR_OF_DAY, 0);
        appoint.set(Calendar.MINUTE, 0);
        appoint.set(Calendar.SECOND, 0);
        appoint.set(Calendar.MILLISECOND, 0);

        long time = current.getTime().getTime() - appoint.getTime().getTime();

        return time / (1000 * 60 * 60 * 24);
    }

    public static long getHoursBetween(Date currentDate, Date appointDate) {
        Calendar current = Calendar.getInstance();
        current.setTime(currentDate);

        Calendar appoint = Calendar.getInstance();
        appoint.setTime(appointDate);

        long time = current.getTime().getTime() - appoint.getTime().getTime();

        return time / (1000 * 60 * 60);
    }

    /**
     * 返回日期相差年数
     *
     * @param currentDate
     * @param appointDate
     * @return
     */
    public static int getYearBetween(Date currentDate, Date appointDate) {
        Calendar current = Calendar.getInstance();
        current.setTime(currentDate);
        int currentYear = current.get(Calendar.YEAR);

        Calendar appoint = Calendar.getInstance();
        appoint.setTime(appointDate);
        int appointYear = appoint.get(Calendar.YEAR);

        return currentYear - appointYear;
    }

    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return getDaysBetween(date1, date1) == 0 ? true : false;
    }

    /**
     * 判断是否为上午
     *
     * @param date
     */
    public static boolean isAM(Date date) {
        DateFormat df = new SimpleDateFormat("HH");

        return Integer.parseInt(df.format(date)) <= 12;
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return 2014-03-12 18:28
     */
    public static String format_yMdHm(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(date);
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return 2014-03-12
     */
    public static String format_yMd(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return 18:28
     */
    public static String format_Hm(Date date) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return 18:28:58
     */
    public static String format_Hms(Date date) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(date);
    }

    /**
     * 自定义时间格式
     *
     * @param date
     * @return 2014-12-26 周五 10:18
     */
    public static String format_yMdWHm(Date date) {
        String[] mDayOfWeekNames = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd / HH:mm");
        String strDate = format.format(date);
        String[] strDates = strDate.split("/");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String strDayOfWeek = mDayOfWeekNames[dayOfWeek - 1];

        return strDates[0] + strDayOfWeek + strDates[1];
    }

    /**
     * @param s yyyy-MM-dd HH:mm
     * @return
     */
    public static Date parse(String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param s yyyy-MM-dd
     * @return
     */
    public static Date parseShort(String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得当天0点时间
     */
    public static long getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    /**
     * 获得当天24点时间
     */
    public static long getTimesNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

}
