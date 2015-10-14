package com.aspirecn.corpsocial.bundle.workgrp.repository.entity;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

@DatabaseTable(tableName = "bbs_item")
public class BBSItemEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2910040198870956879L;

    @DatabaseField(id = true, unique = true)
    private String itemId;
    @DatabaseField
    private String groupId;
    @DatabaseField
    private String title;
    @DatabaseField
    private String creatorId;
    @DatabaseField
    private String creatorName;
    @DatabaseField
    private String replyTimes;
    @DatabaseField
    private String praiseTimes;
    @DatabaseField
    private String content;
    @DatabaseField
    private long createTime;
    @DatabaseField
    private String creatorHeadImg;
    @DatabaseField
    private long lastModifiedTime;
    /**
     * 用户的id
     */
    @DatabaseField
    private String userid;
    @DatabaseField
    private String corpId;
    /** 帖子的图片的json数据 */
    @DatabaseField
    private String fileInfoString;
    @DatabaseField
    private boolean hasPic;
    @DatabaseField
    private String status;
    @DatabaseField
    private String isPraised;

    private List<FileInfoEntity> fileInfo;
    public BBSItemEntity() {
        super();
    }

    public BBSItemEntity(String id, String groupId, String title,
                         String creatorId, String creatorName, String replyTimes,
                         String praiseTimes, String content, long createTime,
                         String praiseUserIds, long lastModifyTime, String userid) {
        super();
        this.itemId = id;
        this.groupId = groupId;
        this.title = title;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.replyTimes = replyTimes;
        this.praiseTimes = praiseTimes;
        this.content = content;
        this.createTime = createTime;
        this.lastModifiedTime = lastModifyTime;
        this.userid = userid;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getReplyTimes() {
        return replyTimes;
    }

    public void setReplyTimes(String replyTimes) {
        this.replyTimes = replyTimes;
    }

    public String getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(String praiseTimes) {
        this.praiseTimes = praiseTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCreatorHeadImg() {
        return creatorHeadImg;
    }

    public void setCreatorHeadImg(String creatorHeadImg) {
        this.creatorHeadImg = creatorHeadImg;
    }

    public String getIsPraised() {
        return isPraised;
    }

    public void setIsPraised(String isPraised) {
        this.isPraised = isPraised;
    }

    public String getFileInfoString() {
        return fileInfoString;
    }

    public void setFileInfoString(String fileInfoString) {
        this.fileInfoString = fileInfoString;
    }

    public List<FileInfoEntity> getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(List<FileInfoEntity> fileInfo) {
        this.fileInfo = fileInfo;
    }
}
