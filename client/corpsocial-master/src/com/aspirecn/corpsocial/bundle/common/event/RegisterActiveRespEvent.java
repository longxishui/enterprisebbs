package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * @author lizhuo_a
 */
public class RegisterActiveRespEvent extends BusEvent {

    private int errorCode;

    private String message;

    public RegisterActiveRespEvent(int errorCode, String message) {
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
