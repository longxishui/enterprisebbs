package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * 获取联系人详情事件
 *
 * @author mxs
 */
public class GetContactsEvent extends BusEvent {
    private List<String> contactIds;

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }
}
