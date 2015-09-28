/**
 * @(#) ImReceiveEvent.java Created on 2013-11-11
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.event;

import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 加载单个会话
 *
 * @author lizhuo_a
 */
public class LoadChatEvent extends BusEvent {
    private ChatEntity chatEntity;

    private int index = 0;

    /**
     * 默认加载10条信息
     */
    private int count = 30;

    public ChatEntity getChatEntity() {
        return chatEntity;
    }

    public void setChatEntity(ChatEntity chatEntity) {
        this.chatEntity = chatEntity;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
