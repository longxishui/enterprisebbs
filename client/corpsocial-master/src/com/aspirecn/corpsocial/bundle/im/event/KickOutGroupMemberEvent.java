package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * 踢出群成员事件
 *
 * @author lizhuo_a
 */
public class KickOutGroupMemberEvent extends BusEvent {

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 被踢成员列表
     */
    private List<String> kickoutUserList;

    /**
     * 踢人者ID
     */
    private String operatorId;

    /**
     * 踢人者姓名
     */
    private String operatorName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getKickoutUserList() {
        return kickoutUserList;
    }

    public void setKickoutUserList(List<String> kickoutUserList) {
        this.kickoutUserList = kickoutUserList;
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
