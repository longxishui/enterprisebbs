package com.aspirecn.corpsocial.bundle.common.domain;

import android.text.TextUtils;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspiren.corpsocial.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 应用的全部配置信息
 */
public class AppConfig {
    /**
     * 应用名称（如：工作.圈）
     */
    public String appName;
    /**
     * 企业logo
     */
    public String icon;
    /**
     * 登陆页logo
     */
    public String preIcon;
    /**
     * 企业业务介绍
     */
    public List<String> sumaryIcon;
    /**
     * 企业口号
     */
    public String slogan;
    /**
     * 昵称
     */
    public String nickName;
    /**
     * 用户配置信息
     */
    public List<ConfigInfo> userConfigList;
    /**
     * 企业配置信息
     */
    public List<CorpConfig> corpConfigList;
    /**
     * 底部应用定义
     */
    public List<AppInfo> bottomDef;
    /**
     * 工作台APP分组
     */
    public List<WorkAreaDef> workAreaDef;
    /**
     * ActionBar参数配置
     */
    public ViewDef topViewDef;
    /**
     * 底部导航栏参数配置
     */
    public ViewDef bottomViewDef;
    /**
     * 工作台应用列表参数配置
     */
    public ViewDef workAreaViewDef;


    /**
     * 默认返回已选企业的配置
     *
     * @return
     */
    public static AppConfig getInstance() {
        return getInstance(ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
    }

    /**
     * 将保存在SP的JSON字符串转为对象
     *
     * @return
     */
    public static AppConfig getInstance(String corpId) {
        AppConfig config;
        String appConfigString = ConfigPersonal.getString(ConfigPersonal.Key.APP_CONFIG + corpId);
        if (!TextUtils.isEmpty(appConfigString)) {
            config = new Gson().fromJson(appConfigString, AppConfig.class);
            if (TextUtils.isEmpty(config.appName)) {
                config.appName = "ICT生态圈";
            }
            if (config.topViewDef == null) {
                config.topViewDef = new ViewDef();
            }
            if (TextUtils.isEmpty(config.topViewDef.backgroundColor)) {
                config.topViewDef.backgroundColor = "#0085d0";
            }
            if (TextUtils.isEmpty(config.topViewDef.fontColor)) {
                config.topViewDef.fontColor = "#ffffff";
            }

            if (config.bottomViewDef == null) {
                config.bottomViewDef = new ViewDef();
            }
            if (TextUtils.isEmpty(config.bottomViewDef.backgroundColor)) {
                config.bottomViewDef.backgroundColor = "#ffffff";
            }
            if (TextUtils.isEmpty(config.bottomViewDef.fontColor)) {
                config.bottomViewDef.fontColor = "#0085d0";
            }

            if (TextUtils.isEmpty(config.slogan)) {
                config.slogan = AspirecnCorpSocial.getContext().getResources().getString(R.string.common_startup_slogan);
            }

        } else {
            config = new AppConfig();
            config.appName = "工作•圈";
            config.topViewDef = new ViewDef();
            config.topViewDef.backgroundColor = "#0085d0";
            config.topViewDef.fontColor = "#ffffff";

            config.bottomViewDef = new ViewDef();
            config.bottomViewDef.backgroundColor = "#ffffff";
            config.bottomViewDef.fontColor = "#0085d0";
            config.slogan = AspirecnCorpSocial.getContext().getResources().getString(R.string.common_startup_slogan);
        }
        return config;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "appName='" + appName + '\'' +
                ", icon='" + icon + '\'' +
                ", preIcon='" + preIcon + '\'' +
                ", sumaryIcon=" + sumaryIcon +
                ", slogan='" + slogan + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userConfigList=" + userConfigList +
                ", corpConfigList=" + corpConfigList +
                ", bottomDef=" + bottomDef +
                ", workAreaDef=" + workAreaDef +
                ", topViewDef=" + topViewDef +
                ", bottomViewDef=" + bottomViewDef +
                ", workAreaViewDef=" + workAreaViewDef +
                '}';
    }
}
