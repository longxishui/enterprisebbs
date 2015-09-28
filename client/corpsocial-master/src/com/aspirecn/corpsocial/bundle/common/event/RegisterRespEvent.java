package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * @author lizhuo_a
 */
public class RegisterRespEvent extends BusEvent {
    private int errorCode;

    private String message;

    public RegisterRespEvent(int errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
