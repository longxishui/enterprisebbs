package com.aspirecn.corpsocial.bundle.addrbook.repository.vo;

import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FrequentlyContactType;
import com.aspirecn.corpsocial.common.util.DateUtils;

import cn.trinea.android.common.util.StringUtils;


/**
 * 常用联系人信息
 *
 * @author mxs
 */
public final class FrequentlyContactVO {
    private final String contactId;
    private final long lastTime; //秒数
    private final FrequentlyContactType type;
    private final String name;
    private final String headImageUrl;
    private final String deptName;
    private final String mobilePhone;
    private final String fullDeptName;
    private final String signature;
    private final String email;
    private final String titleType;
    private final String titleName;

    private FrequentlyContactVO(Builder build) {
        this.contactId = build.contactId;
        this.lastTime = build.lastTime;
        this.type = build.type;
        this.name = build.name;
        this.headImageUrl = build.headImageUrl;
        this.deptName = build.deptName;
        this.fullDeptName = build.fullDeptName;
        this.mobilePhone = build.mobilePhone;
        this.signature = build.signature;
        this.email = build.email;
        this.titleName = build.titleName;
        this.titleType = build.titleType;
    }

    public String getTitleType() {
        return titleType;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getEmail() {

        return email;
    }

    public String getContactId() {
        return contactId;
    }

    /**
     * 时间，秒数格式。
     *
     * @return
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * 按微信的格式返回最后时间。
     *
     * @return
     */
    public String getWeiXinTime() {
        return DateUtils.getWeiXinTime(lastTime * 1000);
    }

    public FrequentlyContactType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getFullDeptName() {
        return fullDeptName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((contactId == null) ? 0 : contactId.hashCode());
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
        FrequentlyContactVO other = (FrequentlyContactVO) obj;
        if (contactId == null) {
            if (other.contactId != null)
                return false;
        } else if (!contactId.equals(other.contactId))
            return false;
        return true;
    }

    public static class Builder {
        private String contactId;
        private long lastTime;
        private FrequentlyContactType type;
        private String name;
        private String headImageUrl;
        private String deptName;
        private String fullDeptName;
        private String mobilePhone;
        private String signature;
        private String email;
        private String titleType;
        private String titleName;

        public Builder() {
        }

        public FrequentlyContactVO build() {
            return new FrequentlyContactVO(this);
        }

        public Builder contactId(String value) {
            this.contactId = value;

            return this;
        }

        public Builder email(String value) {
            this.email = value;
            return this;
        }

        /**
         * @param value 时间（秒数）
         * @return
         */
        public Builder lastTime(long value) {
            this.lastTime = value;

            return this;
        }

        public Builder type(FrequentlyContactType value) {
            this.type = value;

            return this;
        }

        public Builder name(String value) {
            this.name = value;

            return this;
        }

        public Builder headImageUrl(String value) {
            this.headImageUrl = value;

            return this;
        }

        public Builder deptName(String value) {
            if (StringUtils.isBlank(value)) {
                this.deptName = "";
                return this;
            } else {
                //只取最后两级。
                String[] levelArray = value.split("\\/");
                if (levelArray.length > 2) {
                    this.deptName = String.format("%s/%s",
                            levelArray[levelArray.length - 2],
                            levelArray[levelArray.length - 1]);
                } else {
                    this.deptName = value;
                }
                return this;
            }
        }

        public Builder fullDeptName(String value) {
            this.fullDeptName = value;

            return this;
        }

        public Builder mobilePhone(String value) {
            this.mobilePhone = value;

            return this;
        }

        public Builder signature(String value) {
            this.signature = value;

            return this;
        }

        public Builder titleType(String value) {
            this.titleType = value;

            return this;
        }

        public Builder titleName(String value) {
            this.titleName = value;
            return this;
        }

    }
}
