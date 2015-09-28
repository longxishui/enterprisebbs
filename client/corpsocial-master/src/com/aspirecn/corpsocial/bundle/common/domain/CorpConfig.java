package com.aspirecn.corpsocial.bundle.common.domain;

import java.util.List;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 通知提醒配置
 */
public class CorpConfig {
    /**
     * 说明（可忽略）
     */
    public String memo;

    public String key;

    public List<ConfigData> configDataList;

    @Override
    public String toString() {
        return "CorpConfig{" +
                "memo='" + memo + '\'' +
                ", key='" + key + '\'' +
                ", configDataList=" + configDataList +
                '}';
    }
}
