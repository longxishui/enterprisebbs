package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * Created by Amos on 15-6-24.
 */
public class FindInviteContactRespEvent extends BusEvent {

    private String message;

    private String rst;

    private int errorCode;

    private List<FriendInvite> invites;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<FriendInvite> getInvites() {
        return invites;
    }

    public void setInvites(List<FriendInvite> invites) {
        this.invites = invites;
    }
}
