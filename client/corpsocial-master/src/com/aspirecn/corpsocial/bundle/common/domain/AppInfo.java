package com.aspirecn.corpsocial.bundle.common.domain;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 应用信息
 */
public class AppInfo {
    /**
     * 提示类型：0-冒泡提示，1-提示有新消息，2-文字
     */
    public String alertType;
    /**
     * 应用ID
     */
    public String appId;
    /**
     * 图标Url
     */
    public String icon;
    /**
     * 应用名称
     */
    public String name;
    /**
     * 排序，升序排列，越小越前
     */
    public int sortNo;
    /**
     * 启动路径，内置应用要事先约定
     */
    public String startPath;
    /**
     * 显示内容
     */
    public String text;
    /**
     * 类型：0-内置，1-公众号/服务号，2-html5应用，3-Native应用，4-链接
     */
    public String type;

    @Override
    public String toString() {
        return "AppInfoConfig{" +
                "alertType='" + alertType + '\'' +
                ", appId='" + appId + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", sortNo='" + sortNo + '\'' +
                ", startPath='" + startPath + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
