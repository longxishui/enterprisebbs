package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 移除常用联系人，即将某联系人设置为不常用
 *
 * @author mxs
 */
public class RemoveFrequentlyContactEvent extends BusEvent {
    private String contactId;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
