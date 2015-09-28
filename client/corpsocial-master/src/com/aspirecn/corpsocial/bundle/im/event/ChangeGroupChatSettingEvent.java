package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupChatSetting;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class ChangeGroupChatSettingEvent extends BusEvent {

    private GroupChatSetting groupChatSetting;

    public GroupChatSetting getGroupChatSetting() {
        return groupChatSetting;
    }

    public void setGroupChatSetting(GroupChatSetting groupChatSetting) {
        this.groupChatSetting = groupChatSetting;
    }


}
