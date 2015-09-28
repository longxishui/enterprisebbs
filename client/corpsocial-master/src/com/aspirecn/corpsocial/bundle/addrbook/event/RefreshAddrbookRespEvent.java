/**
 * @(#) VeritifyDataEvent.java Created on 2013-11-26
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;


/**
 * 刷新通讯录结果反馈事件
 *
 * @author lizhuo_a
 */
public class RefreshAddrbookRespEvent extends BusEvent {
    private int errorCode;
    private String message;

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
