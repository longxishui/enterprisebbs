package com.aspirecn.corpsocial.bundle.im.repository.entity;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;

/**
 * 点对点会话设置
 *
 * @author lizhuo_a
 */
public class P2PChatSetting extends ChatSetting {

    private User user;
    private String chatId;
    private boolean newMsgNotify;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public boolean isNewMsgNotify() {
        return newMsgNotify;
    }

    public void setNewMsgNotify(boolean newMsgNotify) {
        this.newMsgNotify = newMsgNotify;
    }
}
