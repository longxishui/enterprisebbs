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
public class ResetPasswordActiveEvent extends BusEvent {

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 激活码
     */
    private String activeCode;

    /**
     * 新密码
     */
    private String newPassword;

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

}
