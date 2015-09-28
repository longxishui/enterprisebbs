/**
 * @(#) User.java Created on 2013��10��31��
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.domain;

import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The class <code>User</code>
 *
 * @author danel
 * @version 1.0
 */
public class User implements Serializable {

    //	/** SerialVersionUID */
    private static final long serialVersionUID = -674624358696975109L;

    /**
     * 标识
     */
    private String userid;
    /**
     * 所属企业列表
     */
    private List<UserCorp> corpList;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String url;
    /**
     * 头像缩略图
     */
    private String smallUrl;
    /**
     * 手机
     */
    private String cellphone;
    /**
     * 拼音码
     */
    private String spellKey;
    /**
     * 名字首字母
     */
    private String initialKey;
    /**
     * 微信号状态 0已开通 1未开通 2已激活 3已关闭
     */
    private String imStatus;
    /**
     * 个性签名
     */
    private String signature;
    /**
     * 最后更新时间
     */
    private long lastModifiedTime;
    /**
     * 是否好友 1 yes 0 no
     */
    private int isFriend;

    private int isSameCorp;

    private String corpId;

    private String corpName;

    //main
    private String deptId;

    private String deptCode;

    private String pDeptCode;

    private String deptName;

    private String duty;

    private String email;

    /**
     * 手机短号
     */
    private String innerPhone;
    /**
     * 办公地点
     */
    private String location;

    /**
     * 是否标星
     */
    private int isStar;

    /**
     * oa登录名
     */
    private String loginName;

    /**
     * 是否显示手机号码 0显示 1隐藏
     */
    private int isShow;

    /**
     * 0正式员工 1外包人员 2兼职
     */
    private String userType;

    //all depts
    private List<UserDept> depts;
    /**
     * 是否临时用户 0 是，1否（正式用户)
     */
    private int isTemp = 0;

    private int isFreq = 0;

    public int getIsFreq() {
        return isFreq;
    }

    public void setIsFreq(int isFreq) {
        this.isFreq = isFreq;
    }

    /**
     * 是否可通信
     *
     * @return
     */
    public boolean canCommunicate() {
        return (isSameCorp == 1) || (isFriend == 1);
    }

    public int getIsSameCorp() {
        return isSameCorp;
    }

    public void setIsSameCorp(int isSameCorp) {
        this.isSameCorp = isSameCorp;
    }

    public String getpDeptCode() {
        return pDeptCode;
    }

    public void setpDeptCode(String pDeptCode) {
        this.pDeptCode = pDeptCode;
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

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(int isTemp) {
        this.isTemp = isTemp;
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

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<UserCorp> getCorpList() {
        return corpList;
    }

    public void setCorpList(List<UserCorp> corpList) {
        this.corpList = corpList;
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

    public List<UserDept> getDepts() {
        return depts;
    }

    public void setDepts(List<UserDept> depts) {
        this.depts = depts;
    }

    public String toString() {
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < fields.length; i++) {
                builder.append(fields[i].getName() + ":" + fields[i].get(this)).append(" ");
            }
        } catch (Exception e) {
        }

        return builder.toString();
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append(getKeyValue("loginName", loginName));
        sb.append(",");
        sb.append(getKeyValue("id", userid));
        sb.append(",");
        sb.append(getKeyValue("name", name));
        sb.append(",");
        sb.append(getKeyValue("mobilePhone", cellphone));
        sb.append(",");
        sb.append(getKeyValue("deptName", deptName));
        sb.append(",");
        sb.append(getKeyValue("initialCode", initialKey));
        sb.append(",");
        sb.append(getKeyValue("imStatus", imStatus));
        sb.append(",");
        sb.append(getKeyValue("userType", userType));

        sb.append("}");
        return sb.toString();
    }

    private String getKeyValue(String key, String value) {
        if (value == null) {
            return String.format("\"%s\": null", key, value);
        } else {
            return String.format("\"%s\": \"%s\"", key, value);
        }
    }
}
