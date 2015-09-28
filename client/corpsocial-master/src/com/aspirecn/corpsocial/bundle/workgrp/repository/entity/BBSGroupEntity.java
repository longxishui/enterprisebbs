package com.aspirecn.corpsocial.bundle.workgrp.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "bbs_group")
public class BBSGroupEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1674051434752381221L;

    @DatabaseField(id = true, unique = true)
    private String id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String iconurl;

    @DatabaseField
    private String sortNo;

    @DatabaseField
    private String adminList;
    @DatabaseField
    private String belongUserId;
    @DatabaseField
    private int limitType;
    @DatabaseField
    private long lastModifiedTime;
    @DatabaseField
    private String belongCorpId;

    public BBSGroupEntity() {
        super();
    }

    public BBSGroupEntity(String id, String name, String iconurl,
                          String sortNo, String adminList, String userid) {
        super();
        this.id = id;
        this.name = name;
        this.iconurl = iconurl;
        this.sortNo = sortNo;
        this.adminList = adminList;
        this.belongUserId = userid;
    }

    public BBSGroupEntity(String id, String name, String iconurl,
                          String sortNo, String adminList, String userid, String corpId) {
        super();
        this.id = id;
        this.name = name;
        this.iconurl = iconurl;
        this.sortNo = sortNo;
        this.adminList = adminList;
        this.belongUserId = userid;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getLimitType() {
        return limitType;
    }

    public void setLimitType(int limitType) {
        this.limitType = limitType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getAdminList() {
        return adminList;
    }

    public void setAdminList(String adminList) {
        this.adminList = adminList;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getBelongCorpId() {
        return belongCorpId;
    }

    public void setBelongCorpId(String belongCorpId) {
        this.belongCorpId = belongCorpId;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    @Override
    public String toString() {
        return "BBSGroupEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", sortNo='" + sortNo + '\'' +
                ", adminList='" + adminList + '\'' +
                ", belongUserId='" + belongUserId + '\'' +
                ", limitType=" + limitType +
                ", lastModifiedTime=" + lastModifiedTime +
                ", belongCorpId='" + belongCorpId + '\'' +
                '}';
    }

    interface GroupLimitType {
        int LIMIT_NO = 0;
        int LIMIT_YES = 2;
    }
}
