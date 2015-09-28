package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-25.
 */
public class RemoveFriendEvent extends BusEvent {

    private String userid;

    public RemoveFriendEvent() {
    }

    public RemoveFriendEvent(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
