/**
 * @(#) DatabaseHelper.java Created on 2013-11-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhuo_a
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static DatabaseHelper instance;
    private Context context;

    private List<Class<? extends Object>> databaseTables;

    private DatabaseHelper(Context context, String dbName, int dbVer, List<Class<? extends Object>> databaseTables) {
        super(context, dbName, null, dbVer);
        this.context = context;
        this.databaseTables = databaseTables;
    }

    public static DatabaseHelper getInstance(Context context, String databaseName, int databaseVer, List<Class<? extends Object>> databaseTables) {
        if (instance == null) {
            instance = new DatabaseHelper(context, databaseName, databaseVer, databaseTables);
        }
        return instance;
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    /**
     * 创建实例
     */
    private static DatabaseHelper init() {
        String eventBusScanPath = PropertyInfo.getInstance().getString("eventBusScanPath");
        String databaseName = PropertyInfo.getInstance().getString("databaseName");
        int databaseVer = PropertyInfo.getInstance().getInt("databaseVer");

        ClassScanUtil.ClassFilter filter = new DatabaseTableClassFilter();
        List<Class> scanPackage = ClassScanUtil.scanPackage(AspirecnCorpSocial.getContext(), eventBusScanPath, filter);
        List<Class<? extends Object>> databaseTables = new ArrayList<Class<? extends Object>>();
        for (Class<? extends Object> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                databaseTables.add(clazz);
            }
        }
        return new DatabaseHelper(AspirecnCorpSocial.getContext(), databaseName, databaseVer, databaseTables);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        try {
            for (Class<? extends Object> clazz : databaseTables) {
                TableUtils.createTableIfNotExists(source, clazz);
            }
        } catch (SQLException e) {
            LogUtil.e("创建数据库失败", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        DBUpgradeUtils.INSTANCE.checkDBVersionAndUpgrade(context, db, oldVersion, newVersion);//FIXME 无法执行中间过程
        onCreate(db, connectionSource);// 用于执行创建新增的表
    }
}
