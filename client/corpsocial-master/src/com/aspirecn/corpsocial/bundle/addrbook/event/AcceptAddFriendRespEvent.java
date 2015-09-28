package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-19.
 */
public class AcceptAddFriendRespEvent extends BusEvent {

    private int errorCode;

    public AcceptAddFriendRespEvent() {
    }

    public AcceptAddFriendRespEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
