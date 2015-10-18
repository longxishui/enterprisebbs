package com.aspirecn.corpsocial.bundle.workgrp.repository.entity;

import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String fileInfoString;

    private List<FileInfoEntity> fileInfo;

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
    public List<FileInfoEntity> getConvertTOFileInfo(){
        if(!fileInfoString.isEmpty()){
            try{
                fileInfo = new ArrayList<FileInfoEntity>();
                JSONArray jsonArray = new JSONArray(fileInfoString);
                if(jsonArray.length()==0){
                    return null;
                }
                for(int i=0;i<jsonArray.length();i++){
                    FileInfoEntity fileInfoEntity = new Gson().fromJson(jsonArray.get(i).toString(),FileInfoEntity.class);
                    fileInfo.add(fileInfoEntity);
                }
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.e("ReplyfileInfo转换失败");
            }
        }
        return fileInfo;
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
