package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Amos on 15-6-19.
 */
@DatabaseTable(tableName = "friend_invite")
public class FriendInviteEntity {

    /**
     * 本地主键
     */
    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;
    /**
     * 邀请方id
     */
    @DatabaseField
    private String userid;
    /**
     * 邀请方用户名
     */
    @DatabaseField
    private String username;
    /**
     * 邀请方公司
     */
    @DatabaseField
    private String corpName;
    /**
     * 状态， 0为接受 1 接受
     */
    @DatabaseField
    private int status;
    /**
     * 创建日期
     */
    @DatabaseField
    private long createTime;

    @DatabaseField
    private String belongUserId;
    @DatabaseField
    private String userInfo;
    @DatabaseField
    private String smallUrl;

    @DatabaseField
    private String corpInfo;
    /**
     * 0 旧消息已读，1新消息未读
     */
    @DatabaseField
    private int isNew;
    @DatabaseField
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCorpInfo() {
        return corpInfo;
    }

    public void setCorpInfo(String corpInfo) {
        this.corpInfo = corpInfo;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }
}
