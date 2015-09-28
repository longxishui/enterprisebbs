/**
 * @(#) ImReceiveEvent.java Created on 2013-11-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 发送Im消息事件
 *
 * @author lizhuo_a
 */
public class SendMessageEvent extends BusEvent {

    private MsgEntity msgEntity;

    private boolean groupMsg = false;

    public MsgEntity getMsgEntity() {
        return msgEntity;
    }

    public void setMsgEntity(MsgEntity msgEntity) {
        this.msgEntity = msgEntity;
    }

    public boolean isGroupMsg() {
        return groupMsg;
    }

    public void setGroupMsg(boolean groupMsg) {
        this.groupMsg = groupMsg;
    }

}
