package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 接收到邀请后反馈事件
 *
 * @author lizhuo_a
 */
public class AcceptGroupInviteRespEvent extends BusEvent {

    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
