package com.aspirecn.corpsocial.bundle.addrbook.facade;

import java.io.Serializable;

public class Contact implements Serializable {

    private static final long serialVersionUID = -1424705759859424844L;

    /**
     * 联系人ID
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 部门名称
     */
    private String deptName;

    //职务
    private String duty;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 微信号状态：0-已开通，1-未开通，2-已激活，3-已关闭
     */
    private String imStatus;

    /**
     * 用户头像
     */
    private String headImgUrl;

    /**
     * 名字首字母
     */
    private String initialCode;

    /* 个性签名*/
    private String signature;

    private String loginName;

    private String userType; //人员类型0-正式员工，1-外包人员

    public static long getSerialversionuid() {
        return serialVersionUID;
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

    public String getImStatus() {
        return imStatus;
    }

    public void setImStatus(String imStatus) {
        this.imStatus = imStatus;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append(getKeyValue("loginName", loginName));
        sb.append(",");
        sb.append(getKeyValue("id", id));
        sb.append(",");
        sb.append(getKeyValue("name", name));
        sb.append(",");
        sb.append(getKeyValue("mobilePhone", mobilePhone));
        sb.append(",");
        sb.append(getKeyValue("deptName", deptName));
        sb.append(",");
        sb.append(getKeyValue("initialCode", initialCode));
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
