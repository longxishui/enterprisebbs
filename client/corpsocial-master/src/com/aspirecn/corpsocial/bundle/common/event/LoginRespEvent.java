/**
 * @(#) LoginRespEvent.java Created on 2013-11-18
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * The class <code>LoginRespEvent</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class LoginRespEvent extends BusEvent {

    private int respCode;

    private String message;

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
