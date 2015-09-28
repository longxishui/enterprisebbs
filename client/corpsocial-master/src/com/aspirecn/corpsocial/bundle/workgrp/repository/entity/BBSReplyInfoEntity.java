package com.aspirecn.corpsocial.bundle.workgrp.repository.entity;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "bbs_replyinfo")
public class BBSReplyInfoEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8183682108292015460L;
    @DatabaseField(id = true, unique = true)
    private String id;
    @DatabaseField
    private String itemId;
    @DatabaseField
    private String replyerId;
    @DatabaseField
    private String replyerName;
    @DatabaseField
    private long time;
    @DatabaseField
    private String content;
    /**
     * 用户 的id
     */
    @DatabaseField
    private String userid;
    @DatabaseField
    private String corpId;
    @DatabaseField
    private String fileInfo;

    private FileInfoEntity fileInfoData;

    public BBSReplyInfoEntity(String id, String itemId, String replyerId,
                              String replyerName, long time, String content, String userid) {
        super();
        this.id = id;
        this.itemId = itemId;
        this.replyerId = replyerId;
        this.replyerName = replyerName;
        this.time = time;
        this.content = content;
        this.userid = userid;
    }

    public BBSReplyInfoEntity() {
        super();
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getReplyerId() {
        return replyerId;
    }

    public void setReplyerId(String replyerId) {
        this.replyerId = replyerId;
    }

    public String getReplyerName() {
        return replyerName;
    }

    public void setReplyerName(String replyerName) {
        this.replyerName = replyerName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileInfoEntity getFileInfoData() {
        return fileInfoData;
    }

    public void setFileInfoData(FileInfoEntity fileInfoData) {
        this.fileInfoData = fileInfoData;
    }
    public FileInfoEntity getConvertTOFileInfo(){
        if(fileInfo==null||fileInfo.equals("")){
            return null;
        }
        return new Gson().fromJson(fileInfo,FileInfoEntity.class);
    }
    @Override
    public String toString() {
        return "BBSReplyInfoEntity{" +
                "id='" + id + '\'' +
                ", itemId='" + itemId + '\'' +
                ", replyerId='" + replyerId + '\'' +
                ", replyerName='" + replyerName + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", userid='" + userid + '\'' +
                ", corpId='" + corpId + '\'' +
                '}';
    }
}
