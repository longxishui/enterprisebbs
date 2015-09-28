package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class LoadPictureEvent extends BusEvent {

    private MsgEntity msgEntity;

    public MsgEntity getMsgEntity() {
        return msgEntity;
    }

    public void setMsgEntity(MsgEntity msgEntity) {
        this.msgEntity = msgEntity;
    }
}
