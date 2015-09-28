/**
 * @(#) FrequentlyContacts.java Created on 2013年12月5日
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The class <code>FrequentlyContacts</code> 常用联系人
 *
 * @author danel
 * @version 1.0
 */
@DatabaseTable(tableName = "addrbook_freq_contact")
public class FrequentlyContactEntity {

    /**
     * 序列号
     */
    @DatabaseField(generatedId = true)
    private int seqNo;

    /**
     * 用于多账号识别
     */
    @DatabaseField
    private String belongUserId;

    // 某个联系人的id
    @DatabaseField
    private String contactId;

    // 最后联系的时间，秒数
    @DatabaseField(columnName = "LastTime")
    private long lastTime = 0;

    @DatabaseField(columnName = "type")
    private int type = 0;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    /**
     * @return 秒
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * @param second 秒
     */
    public void setLastTime(long second) {
        this.lastTime = second;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FrequentlyContactType getFrequentlyContactType() {
        return FrequentlyContactType.getInstance(type);
    }

    public void setFrequentlyContactType(FrequentlyContactType t) {
        this.type = t.value();
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
        FrequentlyContactEntity other = (FrequentlyContactEntity) obj;
        if (contactId == null) {
            if (other.contactId != null)
                return false;
        } else if (!contactId.equals(other.contactId))
            return false;
        return true;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
}
