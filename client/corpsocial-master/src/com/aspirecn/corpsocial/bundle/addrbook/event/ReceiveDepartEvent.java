/**
 * @(#) AddressDepartReceiveEvent.java Created on 2013-11-25
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;

import java.util.List;

/**
 * The class <code>AddressDepartReceiveEvent</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class ReceiveDepartEvent extends BusEvent {

    private List<Dept> departList;

    /**
     * Getter of departList
     *
     * @return the departList
     */
    public List<Dept> getDepartList() {
        return departList;
    }

    /**
     * Setter of departList
     *
     * @param departList the departList to set
     */
    public void setDepartList(List<Dept> departList) {
        this.departList = departList;
    }

}
