/**
 * @(#) Chat.java Created on 2013年11月4日
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.repository.entity;

import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lizhuo_a
 */
@DatabaseTable(tableName = "im_chat")
public class ChatEntity implements Serializable {

    private static final long serialVersionUID = 9149522392745065953L;

    /**
     * 序列号
     */
    @DatabaseField(generatedId = true)
    private int seqNo;

    /**
     * 对话ID，群聊为群ID，点对点为对方用户ID
     */
    @DatabaseField
    private String chatId;

    /**
     * 会话名称，点对点为聊天对象人名称，群聊为群名
     */
    @DatabaseField
    private String chatName;

    @DatabaseField
    private String description;

    /**
     * 对话类型
     */
    @DatabaseField
    private int chatType;
    /**
     * 会话设置信息，json格式
     */
    @DatabaseField
    private String setting;
    /**
     * 会话所属用户Id，适应多用户场景
     */
    @DatabaseField
    private String belongUserId;
    // 创建时间
    @DatabaseField(dataType = DataType.DATE)
    private Date createTime;
    /**
     * 最近更新事件，每次发送或者接收新消息需要更新
     */
    @DatabaseField(dataType = DataType.DATE)
    private Date lastUpdateTime;
    /**
     * 通知条数
     */
    @DatabaseField
    private int noticeCount = 0;
    /**
     * 是否显示 ，显示为0，不显示为1
     */
    @DatabaseField
    private boolean display;
    /**
     * 置顶时间
     */
    @DatabaseField(dataType = DataType.DATE)
    private Date moveToTopTime;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(int noticeCount) {
        this.noticeCount = noticeCount;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getSetting() {
        if (TextUtils.isEmpty(setting)) {
            JSONObject chatSetting = new JSONObject();
            try {
                // 默认接收新消息
                chatSetting.put("newMsgNotify", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return chatSetting.toString();
        } else {
            return setting;
        }
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getMoveToTopTime() {
        return moveToTopTime;
    }

    public void setMoveToTopTime(Date moveToTopTime) {
        this.moveToTopTime = moveToTopTime;
    }

    /**
     * 对话类型 0为点对点对话，1为群对话
     *
     * @author lizhuo_a
     */
    public enum ChatType {
        P2P(0), GROUP(1);

        public int value;

        ChatType(int value) {
            this.value = value;
        }
    }

}
