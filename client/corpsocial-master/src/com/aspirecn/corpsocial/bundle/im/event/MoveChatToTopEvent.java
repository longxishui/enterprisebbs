/**
 * @(#) ImReceiveEvent.java Created on 2013-11-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 消息置顶
 *
 * @author lihaiqiang
 */
public class MoveChatToTopEvent extends BusEvent {

    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

}
