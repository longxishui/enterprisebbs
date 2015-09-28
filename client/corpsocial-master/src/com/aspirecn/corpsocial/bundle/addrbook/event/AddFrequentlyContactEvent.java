/**
 * @(#) AddressAddCommonContactEvent.java Created on 2013-12-9
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * The class <code>AddressAddCommonContactEvent</code>
 * <p/>
 * 添加常用联系人事件
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class AddFrequentlyContactEvent extends BusEvent {
    private String contactId;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
