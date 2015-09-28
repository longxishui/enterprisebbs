package com.aspirecn.corpsocial.bundle.addrbook.utils;

import android.database.sqlite.SQLiteDatabase;

import com.aspirecn.corpsocial.common.eventbus.DatabaseHelper;
import com.aspirecn.corpsocial.common.util.LogUtil;

/**
 * 清空联系人数据工具类
 *
 * @author meixuesong
 */
public class TruncateTableUtils {
    private static final String truncate_contact_sql = "delete from addrbook_contact";
    private static final String truncate_dept_sql = "delete from addrbook_dept";
    private static final String truncate_freq_sql = "delete from addrbook_freq_contact";
    private static final String getTruncate_customer_sql = "delete from customer_service";

    public static void truncateContactTables() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(truncate_contact_sql);
            db.execSQL(truncate_dept_sql);
            db.execSQL(truncate_freq_sql);
            db.execSQL(getTruncate_customer_sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e(String.format("清空联系人时失败，错误信息：%s", e.getMessage()), e);
        } finally {
            db.endTransaction();
        }
    }

    public static void truncateContactTables2() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(truncate_freq_sql);
            db.execSQL(getTruncate_customer_sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e(String.format("清空联系人时失败，错误信息：%s", e.getMessage()), e);
        } finally {
            db.endTransaction();
        }
    }

    public static void truncateCustomerTables() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(getTruncate_customer_sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e(String.format("清空联系人时失败，错误信息：%s", e.getMessage()), e);
        } finally {
            db.endTransaction();
        }
    }
}
