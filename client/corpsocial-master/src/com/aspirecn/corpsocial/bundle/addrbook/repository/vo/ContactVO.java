package com.aspirecn.corpsocial.bundle.addrbook.repository.vo;

import java.util.ArrayList;
import java.util.List;


/**
 * 联系人详情
 *
 * @author mxs
 */
public class ContactVO {
    //联系人ID
    private String id;
    /**
     * 登录帐号
     */
    private String loginName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 工号
     */
    private String jobNumber;
    /**
     * 联系人性别
     */
    private int gender;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 签名
     */
    private String signature;
    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 第2手机号
     */
    private String mobilePhone2;
    /**
     * 固定电话
     */
    private String phoneNumber;
    /**
     * 内线号码
     */
    private String innerPhoneNumber;
    /**
     * 邮件
     */
    private String email;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 头像
     */
    private String headImageUrl;
    /**
     * 是否隐藏手机号
     */
    private Boolean hidePhone = false;
    /**
     * 职务
     */
    private String duty;
    private String note;
    private boolean frequent = false;
    private String userType;
    private List<PartTimeDeptInfo> partTimeDeptInfo = new ArrayList<PartTimeDeptInfo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone2() {
        return mobilePhone2;
    }

    public void setMobilePhone2(String mobilePhone2) {
        this.mobilePhone2 = mobilePhone2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInnerPhoneNumber() {
        return innerPhoneNumber;
    }

    public void setInnerPhoneNumber(String innerPhoneNumber) {
        this.innerPhoneNumber = innerPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public Boolean getHidePhone() {
        return hidePhone;
    }

    public void setHidePhone(Boolean hidePhone) {
        this.hidePhone = hidePhone;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isFrequent() {
        return frequent;
    }

    public void setFrequent(boolean isFrequent) {
        this.frequent = isFrequent;
    }

    public List<PartTimeDeptInfo> getPartTimeDeptInfo() {
        return partTimeDeptInfo;
    }

    public void setPartTimeDeptInfo(List<PartTimeDeptInfo> partTimeDeptInfo) {
        this.partTimeDeptInfo = partTimeDeptInfo;
    }

    /**
     * 兼职部门信息
     *
     * @author mxs
     */
    public static class PartTimeDeptInfo {
        public String deptId;
        public String deptName;
        public String duty;

        public PartTimeDeptInfo(String id, String name, String duty) {
            this.deptId = id;
            this.deptName = name;
            this.duty = duty;
        }
    }
}
