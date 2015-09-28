package com.aspirecn.corpsocial.common.eventbus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.trinea.android.common.util.StringUtils;

/**
 * 数据库升级工具，需要配合upgrade.sql使用<br/>
 * 用法：DBUpgradeUtils.INSTANCE.checkDBVersionAndUpgrade(context, db, fromVersion, toVersion);
 *
 * @author meixuesong
 */
public enum DBUpgradeUtils {
    INSTANCE;

    public void checkDBVersionAndUpgrade(Context context, SQLiteDatabase db, int fromVersion, int toVersion) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("upgrade.sql");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line;
                int version = -1;
                while ((line = in.readLine()) != null) {
                    if (isNotBlank(line) && isNotComment(line)) {
                        line = line.trim();

                        if (isVersion(line)) {
                            version = getVersion(line);
                            continue;
                        }

                        if (isVersionMatched(version, fromVersion, toVersion)) {
                            LogUtil.i(String.format("版本：%d, 升级语句：%s", version, line));
                            db.execSQL(line);
                        }
                    }
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            LogUtil.e("数据库升级IO失败：" + e.getMessage(), e);
        } catch (Exception e) {
            LogUtil.e("数据库升级失败：" + e.getMessage(), e);
        }
    }

    private boolean isNotComment(String str) {
        return str != null && (!str.trim().startsWith("#"));
    }

    private boolean isVersion(String str) {
        return str != null && str.toLowerCase().startsWith("version");
    }

    private int getVersion(String str) {
        str = str.toLowerCase();
        str = str.replace("：", ":");
        String[] list = str.split(":");
        if (list.length == 2) {
            try {
                return Integer.valueOf(list[1].trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return -1;
    }

    private boolean isVersionMatched(int version, int from, int to) {
        return (version > from && version <= to);
    }

    private boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }
}
