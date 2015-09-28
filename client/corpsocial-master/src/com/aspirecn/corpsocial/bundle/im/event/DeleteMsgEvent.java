/**
 * @(#) ImReceiveEvent.java Created on 2013-11-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * 删除聊天消息
 *
 * @author lizhuo_a
 */
public class DeleteMsgEvent extends BusEvent {

    private String chatId;

    private List<String> msgIds;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(List<String> msgIds) {
        this.msgIds = msgIds;
    }

}
