package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Amos on 15-6-18.
 */
@DatabaseTable(tableName = "depart")
public class DepartEntity {

    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;

    @DatabaseField
    private String belongUserId;

    /**
     * 标识
     */
    @DatabaseField
    private String deptId;
    /**
     * 所属企业
     */
    @DatabaseField
    private String corpId;
    /**
     * 编码
     */
    @DatabaseField
    private String code;
    /**
     * 名称
     */
    @DatabaseField
    private String name;
    /**
     * 全称
     */
    @DatabaseField
    private String fullName;
    /**
     * 上级编码 最上级为0
     */
    @DatabaseField
    private String pcode;
    /**
     * 状态  0正常 1删除
     */
    @DatabaseField
    private String status;
    /**
     * 0分末端节点，1末端节点
     */
    @DatabaseField
    private String leaf;
    /**
     * 0为行业协会  1普通企业
     */
    @DatabaseField
    private String corpType;
    /**
     * 排序
     */
    @DatabaseField
    private String sortNo;
    /**
     * 最后更新时间
     */
    @DatabaseField
    private long lastModifiedTime;
    @DatabaseField
    private int isMainDept;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
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

    public int getIsMainDept() {
        return isMainDept;
    }

    public void setIsMainDept(int isMainDept) {
        this.isMainDept = isMainDept;
    }
}
