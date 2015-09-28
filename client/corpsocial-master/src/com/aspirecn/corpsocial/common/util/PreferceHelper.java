/**
 * @(#) PreferceHelper.java Created on 2013年11月1日
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The class <code>PreferceHelper</code> 用于存储数据的帮助类
 *
 * @author danel
 * @version 1.0
 */
public class PreferceHelper {

    SharedPreferences sp;

    SharedPreferences.Editor editor;

    Context context;

    String XMLNAME;

    /* 构造方法 */
    public PreferceHelper(Context c, String name) {
        context = c;
        XMLNAME = name;
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /* 添加数据 */
    public void putValue(String key, String value) {

        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /* 获取数据 */
    public String getValue(String value) {
        return sp.getString(value, null);
    }

    /* 删除数据 */
    public void RemoveItem(String key) {
        SharedPreferences s = context.getSharedPreferences(XMLNAME, 0);
        s.edit().remove(key).commit();
    }

    /* 清空所有数据 */
    public void ClearData() {
        editor.clear().commit();
    }
}
