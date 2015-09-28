/**
 * @(#) Config.java Created on 2013-10-30
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */

package com.aspirecn.corpsocial.common.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The class <code>Config</code>
 *
 * @author likai
 * @version 1.0
 */
public class Config {

    private static final Config instance = new Config();
    private static SharedPreferences sp = null;
    public final String KEY_INTERVAL_TIME = "interval_time";
    /**
     * 用户名
     */
    private final String KEY_USER_NAME = "user_name";
    /**
     * 工号
     */
    private final String KEY_USER_JOBNUM = "user_jobNum";
    /**
     * 密码
     */
    private final String KEY_PASSWORD = "password";
    /**
     * 服务器返回的用户id
     */
    private final String KEY_USER_ID = "user_id";
    /**
     * 所属企业的id
     */
    private final String KEY_CORP_ID = "corp_id";
    /**
     * 所属部门的id
     */
    private final String KEY_DEPART_ID = "depart_id";
    /**
     * 昵称
     */
    private final String KEY_NICK_NAME = "nick_name";
    /**
     * 昵称
     */
    private final String KEY_LOGIN_NAME = "login_name";
    /**
     * 性别
     */
    private final String KEY_SEX = "sex";
    /**
     * 用户头像的地址
     */
    private final String KEY_HEAD_IMAGE_URL = "head_image_url";
    /**
     * 验证码
     */
    private final String KEY_VERITIFY_CODE = "veritify_code";
    /**
     * 是否已经登录
     */
    private final String KEY_LOGINED = "logined";
    /**
     * 是否已注销账号
     */
    private final String KEY_ACCOUNT = "account";
    /**
     * 是否记住密码
     */
    private final String KEY_REMEMBER_PASSWORD = "remember_password";
    /**
     * 是否开启消息提醒
     */
    private final String MESSAGE_NOTIFY = "message_notify";
    /**
     * 是否开启声音提醒
     */
    private final String MESSAGE_NOTIFY_SOUND = "message_notify_sound";
    /**
     * 是否开启震动提醒
     */
    private final String MESSAGE_NOTIFY_VIBRATE = "message_notify_vibrate";
    /**
     * 群组聊天消息提醒ID
     */
    private final int CHAT_NOTIFY_ID = 100;
    /**
     * 审批电子流消息提醒ID
     */
    private final int PROCESS_NOTIFY_ID = 101;
    /**
     * 任务消息提醒ID
     */
    private final int TASK_NOTIFY_ID = 102;
    /**
     * at的消息提醒标记
     */
    private final String AT_MSG = "at_msg";

    private Editor editor = null;

    public static Config getInstance() {
        return instance;
    }

    public static synchronized SharedPreferences getPreferences() {
        return sp;
    }

    /**
     * 保存整型值
     *
     * @return
     */
    public static void putInt(String key, int value) {
        getPreferences().edit().putInt(key, value).commit();
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getPreferences().getInt(key, 0);
    }

    /**
     * 保存长整型
     *
     * @param key
     * @param value
     */
    public static void putLong(String key, long value) {
        getPreferences().edit().putLong(key, value).commit();
    }

    /**
     * 读取长整型
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return getPreferences().getLong(key, 0l);
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public static void putBoolean(String key, Boolean value) {
        getPreferences().edit().putBoolean(key, value).commit();
    }

    /**
     * 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);

    }

    /**
     * 保存字符串
     *
     * @return
     */
    public static void putString(String key, String value) {
        getPreferences().edit().putString(key, value).commit();
    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getPreferences().getString(key, null);
    }

    /**
     * 移除字段
     *
     * @return
     */
    public static void removeKey(String key) {
        getPreferences().edit().remove(key).commit();
    }

    public int getChatNotifyId() {
        return CHAT_NOTIFY_ID;
    }

    public int getProcessNotifyId() {
        return PROCESS_NOTIFY_ID;
    }

    public int getTaskNotifyId() {
        return TASK_NOTIFY_ID;
    }

