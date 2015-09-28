package com.aspirecn.corpsocial.bundle.addrbook.repository.vo;

import com.aspirecn.corpsocial.bundle.addrbook.facade.Contact;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.ContactEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 联系人简要信息
 *
 * @author mxs
 */
public class ContactBriefVO implements Serializable {
    public static final String[] FIELD_ARRAYS = {"id", "belongUserId", "name", "gender", "deptName",
            "mobilePhone", "email", "headImageUrl", "headLocalImageUrl", "hidePhone",
            "duty", "imStatus", "innerPhoneNumber", "initialCode", "deptId", "signature", "loginName", "userType"};
    public static final Map<String, Integer> FIELD_INDEX;
    /**
     *
     */
    private static final long serialVersionUID = 6990683815350587284L;

    static {
        FIELD_INDEX = new HashMap<String, Integer>();
        for (int i = 0; i < FIELD_ARRAYS.length; i++) {
            FIELD_INDEX.put(FIELD_ARRAYS[i].toLowerCase(), Integer.valueOf(i));
        }
    }

    //联系人ID
    private String id;
    /**
     * 所属人
     */
    private String belongUserId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 联系人性别
     */
    private int gender;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 邮件
     */
    private String email;
    /**
     * 头像
     */
    private String headImageUrl;
    /**
     * 修改签名
     */
    private String signature;
    /**
     * 是否隐藏手机号
     */
    private String hidePhone;
    /**
     * 职务
     */
    private String duty;
    //微信号状态：0-已开通，1-未开通，2-已激活，3-已关闭
    private String imStatus;
    /* 内线电话 */
    private String innerPhoneNumber;
    private String initialCode;
    private String deptId;
    private String loginName;
    private String userType;

    public ContactBriefVO() {
    }

    public ContactBriefVO(ContactEntity entity) {
        this.id = entity.getId();
        this.belongUserId = entity.getBelongUserId();
        this.name = entity.getName();
        this.gender = entity.getGender();
        this.deptName = entity.getDeptName();
        this.mobilePhone = entity.getMobilePhone();
        this.email = entity.getEmail();
        this.headImageUrl = entity.getHeadImageUrl();
        this.signature = entity.getSignature();
        this.hidePhone = entity.getHidePhone();
        this.duty = entity.getDuty();
        this.imStatus = entity.getImStatus();
        this.loginName = entity.getLoginName();
        this.userType = entity.getUserType();
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getHidePhone() {
        return hidePhone;
    }

    public void setHidePhone(String hidePhone) {
        this.hidePhone = hidePhone;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
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

    /**
     * //微信号状态：0-已开通，1-未开通，2-已激活，3-已关闭
     *
     * @return
     */
    public String getImStatus() {
        return imStatus;
    }

    public void setImStatus(String imStatus) {
        this.imStatus = imStatus;
    }

    public String getInnerPhoneNumber() {
        return innerPhoneNumber;
    }

    public void setInnerPhoneNumber(String innerPhoneNumber) {
        this.innerPhoneNumber = innerPhoneNumber;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Contact toContact() {
        Contact contact = new Contact();
        contact.setDeptName(getDeptName());
        contact.setId(getId());
        contact.setName(getName());
        contact.setImStatus(getImStatus());
        contact.setHeadImgUrl(getHeadImageUrl());
        contact.setInitialCode(getInitialCode());
        contact.setSignature(getSignature());
        contact.setMobilePhone(getMobilePhone());
        contact.setLoginName(loginName);
        contact.setUserType(userType);

        return contact;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContactBriefVO other = (ContactBriefVO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
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
