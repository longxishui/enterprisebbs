package com.aspirecn.corpsocial.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {

    private static final long DAY_MILLS = 1000 * 60 * 60 * 24;

    private static final long WEEK_MILLS = DAY_MILLS * 7;

    /**
     * 设置闹钟
     *
     * @param context
     * @param alarmId
     * @param intent
     * @param triggerAtMills 为0将立即发起alarm
     */
    public static void setupAlarm(Context context, int alarmId, Intent intent, long triggerAtMills) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMills, pi);
    }

    /**
     * 设置重复响铃闹钟
     *
     * @param context
     * @param alarmId
     * @param intent
     * @param triggerAtMills
     * @param intervalMills
     */
    public static void setupRepeatedAlarm(Context context, int alarmId, Intent intent, long triggerAtMills, long intervalMills) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMills, intervalMills, pi);
    }

    /**
     * 设置重复响铃闹钟
     *
     * @param context
     * @param alarmId
     * @param intent
     * @param triggerAtMills
     * @param intervalMills
     */
    public static void setInexactRepeating(Context context, int alarmId, Intent intent, long triggerAtMills, long intervalMills) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMills, intervalMills, pi);
    }

    /**
     * 取消闹钟
     *
     * @param context
     * @param alarmId 闹钟唯一标识
     * @param intent
     */
    public static void cancelAlarm(Context context, int alarmId, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }

    /**
     * 获取当前开始的下一次提醒时间（每天提醒方式）
     *
     * @param startMills 首次提醒时间
     * @return
     */
    public static long getNextDateForDay(long startMills) {
        long currentMills = System.currentTimeMillis();
        long nextMills = startMills;
        while (currentMills > nextMills) {
            nextMills += DAY_MILLS;
        }
        return nextMills;
    }

    /**
     * 获取当前开始的下一次提醒时间（每星期提醒方式）
     *
     * @param startMills 首次提醒时间
     * @return
     */
    public static long getNextDateForWeek(long startMills) {
        long currentMills = System.currentTimeMillis();
        long nextMills = startMills;
        while (currentMills > nextMills) {
            nextMills += WEEK_MILLS;
        }
        return nextMills;
    }

}
