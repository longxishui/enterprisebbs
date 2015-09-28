/**
 * @(#) AddressContactReceiveEvent.java Created on 2013-11-25
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * 接收联系人事件
 *
 * @author lizhuo_a
 */
public class ReceiveContactEvent extends BusEvent {
    private List<User> userList;

    /**
     * Getter of userList
     *
     * @return the userList
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * Setter of userList
     *
     * @param userList the userList to set
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
