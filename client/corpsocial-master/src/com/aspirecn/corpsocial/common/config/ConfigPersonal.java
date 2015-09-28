package com.aspirecn.corpsocial.common.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;

/**
 * 私人配置
 *
 * @author yinghui.hong
 */
public class ConfigPersonal {

    private static final ConfigPersonal instance = new ConfigPersonal();
    private static SharedPreferences sp;
    /**
     * 工作台：电子流列表最近刷新时间
     */
    private final String KEY_LAST_UPDATE_PROCESS_LIST = "last_update_process_list";
    /**
     * 工作台：任务列表最近刷新时间
     */
    private final String KEY_LAST_UPDATE_TASK_LIST = "last_update_task_list";
    /**
     * 工作台：已完成列表最近刷新时间
     */
    private final String KEY_LAST_UPDATE_FINISHED_LIST = "last_update_finished_list";
    private final String KEY_LAST_UPDATE_FRIEND = "last_update_friend";
    /**
     * 引用的皮肤包
     */
    private final String KEY_SKIN_PACKAGE = "skin_package";
    /** */
    private final String KEY_LAST_UPDATE_IM_MAIN_TAB = "last_update_im_main_tab";
    /** */
    private final String KEY_LAST_UPDATE_CHAT_LIST = "last_update_chat_list";
    /**
     * 提醒列表最近刷新时间
     */
    private final String KEY_LAST_UPDATE_REMIND_LIST = "last_update_remind_list";
    /**
     * 提醒列表最近刷新时间
     */
    private final String KEY_LAST_UPDATE_REMINDREAD_LIST = "last_update_remindread_list";
    /**
     * 流程更新时间
     */
    private final String PROCESS_UPDATE_TIME = "process_update_time";
    /**
     * 任务更新时间
     */
    private final String TASK_UPDATE_TIME = "task_update_time";
    /**
     * 通知配置
     */
    private final String NOTIFY_CONFIG = "notify_config";
    /**
     * 广告宣传栏
     */
    private final String KEY_ADCOLUMN_LAST_UPDATE_TIME = "adcolumn_last_time";
    /**
     * 他人考勤列表最近更新时间
     */
    private final String KEY_LAST_UPDATE_CHECK_IN_OTHERS_LIST = "last_update_check_in_others_list";
    /**
     * 日报最后更新时间
     *
     * @return
     */
    private final String KEY_LAST_UPADATE_DAILY_TIME = "last_update_daily_time";
    private final String KEY_ADDRBOOK_STATUS = "addressbook_status";
    private final String KEY_MUTI_CORP = "muti_corp";
    private final String KEY_FIRSTTIME = "firsttime";
    private final String DEPT_SYNC_LAST_TIME = "ADDRBOOK_DEPT_SYNC_LAST_TIME";
    private final String CONTACT_SYNC_LAST_TIME = "ADDRBOOK_CONTACT_SYNC_LAST_TIME";
    private final String CONTACT_INIT_STATUS = "ADDRBOOK_SYNC_DONE";
    private final String APPCENTER_SYNC_LAST_TIME = "APPCENTER_SYNC_LAST_TIME";
    private Editor editor;

    private ConfigPersonal() {
        super();
    }

