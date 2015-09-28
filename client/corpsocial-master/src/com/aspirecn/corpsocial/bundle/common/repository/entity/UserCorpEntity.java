package com.aspirecn.corpsocial.bundle.common.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Amos on 15-6-18.
 */
@DatabaseTable(tableName = "user_corp")
public class UserCorpEntity {

    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;

    @DatabaseField
    private String belongUserId;
    @DatabaseField
    private String corpId;
    @DatabaseField
    private String corpName;
    @DatabaseField
    private String userId;

    /**
     * 外线电话
     */
    @DatabaseField
    private String telephone;
    /**
     * 手机短号
     */
    @DatabaseField
    private String innerPhone;
    /**
     * 办公地点
     */
    @DatabaseField
    private String location;
    /**
     *
     */
    @DatabaseField
    private String email;
    /**
     * 是否标星
     */
    @DatabaseField
    private int isStar;
    /**
     * 排序
     */
    @DatabaseField
    private String sortNo;
    /**
     * oa登录名
     */
    @DatabaseField
    private String loginName;
    /**
     * 状态 0正常 1已删除
     */
    @DatabaseField
    private String status;
    /**
     * 0正式员工 1外包人员 2兼职
     */
    @DatabaseField
    private String userType;
    /**
     * 是否显示手机号码 0显示 1隐藏
     */
    @DatabaseField
    private int isShow;

    @DatabaseField
    private long lastModifiedTime;

    @DatabaseField
    private long userLastModifiedTime;

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public long getUserLastModifiedTime() {
        return userLastModifiedTime;
    }

    public void setUserLastModifiedTime(long userLastModifiedTime) {
        this.userLastModifiedTime = userLastModifiedTime;
    }

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getInnerPhone() {
        return innerPhone;
    }

    public void setInnerPhone(String innerPhone) {
        this.innerPhone = innerPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
