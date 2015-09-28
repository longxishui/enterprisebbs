package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 解散群事件
 *
 * @author lizhuo_a
 */
public class DismissGroupEvent extends BusEvent {

    /**
     * 群组标识
     */
    private String groupId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 解散原因
     */
    private String reason;

    /**
     * 解散人ID，接收事件使用
     */
    private String operatorId;

    /**
     * 解散人名称，接收事件使用
     */
    private String operatorName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

}