    public void init(Context context) {
        sp = context.getSharedPreferences(Constant.SHAREDPREFE_NAME_OAWEIXIN,
                Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 时间差(服务器 - 客户端)
     *
     * @return
     */
    public Long getIntervalTime() {
        return sp.getLong(KEY_INTERVAL_TIME, 0);
    }

    /**
     * 时间差(服务器 - 客户端)
     */
    public void setIntervalTime(Long intervalTime) {
        editor.putLong(KEY_INTERVAL_TIME, intervalTime);
        editor.commit();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getUserName() {
        return sp.getString(KEY_USER_NAME, "");
    }

    /**
     * 设置用户名
     *
     * @param userName
     */
    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    /**
     * 获取工号
     *
     * @return
     */
    public String getUserJobNum() {
        return sp.getString(KEY_USER_JOBNUM, "");
    }

    /**
     * 设置工号
     *
     * @param user_jobNum
     */
    public void setUserJobNum(String user_jobNum) {
        editor.putString(KEY_USER_JOBNUM, user_jobNum);
        editor.commit();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return sp.getString(KEY_PASSWORD, "");
    }

    /**
     * 设置密码
     *
     * @param password
     */
    public void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public String getUserId() {
        return sp.getString(KEY_USER_ID, "");
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    public void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.commit();
    }

    /**
     * 获取企业id
     *
     * @return
     */
    public String getCorpId() {
        return sp.getString(KEY_CORP_ID, "");
    }

    /**
     * 设置企业id
     *
     * @param corpId
     */
    public void setCorpId(String corpId) {
        editor.putString(KEY_CORP_ID, corpId);
        editor.commit();
    }

    /**
     * 获取部门id
     *
     * @return
     */
    public String getDepartId() {
        return sp.getString(KEY_DEPART_ID, "");
    }

    /**
     * 设置部门id
     *
     * @param departId
     */
    public void setDepartId(String departId) {
        editor.putString(KEY_DEPART_ID, departId);
        editor.commit();
    }

    /**
     * 获取昵称
     *
     * @return
     */
    public String getNickName() {
        return sp.getString(KEY_NICK_NAME, null);
    }

    /**
     * 设置昵称
     *
     * @param nickName
     */
    public void setNickName(String nickName) {
        editor.putString(KEY_NICK_NAME, nickName);
        editor.commit();
    }

    /**
     * 获取登录名
     *
     * @return
     */
    public String getLoginName() {
        return sp.getString(KEY_LOGIN_NAME, null);
    }

    /**
     * 设置登录名
     *
     * @param loginName
     */
    public void setLoginName(String loginName) {
        editor.putString(KEY_LOGIN_NAME, loginName);
        editor.commit();
    }

    /**
     * 获取性别，BOY = 0; GRIL = 1; OTHER = 2; UNKNOWN = 3;
     *
     * @return
     */
    public int getSex() {
        return sp.getInt(KEY_SEX, 0);
    }

    /**
     * 设置性别,BOY = 0; GRIL = 1; OTHER = 2; UNKNOWN = 3;
     *
     * @param sex
     */
    public void setSex(int sex) {
        editor.putInt(KEY_SEX, sex);
        editor.commit();
    }

    /**
     * 获取用户头像地址
     *
     * @return
     */
    public String getHeadImageUrl() {
        return sp.getString(KEY_HEAD_IMAGE_URL, null);
    }

    /**
     * 设置用户头像地址
     *
     * @param headImageUrl
     */
    public void setHeadImageUrl(String headImageUrl) {
        editor.putString(KEY_HEAD_IMAGE_URL, headImageUrl);
        editor.commit();
    }

    /**
     * 获取连接服务器所需的验证码
     *
     * @return
     */
    public String getVeritifyCode() {
        return sp.getString(KEY_VERITIFY_CODE, null);
    }

    /**
     * 设置连接服务器所需的验证码
     *
     * @param veritifyCode
     */
    public void setVeritifyCode(String veritifyCode) {
        editor.putString(KEY_VERITIFY_CODE, veritifyCode);
        editor.commit();
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    public boolean getLoginStatus() {
        return sp.getBoolean(KEY_LOGINED, false);
    }

    /**
     * 设置是否已经登录
     *
     * @param status
     */
    public void setLoginStatus(boolean status) {
        editor.putBoolean(KEY_LOGINED, status);
        editor.commit();
    }

    /**
     * 获取账号状态
     *
     * @return true:未注销 false：已注销
     */
    public boolean getAccountStatus() {
        return sp.getBoolean(KEY_ACCOUNT, false);
    }

    /**
     * 设置账号状态
     *
     * @param status true:未注销 false：已注销
     */
    public void setAccountStatus(boolean status) {
        editor.putBoolean(KEY_ACCOUNT, status);
        editor.commit();
    }

    /**
     * 设置参数值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 设置参数值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 设置参数值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 设置参数值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取参数值
     *
     * @param key
     * @param defaultValue 如果key不存在时，返回此默认值
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * 判断是否记住密码
     *
     * @return
     */
    public boolean isRememberPassword() {
        return sp.getBoolean(KEY_REMEMBER_PASSWORD, false);
    }

    /**
     * 设置是否记住密码
     *
     * @param isRememberPassword
     */
    public void setRememberPassword(boolean isRememberPassword) {
        editor.putBoolean(KEY_REMEMBER_PASSWORD, isRememberPassword);
        editor.commit();
    }

    /**
     * 判断是否开启消息提醒
     *
     * @return
     */
    public boolean isNotify() {
        return sp.getBoolean(MESSAGE_NOTIFY, true);
    }

    /**
     * 设置是否开启消息提醒
     *
     * @param isNotify
     */
    public void setNotify(boolean isNotify) {
        editor.putBoolean(MESSAGE_NOTIFY, isNotify);
        editor.commit();
    }

    /**
     * 判断是否开启声音震动提醒
     *
     * @return
     */
    public boolean isNotifySound() {
        return sp.getBoolean(MESSAGE_NOTIFY_SOUND, true);
    }

    /**
     * 设置是否开启声音消息提醒
     *
     * @param isNotifySound
     */
    public void setNotifySound(boolean isNotifySound) {
        editor.putBoolean(MESSAGE_NOTIFY_SOUND, isNotifySound);
        editor.commit();
    }

    /**
     * 判断是否开启震动消息提醒
     *
     * @return
     */
    public boolean isNotifyVibrate() {
        return sp.getBoolean(MESSAGE_NOTIFY_VIBRATE, true);
    }

    /**
     * 设置是否开启震动消息提醒
     *
     * @param isNotifyVibrate
     */
    public void setNotifyVibrate(boolean isNotifyVibrate) {
        editor.putBoolean(MESSAGE_NOTIFY_VIBRATE, isNotifyVibrate);
        editor.commit();
    }

    /**
     * 设置是否开启消息通知at
     *
     * @return
     */
    public boolean getAtMsg() {
        return sp.getBoolean(AT_MSG, false);
    }

    /**
     * 是否开启at
     *
     * @param msg
     */
    public void setAtMsg(boolean msg) {
        editor.putBoolean(AT_MSG, msg);
        editor.commit();
    }
}
