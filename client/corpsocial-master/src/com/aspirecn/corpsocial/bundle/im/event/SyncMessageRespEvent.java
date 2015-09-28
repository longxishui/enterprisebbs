package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class SyncMessageRespEvent extends BusEvent {

    private MsgEntity msgEntity;

    private int errorCode;

    private String errorMsg;

    private float percent;

    public MsgEntity getMsgEntity() {
        return msgEntity;
    }

    public void setMsgEntity(MsgEntity msgEntity) {
        this.msgEntity = msgEntity;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
