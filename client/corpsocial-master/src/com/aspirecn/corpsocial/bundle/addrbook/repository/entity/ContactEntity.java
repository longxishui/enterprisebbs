/**
 * @(#) contact.java Created on 2013年11月4日
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

import com.aspirecn.corpsocial.bundle.addrbook.facade.Contact;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 联系人表
 *
 * @author lizhuo_a
 */
@DatabaseTable(tableName = "addrbook_contact")
public class ContactEntity {

    /**
     * 序列号
     */
    @DatabaseField(generatedId = true, unique = true)
    private int seqNo;
    /**
     * 对应服务端的联系人ID
     */
    @DatabaseField
    private String id;
    /**
     * 用于多账号识别
     */
    @DatabaseField
    private String belongUserId;
    /**
     * 登录帐号
     */
    @DatabaseField
    private String loginName;
    /**
     * 姓名
     */
    @DatabaseField(index = true, indexName = "IDX_NAME")
    private String name;
    /**
     * 工号
     */
    @DatabaseField
    private String jobNumber;
    /**
     * 联系人性别
     */
    @DatabaseField
    private int gender = 1;
    /**
     * 部门名称
     */
    @DatabaseField
    private String deptName;
    /**
     * 部门id
     */
    @DatabaseField(index = true, indexName = "IDX_DEPTID")
    private String deptId;
    /**
     * 签名
     */
    @DatabaseField
    private String signature;
    /**
     * 手机号
     */
    @DatabaseField(index = true, indexName = "IDX_MOBILEPHONE")
    private String mobilePhone;
    /**
     * 第2手机号
     */
    @DatabaseField
    private String mobilePhone2;
    /**
     * 固定电话
     */
    @DatabaseField
    private String phoneNumber;
    /**
     * 内线号码
     */
    @DatabaseField
    private String innerPhoneNumber;
    /**
     * 邮件
     */
    @DatabaseField
    private String email;
    /**
     * 出生日期
     */
    @DatabaseField
    private String birthday;
    /**
     * 头像
     */
    @DatabaseField
    private String headImageUrl;
    /**
     * 本地头像地址
     */
    @DatabaseField
    private String headLocalImageUrl;
    /**
     * 中文名字的全拼
     */
    @DatabaseField(index = true, indexName = "IDX_PINYIN")
    private String pinyin;
    /**
     * 拼音首字母
     */
    @DatabaseField(index = true, indexName = "IDX_INITIALCODE")
    private String initialCode;
    /**
     * 是否隐藏手机号, 0不隐藏，1隐藏
     */
    @DatabaseField
    private String hidePhone;
    /**
     * 所属企业
     */
    @DatabaseField
    private String corpId;
    /**
     * 职务
     */
    @DatabaseField
    private String duty;
    @DatabaseField
    private String note;
    @DatabaseField
    private String location;
    //微信号状态：0-已开通，1-未开通，2-已激活，3-已关闭
    @DatabaseField
    private String imStatus;
    //兼任部门信息，格式：部门编码_部门名称_职务，如果有多个则以|分隔。
    @DatabaseField(index = true, indexName = "IDX_PART_TIME_DEPT")
    private String partTimeDept;
    @DatabaseField
    private String sortNo;
    //人员类型0-正式员工，1-外包人员
    @DatabaseField
    private String userType;

    public ContactEntity() {
    }

