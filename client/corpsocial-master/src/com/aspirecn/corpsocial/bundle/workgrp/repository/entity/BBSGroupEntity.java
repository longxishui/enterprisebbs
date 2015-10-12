package com.aspirecn.corpsocial.bundle.workgrp.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "bbs_group")
public class BBSGroupEntity implements Serializable {
    private static final long serialVersionUID = 1674051434752381221L;

    @DatabaseField(id = true, unique = true)
    private String bbsGroupId;

    @DatabaseField
    private String name;

    @DatabaseField
    private String iconurl;

    @DatabaseField
    private String sortNo;

    @DatabaseField
    private String belongUserId;
    @DatabaseField
    private long lastModifiedTime;
    @DatabaseField
    private String belongCorpId;
    @DatabaseField
    private String limitType;

    public BBSGroupEntity() {
        super();
    }

    public BBSGroupEntity(String id, String name, String iconurl,
                          String sortNo, String adminList, String userid, String corpId) {
        super();
        this.bbsGroupId = id;
        this.name = name;
        this.iconurl = iconurl;
        this.sortNo = sortNo;
        this.belongUserId = userid;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
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
    public String getBbsGroupId() {
        return bbsGroupId;
    }

    public void setBbsGroupId(String bbsGroupId) {
        this.bbsGroupId = bbsGroupId;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }
}
