package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 接收到入群申请后批准事件
 *
 * @author lizhuo_a
 */
public class ApproveGroupApplyEvent extends BusEvent {

    /**
     * 群组标识
     */
    private String groupId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 被批准的成员ID
     */
    private String approvedUserId;

    /**
     * 被批准的成员名字
     */
    private String approvedUserName;

    /**
     * 审批者ID
     */
    private String approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    private Boolean accept;

    /**
     * 审批说明
     */
    private String description;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(String approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getApprovedUserName() {
        return approvedUserName;
    }

    public void setApprovedUserName(String approvedUserName) {
        this.approvedUserName = approvedUserName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
