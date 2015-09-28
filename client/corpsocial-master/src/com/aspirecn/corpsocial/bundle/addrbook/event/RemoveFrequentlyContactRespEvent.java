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
 * 删除常用联系人响应事件
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class RemoveFrequentlyContactRespEvent extends BusEvent {
    private int status;
    private String contactId;

    public RemoveFrequentlyContactRespEvent(String contactId, int status) {
        this.contactId = contactId;
        this.status = status;
    }

    /**
     * 返回状态，0表示成功
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    public String getContactId() {
        return contactId;
    }
}
