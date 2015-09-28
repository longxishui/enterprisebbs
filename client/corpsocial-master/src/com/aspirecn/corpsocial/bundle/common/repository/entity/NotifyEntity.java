package com.aspirecn.corpsocial.bundle.common.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 消息通知
 * <p/>
 * Created by yinghuihong on 15/7/1.
 */
@DatabaseTable(tableName = "common_notify")
public class NotifyEntity {

    /**
     * 序列号
     */
    @DatabaseField(generatedId = true, unique = true)
    public int seqNo;
    /**
     * 所属用户Id，适应多用户场景
     */
    @DatabaseField
    public String belongUserId;
    /**
     * 所属企业ID，适应多企业场景
     */
    @DatabaseField
    public String belongCorpId;
    /**
     * 模块ID
     */
    @DatabaseField
    public int modelId;
    /**
     * 模块名
     */
    @DatabaseField
    public String model;
    /**
     * 处理标记
     */
    @DatabaseField(defaultValue = "false")
    public boolean isHandled;

    /**
     * 更新时间
     */
    @DatabaseField
    public Date receiveDate;

    @Override
    public String toString() {
        return "NotifyEntity{" +
                "seqNo=" + seqNo +
                ", belongUserId='" + belongUserId + '\'' +
                ", belongCorpId='" + belongCorpId + '\'' +
                ", modelId=" + modelId +
                ", model='" + model + '\'' +
                ", isHandled=" + isHandled +
                ", receiveDate=" + receiveDate +
                '}';
    }
}
