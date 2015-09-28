/**
 * @(#) PropertyInfo.java Created on 2013-11-18
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.config;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The class <code>PropertyInfo</code>
 *
 * @author likai
 * @version 1.0
 */
public class PropertyInfo {

    private static PropertyInfo instance;
    private final static String SYS_PROP_NAME = "system.properties";
    private static Properties mProperties = null;

    private PropertyInfo() {

    }

    public static PropertyInfo getInstance() {
        if (instance == null) {
            instance = new PropertyInfo();
        }
        return instance;
    }

    static {
        mProperties = new Properties();
        try {
            InputStream inputStream = AspirecnCorpSocial.getContext().getAssets().open(SYS_PROP_NAME);
            mProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 获取配置文件的值
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return mProperties.getProperty(key);
    }

    /**
     * 获取配置文件的值
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        String value = mProperties.getProperty(key);
        if (value != null && value.length() > 0) {
            return Integer.valueOf(value);
        }
        return 0;
    }

    /**
     * 获取配置文件的值
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        String value = mProperties.getProperty(key);
        if (value != null && value.length() > 0) {
            return Long.valueOf(value);
        }
        return 0;
    }

    /**
     * 获取配置文件的值
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        String value = mProperties.getProperty(key);
        if (value != null && value.length() > 0) {
            return Boolean.valueOf(value);
        }
        return false;
    }
}
