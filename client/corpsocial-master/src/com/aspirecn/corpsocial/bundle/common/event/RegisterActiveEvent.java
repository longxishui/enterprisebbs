package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * @author lizhuo_a
 */
public class RegisterActiveEvent extends BusEvent {

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 激活码
     */
    private String activeCode;

    /**
     * 密码
     */
    private String password;

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

}
