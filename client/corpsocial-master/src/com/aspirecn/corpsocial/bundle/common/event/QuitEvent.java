/**
 * @(#) LoginEvent.java Created on 2013-11-18
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 退出事件
 *
 * @author lizhuo_a
 */
public class QuitEvent extends BusEvent {

    private String quitInfo;

    public String getQuitInfo() {
        return quitInfo;
    }

    public void setQuitInfo(String quitInfo) {
        this.quitInfo = quitInfo;
    }

}
