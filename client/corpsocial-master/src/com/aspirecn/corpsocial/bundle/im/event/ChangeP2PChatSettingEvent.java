package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.P2PChatSetting;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class ChangeP2PChatSettingEvent extends BusEvent {

    private P2PChatSetting p2pChatSetting;

    public P2PChatSetting getP2pChatSetting() {
        return p2pChatSetting;
    }

    public void setP2pChatSetting(P2PChatSetting p2pChatSetting) {
        this.p2pChatSetting = p2pChatSetting;
    }

}
