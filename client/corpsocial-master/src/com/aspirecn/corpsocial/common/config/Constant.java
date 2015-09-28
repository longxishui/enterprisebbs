/**
 * @(#) AspWeiXinConstant.java Created on 2013-10-30
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.config;

import android.os.Environment;

import java.io.File;

/**
 * @author lizhuo_a
 */
public interface Constant {

    int CORNER_RADIUS_PIXELS = 20;
    /**
     * shareprefrence的名字
     */
    String SHAREDPREFE_NAME_OAWEIXIN = "aspirencorpsocial";
    String SHAREDPREFE_NAME_OAWEIXIN_APPCENTER = "appcenter";

    /**
     * 数据库名称
     */
    String DB_NAME = "aspirencorpsocial.db";
    int DB_VERSION = 1;

    /**
     * 部门删除标识1删除 0显示
     */
    String IS_DELETE_DEPART = "1";
    /**
     * 部门删除标识1删除 0显示
     */
    String IS_NOT_DELETE_DEPART = "0";

    /**
     * sd卡文件系统的主目录
     */
    String ROOT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "aspirencorpsocial"
            + File.separator;
    /**
     * 日志的目录
     */
    String LOG_PATH = ROOT_PATH + "log" + File.separator;
    /**
     * 用户行为日志目录
     */
    String BEHAVIOR_LOG_PATH = ROOT_PATH + "behavior_log" + File.separator;
    /** 语音的目录 */
    //String VOICE_PATH = ROOT_PATH + "voice" + File.separator;
    /**
     * 图片的目录
     */
    String PICTURE_PATH = ROOT_PATH + "picture" + File.separator;
    /**
     * 设置模块图片的目录
     */
    String SETTING_PATH = ROOT_PATH + "setting" + File.separator;
    /**
     * 临时目录
     */
    String TEMP_PATH = ROOT_PATH + "tmpdir" + File.separator;
    /**
     * 临时目录
     */
    String TEMPLATE_PATH = ROOT_PATH + "processtemplate" + File.separator;
    /**
     * 文档目录
     */
    String DOC_PATH = ROOT_PATH + "document" + File.separator;
    /**
     * HTML应用目录
     */
    String HTML_PATH = ROOT_PATH + "html" + File.separator;
    /**
     * NATIVE应用目录
     */
    String NATIVE_PATH = ROOT_PATH + "native" + File.separator;
    /**
     * IM目录
     */
    String IM_PATH = ROOT_PATH + "im" + File.separator;
    /**
     * oa目录
     */
    String PROCESS_PATH = ROOT_PATH + "process" + File.separator;
    /**
     * 公众号目录
     */
    String PUBACCOUNT_PATH = ROOT_PATH + "pubaccount" + File.separator;
    /**
     * 用户行为文件目录
     */
    String APP_FEEDBACK_PATH = ROOT_PATH + "appFeedBack" + File.separator;

    /** 亲加通讯录key */
    String QINJIA_APPKEY = "81f61271-12cf-4826-9d31-9ac41b14ca4d";
}
