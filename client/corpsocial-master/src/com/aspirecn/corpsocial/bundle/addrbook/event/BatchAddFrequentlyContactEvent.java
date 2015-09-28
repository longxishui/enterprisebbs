/**
 * @(#) AddressAddCommonContactEvent.java Created on 2013-12-9
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * The class <code>AddressAddCommonContactEvent</code>
 * <p/>
 * 批量添加常用联系人事件
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class BatchAddFrequentlyContactEvent extends BusEvent {
    private List<String> contactIds;

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }
}
