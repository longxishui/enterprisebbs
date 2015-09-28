package com.aspirecn.corpsocial.bundle.common.facade;

/**
 * Created by yinghuihong on 15/5/24.
 */
public enum AppStartPath {
    // 消息
    IM("WORK_GROUP"),
    // 通讯录
    ADDRESS_BOOK("CORP_ADDRESS"),
    // 工作台
    WORKBENCH("WORK_AREA"),
    // 设置
    SETTING("SETTING"),
    // 服务号
    PUB_ACCOUNT("PUB_ACC"),
    // 同事圈
    WORK_GROUP("CORP_BBS"),
    // 日程管理
    SCHEDULE("WORK_SCHEDULE"),
    // 考勤管理
    CHECK_IN("WORK_ATTENDANCE"),
    // 协同办公
    OA("WORK_PROCESS"),
    // 日报管理
    DAILY("REPORT_DAILY"),
    // 应用中心
    APP_CENTER("APP_CENTER"),
    // 多方通话
    EXTENSIVE_CALL("EXTENSIVE_CALL");


    public String START_PATH;

    AppStartPath(String startPath) {
        this.START_PATH = startPath;
    }

}