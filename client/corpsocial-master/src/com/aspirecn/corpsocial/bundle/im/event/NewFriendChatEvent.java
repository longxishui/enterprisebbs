package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-26.
 */
public class NewFriendChatEvent extends BusEvent {

    private String userid;

    public NewFriendChatEvent() {
    }

    public NewFriendChatEvent(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
