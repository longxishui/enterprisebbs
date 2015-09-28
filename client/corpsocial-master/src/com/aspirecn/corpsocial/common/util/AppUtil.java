package com.aspirecn.corpsocial.common.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;

import java.util.List;

public class AppUtil {

    /**
     * 获取应用的版本信息
     *
     * @return
     */
    public static Version getAppVersionInfo() {
        Context context = AspirecnCorpSocial.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            Version info = new Version();
            info.code = pi.versionCode;
            info.name = pi.versionName;

            return info;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground() {
        Context context = AspirecnCorpSocial.getContext();
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否亮屏
     *
     * @return
     */
    public static boolean isScreenOn() {
        PowerManager pm = (PowerManager) AspirecnCorpSocial.getContext()
                .getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 版本信息
     *
     * @author meixuesong
     */
    public static class Version {
        public int code;
        public String name;
    }

}
