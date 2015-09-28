/**
 * @(#) LoginEvent.java Created on 2013-11-18
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * The class <code>LoginEvent</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class ResetPasswordEvent extends BusEvent {

    /**
     * 手机号码
     */
    private String mobilePhone;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

}
