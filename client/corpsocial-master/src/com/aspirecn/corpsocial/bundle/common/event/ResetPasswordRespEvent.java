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
public class ResetPasswordRespEvent extends BusEvent {

    private int errorCode;

    private String message;

    public ResetPasswordRespEvent(int errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
