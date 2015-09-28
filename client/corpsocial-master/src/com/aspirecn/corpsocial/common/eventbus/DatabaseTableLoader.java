/**
 * @(#) EventListenerCache.java Created on 2013-11-13
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import android.content.Context;

import com.aspirecn.corpsocial.common.eventbus.ClassScanUtil.ClassFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * The class <code>EventListenerCache</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class DatabaseTableLoader {

    private static DatabaseTableLoader container = null;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private DatabaseTableLoader(Context context, String databaseName, int databaseVer, List<Class> scanPackage) {
        ClassFilter filter = new DatabaseTableClassFilter();

        List<Class<? extends Object>> databaseTables = new ArrayList<Class<? extends Object>>();
        for (Class<? extends Object> clazz : scanPackage) {
            if (filter.accept(clazz)) {
                databaseTables.add(clazz);
            }
        }
        DatabaseHelper.getInstance(context, databaseName, databaseVer, databaseTables);

    }

    public static DatabaseTableLoader getInstance(Context context, String databaseName, int databaseVer, @SuppressWarnings("rawtypes") List<Class> scanPackage) {
        if (container == null) {
            container = new DatabaseTableLoader(context, databaseName, databaseVer, scanPackage);
        }
        return container;
    }

}
