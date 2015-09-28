package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-25.
 */
public class RemoveFriendRespEvent extends BusEvent {

    private int status;
    private String userId;
    //0 主动删除  1被删除
    private int type;

    public RemoveFriendRespEvent() {
    }

    public RemoveFriendRespEvent(String userId) {
        this.userId = userId;
    }

    public RemoveFriendRespEvent(String userId, int status, int type) {
        this.userId = userId;
        this.status = status;
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