    private ContactEntity(Builder build) {
        this.id = build.id;
        this.loginName = build.loginName;
        this.name = build.name;
        this.jobNumber = build.jobNumber;
        this.gender = build.gender;
        this.deptName = build.deptName;
        this.deptId = build.deptId;
        this.signature = build.signature;
        this.mobilePhone = build.mobilePhone;
        this.mobilePhone2 = build.mobilePhone2;
        this.phoneNumber = build.phoneNumber;
        this.innerPhoneNumber = build.innerPhoneNumber;
        this.email = build.email;
        this.birthday = build.birthday;
        this.headImageUrl = build.headImageUrl;
        this.headLocalImageUrl = build.headLocalImageUrl;
        this.pinyin = build.pinyin;
        this.initialCode = build.initialCode;
        this.hidePhone = build.hidePhone;
        this.corpId = build.corpId;
        this.duty = build.duty;
        this.note = build.note;
        this.location = build.location;
        this.imStatus = build.imStatus;
        this.partTimeDept = build.partTimeDept;
        this.sortNo = build.sortNo;
        this.userType = build.userType;
        this.belongUserId = build.belongUserId;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

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
        if (hidePhone.equals("1")) {
            return "";
        }

        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone2() {
        if ("1".equals(hidePhone)) {
            return "";
        }

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
        if ("1".equals(hidePhone)) {
            return "";
        }

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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public String getHidePhone() {
        return hidePhone;
    }

    public void setHidePhone(String hidePhone) {
        this.hidePhone = hidePhone;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadLocalImageUrl() {
        return headLocalImageUrl;
    }

    public void setHeadLocalImageUrl(String headLocalImageUrl) {
        this.headLocalImageUrl = headLocalImageUrl;
    }

    public String getImStatus() {
        return imStatus;
    }

    public void setImStatus(String imStatus) {
        this.imStatus = imStatus;
    }

    public String getPartTimeDept() {
        return partTimeDept;
    }

    public void setPartTimeDept(String partTimeDept) {
        this.partTimeDept = partTimeDept;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
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
        contact.setMobilePhone(this.getMobilePhone());
        contact.setHeadImgUrl(getHeadImageUrl());
        contact.setInitialCode(getInitialCode());
        contact.setSignature(getSignature());
        contact.setLoginName(getLoginName());
        contact.setUserType(getUserType());
        contact.setDuty(getDuty());

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
        ContactEntity other = (ContactEntity) obj;
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

    public static class Builder {
        private String id;
        private String loginName;
        private String name;
        private String jobNumber;
        private int gender = 1;
        private String deptName;
        private String deptId;
        private String signature;
        private String mobilePhone;
        private String mobilePhone2;
        private String phoneNumber;
        private String innerPhoneNumber;
        private String email;
        private String birthday;
        private String headImageUrl;
        private String headLocalImageUrl;
        private String pinyin;
        private String initialCode;
        private String hidePhone;
        private String corpId;
        private String duty;
        private String note;
        private String location;
        private String imStatus;
        private String partTimeDept;
        private String sortNo;
        private String userType;
        private String belongUserId;

        public Builder() {
        }

        public ContactEntity build() {
            return new ContactEntity(this);
        }

        public Builder id(String value) {
            this.id = value;

            return this;
        }

        public Builder loginName(String value) {
            this.loginName = value;

            return this;
        }

        public Builder name(String value) {
            this.name = value;

            return this;
        }

        public Builder jobNumber(String value) {
            this.jobNumber = value;

            return this;
        }

        public Builder gender(int value) {
            this.gender = value;

            return this;
        }

        public Builder deptName(String value) {
            this.deptName = value;

            return this;
        }

        public Builder deptId(String value) {
            this.deptId = value;

            return this;
        }

        public Builder signature(String value) {
            this.signature = value;

            return this;
        }

        public Builder mobilePhone(String value) {
            this.mobilePhone = value;

            return this;
        }

        public Builder mobilePhone2(String value) {
            this.mobilePhone2 = value;

            return this;
        }

        public Builder phoneNumber(String value) {
            this.phoneNumber = value;

            return this;
        }

        public Builder innerPhoneNumber(String value) {
            this.innerPhoneNumber = value;

            return this;
        }

        public Builder email(String value) {
            this.email = value;

            return this;
        }

        public Builder birthday(String value) {
            this.birthday = value;

            return this;
        }

        public Builder headImageUrl(String value) {
            this.headImageUrl = value;

            return this;
        }

        public Builder headLocalImageUrl(String value) {
            this.headLocalImageUrl = value;

            return this;
        }

        public Builder pinyin(String value) {
            this.pinyin = value;

            return this;
        }

        public Builder initialCode(String value) {
            this.initialCode = value;

            return this;
        }

        public Builder hidePhone(String value) {
            if (value.equals("0") || value.equals("1")) {
                this.hidePhone = value;
            } else {
                this.hidePhone = "0";

            }

            return this;
        }

        public Builder corpId(String value) {
            this.corpId = value;

            return this;
        }

        public Builder duty(String value) {
            this.duty = value;

            return this;
        }

        public Builder note(String value) {
            this.note = value;

            return this;
        }

        public Builder location(String value) {
            this.location = value;

            return this;
        }

        public Builder imStatus(String value) {
            this.imStatus = value;

            return this;
        }

        public Builder partTimeDept(String value) {
            this.partTimeDept = value;

            return this;
        }

        public Builder sortNo(String value) {
            this.sortNo = value;

            return this;
        }

        public Builder userType(String value) {
            this.userType = value;
            return this;
        }
    }
}
