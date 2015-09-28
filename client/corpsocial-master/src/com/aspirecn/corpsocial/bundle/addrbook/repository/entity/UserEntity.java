package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Amos on 15-6-16.
 */
@DatabaseTable(tableName = "addrbook_user")
public class UserEntity {

    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;

    @DatabaseField
    private String belongUserId;

    /**
     * 是否好友 1 yes 0 no
     */
    @DatabaseField
    private int isFriend;
    /**
     * 是否同组织 1 yes 0 no
     */
    @DatabaseField
    private int isSameCorp;

    /**
     * 标识
     */
    @DatabaseField(index = true, indexName = "IDX_USER_USERID")
    private String userid;
    /**
     * 所属企业列表
     */
    @DatabaseField(index = true, indexName = "IDX_USER_CORP")
    private String corpId;
    /**
     * 部门
     */
    @DatabaseField(index = true, indexName = "IDX_USER_DEPT")
    private String deptId;

    @DatabaseField(index = true, indexName = "IDX_USER_DEPTCODE")
    private String deptCode;

    @DatabaseField
    private String deptName;
    @DatabaseField
    private String duty;

    //兼任部门信息，格式：部门编码_部门ID_部门名称_职务，如果有多个则以;分隔。
    @DatabaseField(index = true, indexName = "IDX_USER_PARTTIME_DEPT")
    private String partTimeDept;
    /**
     * 姓名
     */
    @DatabaseField
    private String name;
    /**
     * 头像
     */
    @DatabaseField
    private String url;
    /**
     * 头像缩略图
     */
    @DatabaseField
    private String smallUrl;
    /**
     * 手机
     */
    @DatabaseField(index = true, indexName = "IDX_USER_CELLPHONE")
    private String cellphone;
    /**
     * 拼音码
     */
    @DatabaseField
    private String spellKey;
    /**
     * 名字首字母
     */
    @DatabaseField
    private String initialKey;
    /**
     * 微信号状态 0已开通 1未开通 2已激活 3已关闭
     */
    @DatabaseField
    private String imStatus;
    /**
     * 个性签名
     */
    @DatabaseField
    private String signature;
    /**
     * 最后更新时间
     */
    @DatabaseField
    private long lastModifiedTime;


    /**
     * 用户企业信息#####################################################################
     */

    @DatabaseField
    private String corpName;
    @DatabaseField
    private String telephone;
    @DatabaseField
    private String innerPhone;
    @DatabaseField
    private String location;
    @DatabaseField
    private String email;
    @DatabaseField
    private String sortNo;
    @DatabaseField
    private int isStar;
    @DatabaseField
    private String loginName;
    @DatabaseField
    private String status;
    @DatabaseField
    private String userType;
    @DatabaseField
    private int isShow;

    /**
     * 是否临时用户信息
     */
    @DatabaseField
    private int isTemp = 0;

    @DatabaseField
    private int isFreq = 0;

    public int getIsFreq() {
        return isFreq;
    }

    public void setIsFreq(int isFreq) {
        this.isFreq = isFreq;
    }

    public int getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(int isTemp) {
        this.isTemp = isTemp;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPartTimeDept() {
        return partTimeDept;
    }

    public void setPartTimeDept(String partTimeDept) {
        this.partTimeDept = partTimeDept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getSpellKey() {
        return spellKey;
    }

    public void setSpellKey(String spellKey) {
        this.spellKey = spellKey;
    }

    public String getInitialKey() {
        return initialKey;
    }

    public void setInitialKey(String initialKey) {
        this.initialKey = initialKey;
    }

    public String getImStatus() {
        return imStatus;
    }

    public void setImStatus(String imStatus) {
        this.imStatus = imStatus;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public int getIsSameCorp() {
        return isSameCorp;
    }

    public void setIsSameCorp(int isSameCorp) {
        this.isSameCorp = isSameCorp;
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

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
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

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }


}