    public synchronized static ConfigPersonal getInstance() {
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

    public void init(String username) {
        String fileName = Constant.SHAREDPREFE_NAME_OAWEIXIN + "_" + username;
        AspirecnCorpSocial context = AspirecnCorpSocial.getContext();
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 获取电子流列表最近刷新时间
     *
     * @return
     */
    public Long getLastUpdateProcessList() {
        return sp.getLong(KEY_LAST_UPDATE_PROCESS_LIST, 0l);
    }

    /**
     * 设置电子流列表最近刷新时间
     *
     * @param timeMillis
     */
    public void setLastUpdateProcessList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_PROCESS_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取任务列表最近刷新时间
     *
     * @return
     */
    public Long getLastUpdateTaskList() {
        return sp.getLong(KEY_LAST_UPDATE_TASK_LIST, 0l);
    }

    /**
     * 设置任务列表最近刷新时间
     *
     * @param timeMillis
     */
    public void setLastUpdateTaskList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_TASK_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取已完成列表最近刷新时间
     *
     * @return
     */
    public Long getLastUpdateFinishedList() {
        return sp.getLong(KEY_LAST_UPDATE_FINISHED_LIST, 0l);
    }

    /**
     * 设置已完成列表最近刷新时间
     *
     * @param timeMillis
     */
    public void setLastUpdateFinishedList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_FINISHED_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取提醒列表最近刷新时间
     *
     * @return
     */
    public Long getLastUpdateRemindList() {
        return sp.getLong(KEY_LAST_UPDATE_REMIND_LIST, 0l);
    }

    /**
     * 设置提醒列表最近刷新时间
     *
     * @param timeMillis
     */
    public void setLastUpdateRemindList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_REMIND_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取提醒列表最近刷新时间
     *
     * @return
     */
    public Long getLastUpdateRemindReadList() {
        return sp.getLong(KEY_LAST_UPDATE_REMINDREAD_LIST, 0l);
    }

    /**
     * 设置提醒列表最近刷新时间
     *
     * @param timeMillis
     */
    public void setLastUpdateRemindReadList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_REMINDREAD_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取引用的皮肤包
     *
     * @return
     */
    public String getSkinPackage() {
        return sp.getString(KEY_SKIN_PACKAGE, "com.aspiren.corpsocial");
    }

    /**
     * 设置引用的皮肤包
     *
     * @param packageName
     */
    public void setSkinPackage(String packageName) {
        editor.putString(KEY_SKIN_PACKAGE, packageName);
        editor.commit();
    }

    public Long getLastUpdateImMainTab() {
        return sp.getLong(KEY_LAST_UPDATE_IM_MAIN_TAB, 0l);
    }

    public void setLastUpdateImMainTab(Long timeMillis) {
        if (editor != null) {
            editor.putLong(KEY_LAST_UPDATE_IM_MAIN_TAB, timeMillis);
            editor.commit();
        }
    }


    //=========================================通讯录===========================================

    public Long getLastUpdateChatList() {
        return sp.getLong(KEY_LAST_UPDATE_CHAT_LIST, 0l);
    }

    public void setLastUpdateChatList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_CHAT_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取流程最新更新时间
     *
     * @return
     */
    public long getProcessUpdateTime() {
        return sp.getLong(PROCESS_UPDATE_TIME, 0l);
    }

    /**
     * 设置流程最新更新时间
     */
    public void setProcessUpdateTime(Long processUpdateTime) {
        editor.putLong(PROCESS_UPDATE_TIME, processUpdateTime);
        editor.commit();
    }

    /**
     * 获取任务更新时间
     *
     * @return
     */
    public long getTaskUpdateTime() {
        return sp == null ? 0l : sp.getLong(TASK_UPDATE_TIME, 0l);
    }

    /**
     * 设置任务更新时间
     */
    public void setTaskUpdateTime(Long taskUpdateTime) {
        editor.putLong(TASK_UPDATE_TIME, taskUpdateTime);
        editor.commit();
    }

    /**
     * 获取通知配置
     *
     * @return
     */
    public String getNotifyConfig() {
        return sp.getString(NOTIFY_CONFIG, "");
    }

    /**
     * 设置通知配置
     */
    public void setNotifyConfig(String notifyConfigValue) {
        editor.putString(NOTIFY_CONFIG, notifyConfigValue);
        editor.commit();
    }

    /**
     * 获取他人考勤列表最近刷新时间
     *
     * @return
     */
    public Long getLastUpdateCheckInOthersList() {
        return sp.getLong(KEY_LAST_UPDATE_CHECK_IN_OTHERS_LIST, 0l);
    }

    /**
     * 设置他人考勤列表最近刷新时间
     *
     * @param timeMillis
     */
    public void setLastUpdateCheckInOthersList(Long timeMillis) {
        editor.putLong(KEY_LAST_UPDATE_CHECK_IN_OTHERS_LIST, timeMillis);
        editor.commit();
    }

    /**
     * 获取广告宣传栏更新时间
     *
     * @return
     */
    public Long getAdcolumnLastTime() {
        return sp.getLong(KEY_ADCOLUMN_LAST_UPDATE_TIME, 0l);
    }

    /**
     * 设置广告宣传栏更新时间
     *
     * @param timeMillis
     */
    public void setAdcolumnLastTime(Long timeMillis) {
        editor.putLong(KEY_ADCOLUMN_LAST_UPDATE_TIME, timeMillis);
        editor.commit();
    }

    /**
     * 获取日报更新时间
     */
    public Long getDailyLastUpDateTime() {
        return sp.getLong(KEY_LAST_UPADATE_DAILY_TIME, 0l);
    }

    /**
     * 设置日报更新时间
     */
    public void setDailyLastUpDateTime(Long timeMillis) {
        editor.putLong(KEY_LAST_UPADATE_DAILY_TIME, timeMillis);
        editor.commit();
    }

    public Long getContactLastTime() {
        return sp.getLong(CONTACT_SYNC_LAST_TIME, 0l);
    }

    public void setContactLastTime(Long lastTime) {
        editor.putLong(CONTACT_SYNC_LAST_TIME, lastTime);
        editor.commit();
    }

    /**
     * add by amos
     *
     * @param corpId
     * @param lastTime
     */
    public void setDeptLastTime(String corpId, Long lastTime) {
        editor.putLong(DEPT_SYNC_LAST_TIME + "_" + corpId, lastTime);
        editor.commit();
    }

    public Long getDeptLastTime(String corpId) {
        return sp.getLong(DEPT_SYNC_LAST_TIME + "_" + corpId, 0l);
    }

    public int getMutiCorp() {
        return sp.getInt(KEY_MUTI_CORP, -1);
    }

    //=========================================应用中心===========================================

    public void setMutiCorp(int value) {
        editor.putInt(KEY_MUTI_CORP, value);
        editor.commit();
    }

    public int getAddrStatus() {
        return sp.getInt(KEY_ADDRBOOK_STATUS, 0);
    }

    public void setAddrStatus(int status) {
        editor.putInt(KEY_ADDRBOOK_STATUS, status);
        editor.commit();
    }


    //=======================================改造================================================

    public void setFirstTime() {
        editor.putInt(KEY_FIRSTTIME, 1);
        editor.commit();
    }

    public int getFirstTime() {
        return sp.getInt(KEY_FIRSTTIME, 0);
    }

    public Long getFriendLastTime() {
        return sp.getLong(KEY_LAST_UPDATE_FRIEND, 01);
    }

    public void setFriendLastTime(Long lastTime) {
        editor.putLong(KEY_LAST_UPDATE_FRIEND, lastTime);
        editor.commit();
    }

    public Long getDeptLastTime() {
        return sp.getLong(DEPT_SYNC_LAST_TIME, 0l);
    }

    public void setDeptLastTime(Long lastTime) {
        editor.putLong(DEPT_SYNC_LAST_TIME, lastTime);
        editor.commit();
    }

    public void setContactSyn(boolean isFinished) {
        editor.putBoolean(CONTACT_INIT_STATUS, isFinished);
        editor.commit();
    }

    public boolean isContactSynFinished() {
        return sp.getBoolean(CONTACT_INIT_STATUS, false);
    }

    public Long getAppcenterLastTime() {
        return sp.getLong(APPCENTER_SYNC_LAST_TIME, 0l);
    }

    public void setAppcenterLastTime(Long lastTime) {
        editor.putLong(APPCENTER_SYNC_LAST_TIME, lastTime);
        editor.commit();
    }

    public interface Key {
        /**
         * 个人对应企业的配置信息
         */
        String APP_CONFIG = "key_app_config";
        /**
         * 当前选择的企业ID
         */
        String CORP_ID_SELECTED = "key_corp_id_selected";

        String CORP_STATUS = "key_corp_status";

        String ADDRESS_STATUS = "key_address_status";
        String GETLISTCORP_KEY = "key_getlistcorp";
    }
}
