package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-25.
 */
public class AcceptInviteRespEvent extends BusEvent {

    private String userid;

    private String username;

    public AcceptInviteRespEvent() {
    }

    public AcceptInviteRespEvent(String userid) {
        this.userid = userid;
    }

    public AcceptInviteRespEvent(String userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
