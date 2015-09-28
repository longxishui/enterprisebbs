package com.aspirecn.corpsocial.common.config;

import android.text.TextUtils;

import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统通知配置
 *
 * @author lizhuo_a
 */
public class NotifyConfig {
    private static NotifyConfig instance = null;
    private Map<String, Map<String, String>> notifyConfig = new HashMap<String, Map<String, String>>();

    private NotifyConfig() {
        super();

        String notifyConfig = ConfigPersonal.getInstance().getNotifyConfig();
        if (!TextUtils.isEmpty(notifyConfig)) {
            init(notifyConfig);
        }

    }

    public static NotifyConfig getInstance() {
        if (instance == null) {
            instance = new NotifyConfig();
        }
        return instance;
    }

    public void init(String value) {
        JSONArray appNotifyaArray;
        try {
            appNotifyaArray = new JSONArray(value);
            if (appNotifyaArray != null) {
                for (int i = 0; i < appNotifyaArray.length(); i++) {
                    JSONObject jsonObject = appNotifyaArray.getJSONObject(i);

                    String moduleKey = jsonObject.getString("moduleKey");
                    JSONArray moduleVauleArray = jsonObject
                            .getJSONArray("moduleValue");

                    Map<String, String> areaNotifyMap = new HashMap<String, String>();
                    for (int j = 0; j < moduleVauleArray.length(); j++) {
                        JSONObject areaNotifyObject = moduleVauleArray
                                .getJSONObject(j);

                        // areaNotifyMap =
                        // JSONUtils.parseKeyAndValueToMap(areaNotifyObject);
                        String areaKey = areaNotifyObject.getString("areaKey");
                        String areaValue = areaNotifyObject
                                .getString("areaValue");

                        areaNotifyMap.put(areaKey, areaValue);
                    }
                    notifyConfig.put(moduleKey, areaNotifyMap);
                }
            }

        } catch (JSONException e1) {
            LogUtil.e("解析通知配置失败", e1);
        }
    }

    public Map<String, String> getNotifyConfig(NofityModule module) {
        return notifyConfig.get(module.value);
    }

    public enum NotifySwitch {
        ON("0"), OFF("1");
        public final String value;

        NotifySwitch(String value) {
            this.value = value;
        }

    }

    public enum NofityModule {
        /**
         * 群消息
         */
        NOTIFY_MSG_GROUP("NOTIFY.MSG.GROUP"),
        /**
         * 点对点消息
         */
        NOTIFY_MSG_DIALOG("NOTIFY.MSG.DIALOG"),
        /**
         * 提醒
         */
        NOTIFY_MSG_REMIND("NOTIFY.MSG.REMIND"),
        /**
         * 新待办
         */
        NOTIFY_OA_NEW("NOTIFY.OA.NEW"),
        /**
         * 流程催办
         */
        NOTIFY_OA_REMIND("NOTIFY.OA.REMIND"),
        /**
         * 新任务
         */
        NOTIFY_TASK_NEW("NOTIFY.TASK.NEW"),
        /**
         * 任务回复
         */
        NOTIFY_TASK_REPLY("NOTIFY.TASK.REPLY"),
        /**
         * 任务完成
         */
        NOTIFY_TASK_COMPLETED("NOTIFY.TASK.COMPLETED"),
        /**
         * 任务提醒
         */
        NOTIFY_TASK_REMIND("NOTIFY.TASK.REMIND"),
        /**
         * 常用联系人，签名更改
         */
        NOTIFY_CONTACTS_SIGNATURE("NOTIFY.CONTACTS.SIGNATURE"),
        /**
         * 新应用
         */
        NOTIFY_APP_NEW("NOTIFY.APP.NEW"),
        /**
         * 新新闻公告
         */
        NOTIFY_NEWS_NEW("NOTIFY.NEWS.NEW");

        private final String value;

        NofityModule(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum NofityArea {
        /**
         * 通知栏,值0-打开，1-关闭
         */
        APP_AREA_NOTIFY("APP.AREA.NOTIFY"),
        /**
         * 底部导航栏,值0-打开，1-关闭
         */
        APP_AREA_B_NAV("APP.AREA.B_NAV"),
        /**
         * 上部导航栏,值0-打开，1-关闭
         */
        APP_AREA_T_NAV("APP.AREA.T_NAV"),
        /**
         * 一级列表页面,值0-打开，1-关闭
         */
        APP_AREA_FIRSTPAGE("APP.AREA.FIRSTPAGE"),
        /**
         * 侧滑栏,值0-打开，1-关闭
         */
        APP_AREA_SIDE("APP.AREA.SIDE"),
        /**
         * 二级列表页面,值0-打开，1-关闭
         */
        APP_AREA_SECONDPAGE("APP.AREA.SECONDPAGE"),
        /**
         * 弹出框,值0-打开，1-关闭
         */
        APP_AREA_ALERT("APP.AREA.ALERT"),
        /**
         * 短信,值0-打开，1-关闭
         */
        APP_AREA_MSG("APP.AREA.MSG");

        public final String value;

        NofityArea(String value) {
            this.value = value;
        }
    }

}
