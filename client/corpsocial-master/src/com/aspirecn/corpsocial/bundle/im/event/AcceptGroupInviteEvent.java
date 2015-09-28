package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 接收到邀请后反馈事件
 *
 * @author lizhuo_a
 */
public class AcceptGroupInviteEvent extends BusEvent {

    // 群组标识
    private String groupId;

    private String groupName;

    private String userId;

    private String userName;

    private String userDeptName;

    private Boolean accept;

    private String content;

    private GroupMemberEntity groupMemberEntity;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserDeptName() {
        return userDeptName;
    }

    public void setUserDeptName(String userDeptName) {
        this.userDeptName = userDeptName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GroupMemberEntity getGroupMemberEntity() {
        return groupMemberEntity;
    }

    public void setGroupMemberEntity(GroupMemberEntity groupMemberEntity) {
        this.groupMemberEntity = groupMemberEntity;
    }

}
