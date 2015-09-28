package com.aspirecn.corpsocial.bundle.im.facade;

/**
 * Created by yinghuihong on 15/6/26.
 */
public class SystemMessage {

    private String senderId;

    private boolean isGroupMsg;

    private String chatId;

    private String content;

    public SystemMessage() {
    }

    public boolean isGroupMsg() {
        return isGroupMsg;
    }

    public void setGroupMsg(boolean isGroupMsg) {
        this.isGroupMsg = isGroupMsg;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

}
