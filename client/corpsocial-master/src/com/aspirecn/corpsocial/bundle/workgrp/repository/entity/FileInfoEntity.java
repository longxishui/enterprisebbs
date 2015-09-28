package com.aspirecn.corpsocial.bundle.workgrp.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "bbs_fileinfo")
public class FileInfoEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3120012842289771132L;
    @DatabaseField(id = true, unique = true)
    private String id;
    @DatabaseField
    private String groupName;
    @DatabaseField
    private String address;
    @DatabaseField
    private long length;
    @DatabaseField
    private String orginalAddress;
    @DatabaseField
    private String url;
    @DatabaseField
    private String orginalUrl;
    @DatabaseField
    private long orginalLength;
    /**
     * 用户的 id
     */
    @DatabaseField
    private String userid;

    @DatabaseField
    private String corpId;

    public FileInfoEntity() {
        super();
    }

    public FileInfoEntity(String id, String groupName, String address,
                          long length, String orginalAddress, String url, String orginalUrl,
                          long orginalLength) {
        super();
        this.id = id;
        this.groupName = groupName;
        this.address = address;
        this.length = length;
        this.orginalAddress = orginalAddress;
        this.url = url;
        this.orginalUrl = orginalUrl;
        this.orginalLength = orginalLength;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getOrginalAddress() {
        return orginalAddress;
    }

    public void setOrginalAddress(String orginalAddress) {
        this.orginalAddress = orginalAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrginalUrl() {
        return orginalUrl;
    }

    public void setOrginalUrl(String orginalUrl) {
        this.orginalUrl = orginalUrl;
    }

    public long getOrginalLength() {
        return orginalLength;
    }

    public void setOrginalLength(long orginalLength) {
        this.orginalLength = orginalLength;
    }

    @Override
    public String toString() {
        return "FileInfoEntity [id=" + id + ", groupName=" + groupName
                + ", address=" + address + ", length=" + length
                + ", orginalAddress=" + orginalAddress + ", url=" + url
                + ", orginalUrl=" + orginalUrl + ", orginalLength="
                + orginalLength + ", userid=" + userid + "]";
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
