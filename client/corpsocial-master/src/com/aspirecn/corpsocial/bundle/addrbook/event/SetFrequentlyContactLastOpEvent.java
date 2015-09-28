/**
 * @(#) AddressAddCommonContactEvent.java Created on 2013-12-9
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FrequentlyContactType;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 常用联系人最后操作事件
 *
 * @author meixuesong
 * @version 1.0
 */
public class SetFrequentlyContactLastOpEvent extends BusEvent {
    private FrequentlyContactType type;
    private String contactId;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public FrequentlyContactType getType() {
        return type;
    }

    public void setType(FrequentlyContactType type) {
        this.type = type;
    }
}
